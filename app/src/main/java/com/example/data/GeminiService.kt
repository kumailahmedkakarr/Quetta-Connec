package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Get the API Key securely from BuildConfig
    private fun getApiKey(): String {
        return BuildConfig.GEMINI_API_KEY
    }

    /**
     * Ask the AI Assistant a question. Uses standard Gemini 3.5 Flash via REST.
     * Includes localized Quetta personality and multilingual intelligence.
     */
    suspend fun askAssistant(prompt: String, chatHistory: List<Pair<String, String>> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key is not configured. Falling back to local smart assistant.")
            return@withContext getLocalFallbackResponse(prompt)
        }

        try {
            // Build the contents array with chat history + current prompt
            val contentsArray = JSONArray()

            // System Instruction (sent as model context)
            val systemInstruction = "You are 'Saeen AI', the humorous, highly intelligent, senior digital representative of Quetta Connect. " +
                    "Your personality is warm, wittily humorous (with typical Quetta/Balochistani hospitality, references to Sajji, green tea/Kahwah, cold weather, and local areas like Sariab, Jinnah Town, and Hanna Lake), " +
                    "and extremely civic-minded. Answer in the language of the prompt (English, Urdu, Balochi, or Brahui). " +
                    "Always suggest relevant smart city departments for issues, explain procedures simply, and make citizens smile. " +
                    "Do not mention complex JSON or system paths. Keep responses concise, helpful, and highly engaging."

            // Add history
            for (turn in chatHistory) {
                val role = if (turn.first == "user") "user" else "model"
                contentsArray.put(JSONObject().apply {
                    put("role", role)
                    put("parts", JSONArray().put(JSONObject().put("text", turn.second)))
                })
            }

            // Add current prompt
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            })

            // Complete request body
            val requestBody = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemInstruction))))
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 800)
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e(TAG, "Gemini API error: Status Code ${response.code}, Response: $errBody")
                    return@withContext "Saeen! It seems the internet winds are too strong today, or the API is taking a Kahwah break (Status ${response.code}). Let me tell you locally: " + getLocalFallbackResponse(prompt)
                }

                val responseBody = response.body?.string() ?: return@withContext "Empty response from Gemini server."
                val jsonObject = JSONObject(responseBody)
                val text = jsonObject.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                return@withContext text
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini call failed with exception", e)
            return@withContext "Oh Saeen! The electric wires are shaking (Error: ${e.localizedMessage}). Here is your local response: " + getLocalFallbackResponse(prompt)
        }
    }

    /**
     * Categorizes a complaint description, returning a recommended Category,
     * Assignee Department, and Priority level.
     */
    suspend fun categorizeComplaint(description: String): ComplaintAIInfo = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        val defaultInfo = getLocalComplaintCategorizer(description)

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext defaultInfo
        }

        try {
            val prompt = "Analyze this urban civic complaint from Quetta and categorize it. " +
                    "Complaint description: \"$description\" " +
                    "Return a JSON object with EXACTLY three fields: " +
                    "\"category\" (Choose strictly from: \"Road Damage\", \"Garbage/Waste\", \"Water Leakage\", \"Electricity\", \"Street Lights\", \"Traffic Signal\"), " +
                    "\"assigneeDept\" (Choose strictly from: \"Quetta Metropolitan Corporation (QMC)\", \"WASA Quetta\", \"Balochistan C&W Department\", \"QMC Streetlights Wing\", \"Quetta Traffic Police\"), " +
                    "\"isEmergency\" (true or false). " +
                    "Return ONLY raw JSON, do not include markdown blocks like ```json."

            val requestBody = JSONObject().apply {
                put("contents", JSONArray().put(JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", prompt)))
                }))
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext defaultInfo

                val body = response.body?.string() ?: return@withContext defaultInfo
                val jsonObject = JSONObject(body)
                val textResponse = jsonObject.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                val resJson = JSONObject(textResponse.trim())
                return@withContext ComplaintAIInfo(
                    category = resJson.optString("category", defaultInfo.category),
                    assigneeDept = resJson.optString("assigneeDept", defaultInfo.assigneeDept),
                    isEmergency = resJson.optBoolean("isEmergency", defaultInfo.isEmergency)
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Complaint categorization failed, using fallback.", e)
            return@withContext defaultInfo
        }
    }

    /**
     * Helper to perform local rule-based complaint analysis when off-line or api key is missing.
     */
    private fun getLocalComplaintCategorizer(desc: String): ComplaintAIInfo {
        val lower = desc.lowercase()
        return when {
            lower.contains("water") || lower.contains("leak") || lower.contains("gutter") || lower.contains("pipeline") -> {
                ComplaintAIInfo("Water Leakage", "WASA Quetta", false)
            }
            lower.contains("garbage") || lower.contains("trash") || lower.contains("dump") || lower.contains("kuchra") || lower.contains("smell") -> {
                ComplaintAIInfo("Garbage/Waste", "Quetta Metropolitan Corporation (QMC)", false)
            }
            lower.contains("light") || lower.contains("bulb") || lower.contains("andhera") || lower.contains("dark") || lower.contains("khamba") -> {
                ComplaintAIInfo("Street Lights", "QMC Streetlights Wing", false)
            }
            lower.contains("wire") || lower.contains("electricity") || lower.contains("current") || lower.contains("bijli") || lower.contains("transformer") -> {
                ComplaintAIInfo("Electricity", "WAPDA / QESC Quetta", true)
            }
            lower.contains("pothole") || lower.contains("road") || lower.contains("cracks") || lower.contains("sadak") || lower.contains("gaddha") -> {
                ComplaintAIInfo("Road Damage", "Balochistan C&W Department", false)
            }
            lower.contains("traffic") || lower.contains("signal") || lower.contains("jam") -> {
                ComplaintAIInfo("Traffic Signal", "Quetta Traffic Police", false)
            }
            else -> {
                ComplaintAIInfo("Road Damage", "Quetta Metropolitan Corporation (QMC)", false)
            }
        }
    }

    /**
     * Clever, humorous, and accurate local fallbacks.
     */
    private fun getLocalFallbackResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("sajji") || lower.contains("eat") || lower.contains("food") -> {
                "Saeen! When talking about food in Quetta, Sajji is the undisputed King! Head straight to Lehri Sajji on Jinnah Road or Cantonment area. Let the slow-cooked salted mutton or chicken do the talking, washed down with nice green tea (Kahwah). Shall I add this to the complaint register under 'Excessive Deliciousness'?"
            }
            lower.contains("cold") || lower.contains("weather") || lower.contains("temperature") -> {
                "Quetta's weather has two modes: 'Sun-kissed Desert' and 'Kandahari Chill'. In winters, the famous 'Kandahari Hawa' blows from the northwest, making us appreciate gas heaters, thick Balochi blankets, and woolly hats. Wear three layers, Saeen! No compromise!"
            }
            lower.contains("complaint") || lower.contains("report") -> {
                "To report a complaint, simply tap the '+' Floating Action Button in the Complaints tab! Our AI will analyze your description, select the right department (QMC, WASA, or C&W), and register it instantly in the local Room database. Try it now!"
            }
            lower.contains("hanna") || lower.contains("lake") || lower.contains("tourist") || lower.contains("visit") -> {
                "Saeen! Hanna Lake is Quetta's jewel, just 10 kilometers away. Although water levels vary, sitting near the lakeside with a cup of hot Chai in the evening is pure bliss. Also, visit Urak Valley nearby for the world's sweet apples!"
            }
            lower.contains("blood") || lower.contains("donor") -> {
                "To search for blood donors or register yourself as a life-saver, go to the 'Services' tab and select 'Blood Donation'. You can filter by blood group and find donors in Sariab, Satellite Town, and Jinnah Town."
            }
            lower.contains("emergency") || lower.contains("sos") -> {
                "If you are facing an emergency, tap the big red 'SOS' button on the Home screen. It allows one-tap calling to Rescue 1122, Police 15, or Fire Brigade, and lets you copy or share your simulated GPS coordinates instantly!"
            }
            else -> {
                "Lala, I hear you! Quetta Connect is fully functional! You can explore the Interactive Map of Quetta, check local announcements, register complaints, find nearby hospitals, or review jobs. If you register a complaint or blood donor, it saves directly into our secure device storage. How can I help you today?"
            }
        }
    }
}

data class ComplaintAIInfo(
    val category: String,
    val assigneeDept: String,
    val isEmergency: Boolean
)
