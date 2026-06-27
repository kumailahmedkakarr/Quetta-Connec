package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Announcement
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.BloodDonor
import com.example.data.Complaint
import com.example.data.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuettaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository

    // Central state flows
    val complaints: StateFlow<List<Complaint>>
    val bloodDonors: StateFlow<List<BloodDonor>>
    val announcements: StateFlow<List<Announcement>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database)

        // Bind flows to the UI lifetime
        complaints = repository.complaints.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        bloodDonors = repository.bloodDonors.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        announcements = repository.announcements.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial mock database items if empty
        viewModelScope.launch {
            repository.prepopulateIfEmpty()
        }
    }

    // ==========================================
    // SECTIONS & APP CONTROLS
    // ==========================================
    private val _currentTab = MutableStateFlow("Home")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    fun selectTab(tab: String) {
        _currentTab.value = tab
    }

    private val _currentRole = MutableStateFlow("Citizen") // "Citizen", "Moderator", "Government Officer", "Department Admin", "Super Admin"
    val currentRole: StateFlow<String> = _currentRole.asStateFlow()

    fun selectRole(role: String) {
        _currentRole.value = role
    }

    private val _currentLanguage = MutableStateFlow("English") // "English", "Urdu", "Balochi", "Brahui"
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun selectLanguage(lang: String) {
        _currentLanguage.value = lang
    }

    // ==========================================
    // CHATBOT / AI CITY ASSISTANT STATE
    // ==========================================
    private val _chatMessages = MutableStateFlow<List<Pair<String, String>>>(
        listOf(
            "model" to "Saeen! Welcome to Quetta Connect AI Assistant. Ask me anything about municipal issues, blood donors, Urak Valley apples, or where to find the best mutton Sajji in Quetta! I can guide you in English, Urdu, Balochi, or Brahui."
        )
    )
    val chatMessages: StateFlow<List<Pair<String, String>>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    fun sendMessage(prompt: String) {
        if (prompt.isBlank()) return

        val currentList = _chatMessages.value.toMutableList()
        currentList.add("user" to prompt)
        _chatMessages.value = currentList

        _isChatLoading.value = true

        viewModelScope.launch {
            // Get last few messages for context
            val history = _chatMessages.value.takeLast(10).dropLast(1)
            val responseText = GeminiService.askAssistant(prompt, history)

            val updatedList = _chatMessages.value.toMutableList()
            updatedList.add("model" to responseText)
            _chatMessages.value = updatedList

            _isChatLoading.value = false
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            "model" to "Chat history cleared. Saeen! Ask me a fresh query. My teapot is full and ready!"
        )
    }

    // ==========================================
    // COMPLAINT REPORTING STATE
    // ==========================================
    private val _newComplaintTitle = MutableStateFlow("")
    val newComplaintTitle: StateFlow<String> = _newComplaintTitle.asStateFlow()

    private val _newComplaintDescription = MutableStateFlow("")
    val newComplaintDescription: StateFlow<String> = _newComplaintDescription.asStateFlow()

    private val _isCategorizing = MutableStateFlow(false)
    val isCategorizing: StateFlow<Boolean> = _isCategorizing.asStateFlow()

    fun updateComplaintTitle(title: String) {
        _newComplaintTitle.value = title
    }

    fun updateComplaintDescription(desc: String) {
        _newComplaintDescription.value = desc
    }

    fun submitComplaint(
        title: String,
        description: String,
        category: String,
        dept: String,
        lat: Double,
        lng: Double,
        citizen: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val complaint = Complaint(
                title = title,
                description = description,
                category = category,
                status = "Pending",
                latitude = lat,
                longitude = lng,
                citizenName = citizen,
                assigneeDept = dept,
                citizenComments = "Reported via mobile application."
            )
            repository.insertComplaint(complaint)

            // Reset form
            _newComplaintTitle.value = ""
            _newComplaintDescription.value = ""
            onSuccess()
        }
    }

    // Run AI analysis of complaint text to categorize and suggest assignee
    fun analyzeComplaintWithAI(desc: String, onAnalyzed: (category: String, dept: String, isEmergency: Boolean) -> Unit) {
        if (desc.isBlank()) return
        _isCategorizing.value = true
        viewModelScope.launch {
            val aiInfo = GeminiService.categorizeComplaint(desc)
            _isCategorizing.value = false
            onAnalyzed(aiInfo.category, aiInfo.assigneeDept, aiInfo.isEmergency)
        }
    }

    fun updateComplaintStatus(complaintId: Long, newStatus: String) {
        viewModelScope.launch {
            val original = complaints.value.find { it.id == complaintId } ?: return@launch
            val updated = original.copy(status = newStatus)
            repository.updateComplaint(updated)
        }
    }

    // ==========================================
    // BLOOD DONOR REGISTRATION STATE
    // ==========================================
    fun registerBloodDonor(name: String, bloodGroup: String, contact: String, area: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val donor = BloodDonor(
                name = name,
                bloodGroup = bloodGroup,
                contact = contact,
                area = area,
                isAvailable = true
            )
            repository.insertDonor(donor)
            onSuccess()
        }
    }

    // ==========================================
    // SEARCH & FILTER STATES (Map, News, Directory)
    // ==========================================
    private val _mapFilter = MutableStateFlow("All") // "All", "Hospitals", "Police", "Tourist", "Schools", "Mosques"
    val mapFilter: StateFlow<String> = _mapFilter.asStateFlow()

    fun updateMapFilter(filter: String) {
        _mapFilter.value = filter
    }

    private val _directorySearch = MutableStateFlow("")
    val directorySearch: StateFlow<String> = _directorySearch.asStateFlow()

    fun updateDirectorySearch(query: String) {
        _directorySearch.value = query
    }

    // ==========================================
    // NEWS / BROADCASTS CREATION
    // ==========================================
    fun publishAlert(title: String, content: String, type: String, isAlert: Boolean) {
        viewModelScope.launch {
            repository.insertAnnouncement(
                Announcement(
                    title = title,
                    content = content,
                    type = type,
                    isAlert = isAlert
                )
            )
        }
    }
}
