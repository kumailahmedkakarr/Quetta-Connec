package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Announcement
import com.example.data.BloodDonor
import com.example.data.Complaint
import com.example.data.GeminiService
import com.example.ui.viewmodel.QuettaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ==========================================
// TRANSLATION SYSTEM
// ==========================================
object QuettaLocale {
    val dictionary = mapOf(
        "English" to mapOf(
            "app_tagline" to "One City. One Platform.",
            "welcome" to "Salam, Saeen!",
            "home_subtitle" to "Welcome to Quetta Smart Civic Center",
            "emergency_sos" to "EMERGENCY SOS",
            "search_city" to "Search Quetta services...",
            "weather" to "Weather",
            "air_quality" to "Air Quality",
            "complaints" to "Complaints",
            "map" to "Interactive Map",
            "ai_assistant" to "Saeen AI Chat",
            "services" to "Services",
            "admin" to "Control Room",
            "active_complaints" to "Active Complaints",
            "report_complaint" to "Report a Complaint",
            "title" to "Title",
            "desc" to "Describe the issue...",
            "submit" to "Submit",
            "auto_cat" to "AI Categorize & Assign",
            "blood_donor" to "Blood Donation",
            "job_portal" to "Job Portal",
            "tourism" to "Tourism",
            "events" to "Events",
            "doc_guide" to "Documents Guide",
            "business_dir" to "Business Directory",
            "role" to "Switch Role",
            "lang" to "Language",
            "register_donor" to "Register as Donor",
            "analytics" to "Municipal Analytics",
            "announcements" to "Emergency Broadcasts"
        ),
        "Urdu" to mapOf(
            "app_tagline" to "ایک شہر، ایک پلیٹ فارم۔",
            "welcome" to "سلام، سائیں!",
            "home_subtitle" to "کوئٹہ اسمارٹ سٹیزن پورٹل میں خوش آمدید",
            "emergency_sos" to "ہنگامی مدد (SOS)",
            "search_city" to "کوئٹہ کی خدمات تلاش کریں...",
            "weather" to "موسم",
            "air_quality" to "ہوا کا معیار",
            "complaints" to "شکایات",
            "map" to "نقشہ",
            "ai_assistant" to "سائیں اے آئی",
            "services" to "خدمات",
            "admin" to "کنٹرول روم",
            "active_complaints" to "شکایات بورڈ",
            "report_complaint" to "شکایت درج کریں",
            "title" to "عنوان",
            "desc" to "مسئلہ بیان کریں...",
            "submit" to "جمع کروائیں",
            "auto_cat" to "اے آئی کیٹیگری اور تفویض",
            "blood_donor" to "عطیہ خون",
            "job_portal" to "نوکریاں",
            "tourism" to "سیاحت",
            "events" to "تقریبات",
            "doc_guide" to "دستاویزات گائیڈ",
            "business_dir" to "کاروباری ڈائریکٹری",
            "role" to "کردار تبدیل کریں",
            "lang" to "زبان",
            "register_donor" to "عطیہ دہندہ بنیں",
            "analytics" to "میونسپل تجزیات",
            "announcements" to "ہنگامی اعلانات"
        ),
        "Balochi" to mapOf(
            "app_tagline" to "یک شہر۔ یک پلیٹ فارم۔",
            "welcome" to "وش اتکے، سائیں!",
            "home_subtitle" to "کوئٹہ اسمارٹ سٹیزن پورٹل ءَ وش اتکے",
            "emergency_sos" to "مدد لوٹگ (SOS)",
            "search_city" to "خدمت پٹ و لوٹ کں...",
            "weather" to "موسم",
            "air_quality" to "ہوا",
            "complaints" to "شکایات",
            "map" to "نکشہ",
            "ai_assistant" to "سائیں اے آئی",
            "services" to "خدمت",
            "admin" to "کنٹرول روم",
            "active_complaints" to "شکایات",
            "report_complaint" to "شکایت نبیس کں",
            "title" to "عنوان",
            "desc" to "مسئلہ نبیس کں...",
            "submit" to "دیم دیگ",
            "auto_cat" to "اے آئی کیٹیگری",
            "blood_donor" to "خون ءِ عطیہ",
            "job_portal" to "کاروبار و نوکری",
            "tourism" to "سند و سیاحت",
            "events" to "دیوان",
            "doc_guide" to "دستاویز گائیڈ",
            "business_dir" to "کاروبار ڈائریکٹری",
            "role" to "کردار",
            "lang" to "زبان",
            "register_donor" to "خون دہندہ بنیں",
            "analytics" to "تجزیات",
            "announcements" to "اعلانات"
        ),
        "Brahui" to mapOf(
            "app_tagline" to "اسِٹ شہر۔ اسِٹ پلیٹ فارم۔",
            "welcome" to "بشخندہ، سائیں!",
            "home_subtitle" to "کوئٹہ اسمارٹ سٹیزن پورٹل اٹ بسلامت مریرے",
            "emergency_sos" to "مدد خواہنگ (SOS)",
            "search_city" to "خدمت پٹ و خواجہ...",
            "weather" to "موسم",
            "air_quality" to "ہوا",
            "complaints" to "شکایت آک",
            "map" to "نکشہ",
            "ai_assistant" to "سائیں اے آئی",
            "services" to "خدمت آک",
            "admin" to "کنٹرول روم",
            "active_complaints" to "شکایت آک",
            "report_complaint" to "شکایت درج کرک",
            "title" to "عنوان",
            "desc" to "مسئلہ ءِ کسر کرک...",
            "submit" to "دیم تننگ",
            "auto_cat" to "اے آئی کیٹیگری",
            "blood_donor" to "خون تننگ",
            "job_portal" to "نوکری تا پٹ و پول",
            "tourism" to "سیاحت",
            "events" to "تقریب آک",
            "doc_guide" to "دستاویزات رہنما",
            "business_dir" to "کاروبار ڈائریکٹری",
            "role" to "کردار",
            "lang" to "زبان",
            "register_donor" to "خون تنندہ مریرے",
            "analytics" to "تجزیات",
            "announcements" to "اعلانات آک"
        )
    )

    fun get(key: String, lang: String): String {
        return dictionary[lang]?.get(key) ?: dictionary["English"]?.get(key) ?: key
    }
}

// ==========================================
// CORE LAYOUT COMPOSABLE
// ==========================================
@Composable
fun QuettaMainLayout(viewModel: QuettaViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()

    var showSosDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            QuettaBottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) },
                currentLang = currentLang,
                currentRole = currentRole
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Custom Branding & Role bar
                QuettaHeaderBar(
                    viewModel = viewModel,
                    currentRole = currentRole,
                    currentLang = currentLang,
                    onSosClicked = { showSosDialog = true }
                )

                // Sub screen render
                Box(modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(200))
                        },
                        label = "ScreenTransition"
                    ) { tab ->
                        when (tab) {
                            "Home" -> HomeScreen(viewModel, currentLang, onSosClicked = { showSosDialog = true })
                            "Complaints" -> ComplaintsScreen(viewModel, currentLang)
                            "Map" -> MapScreen(viewModel, currentLang)
                            "AI Assistant" -> AiChatScreen(viewModel, currentLang)
                            "Services" -> ServicesScreen(viewModel, currentLang)
                            "Admin" -> AdminScreen(viewModel, currentLang)
                        }
                    }
                }
            }

            // Floating SOS button if on Home or Services
            if (currentTab == "Home" || currentTab == "Services") {
                FloatingActionButton(
                    onClick = { showSosDialog = true },
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .testTag("floating_sos_button"),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "SOS Button"
                    )
                }
            }
        }
    }

    // SOS Emergency Overlay Modal
    if (showSosDialog) {
        AlertDialog(
            onDismissRequest = { showSosDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Dangerous, contentDescription = null, tint = Color.Red, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EMERGENCY BROADCAST",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Your simulated live coordinates:",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Latitude: 30.1798° N (Quetta Cantt)", fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 13.sp)
                            Text("Longitude: 66.9754° E", fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 13.sp)
                            Text("Accuracy: ±4 meters", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Text("Select department to place simulated call:", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))

                    val contextLocal = LocalContext.current
                    val callButton: @Composable (String, String, Color) -> Unit = { deptName, number, color ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    Toast.makeText(contextLocal, "Simulating emergency call to $deptName ($number)...", Toast.LENGTH_LONG).show()
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(color.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(deptName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(number, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            }
                        }
                    }

                    callButton("Police Helpline (Madadgar)", "15", Color(0xFF1E3A8A))
                    callButton("Rescue Emergency Service", "1122", Color(0xFFDC2626))
                    callButton("QMC Fire Station", "081-9202155", Color(0xFFEA580C))
                    callButton("Women & Child Protection", "1099", Color(0xFFDB2777))
                    callButton("Edhi Ambulance Quetta", "115", Color(0xFF16A34A))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Location coordinates shared successfully with responders!", Toast.LENGTH_SHORT).show()
                        showSosDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Share Location")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSosDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

// ==========================================
// CUSTOM COMPONENT: HEADER BAR
// ==========================================
@Composable
fun QuettaHeaderBar(
    viewModel: QuettaViewModel,
    currentRole: String,
    currentLang: String,
    onSosClicked: () -> Unit
) {
    var expandedLang by remember { mutableStateOf(false) }
    var expandedRole by remember { mutableStateOf(false) }

    Surface(
        shadowElevation = 1.dp,
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title and Location Context
                Column {
                    Text(
                        text = "QUETTA CONNECT",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        color = Color(0xFF2563EB) // Royal Blue 600
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Green active dot
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF22C55E), CircleShape)
                        )
                        Text(
                            text = "Quetta, PK",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF0F172A) // Slate 900
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Actions: Lang dropdown, Role Switcher
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Notification bell/button styled minimalist
                    IconButton(
                        onClick = onSosClicked,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFFEF2F2), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Language dropdown
                    Box {
                        IconButton(
                            onClick = { expandedLang = true },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFF1F5F9), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = expandedLang,
                            onDismissRequest = { expandedLang = false }
                        ) {
                            listOf("English", "Urdu", "Balochi", "Brahui").forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang, fontSize = 13.sp) },
                                    onClick = {
                                        viewModel.selectLanguage(lang)
                                        expandedLang = false
                                    }
                                )
                            }
                        }
                    }

                    // Role selector badge dropdown
                    Box {
                        TextButton(
                            onClick = { expandedRole = true },
                            modifier = Modifier
                                .height(36.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupervisedUserCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF2563EB)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentRole,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2563EB)
                            )
                        }
                        DropdownMenu(
                            expanded = expandedRole,
                            onDismissRequest = { expandedRole = false }
                        ) {
                            listOf("Citizen", "Government Officer", "Department Admin", "Hospital Admin", "Super Admin").forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role, fontSize = 13.sp) },
                                    onClick = {
                                        viewModel.selectRole(role)
                                        if (role.contains("Admin") || role.contains("Officer")) {
                                            viewModel.selectTab("Admin")
                                        } else {
                                            viewModel.selectTab("Home")
                                        }
                                        expandedRole = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// BOTTOM NAVIGATION BAR
// ==========================================
@Composable
fun QuettaBottomNavigationBar(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    currentLang: String,
    currentRole: String = "Citizen"
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.drawBehind {
            // Top border
            drawLine(
                color = Color(0xFFF1F5F9),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 2f
            )
        }
    ) {
        val navItems = buildList {
            add(Triple("Home", Icons.Default.Home, "Home"))
            add(Triple("Complaints", Icons.Default.ReportProblem, "complaints"))
            add(Triple("Map", Icons.Default.Map, "map"))
            add(Triple("AI Assistant", Icons.Default.SmartToy, "ai_assistant"))
            add(Triple("Services", Icons.Default.Apps, "services"))
            if (currentRole.contains("Admin") || currentRole.contains("Officer")) {
                add(Triple("Admin", Icons.Default.Settings, "admin"))
            }
        }

        navItems.forEach { (tabName, icon, localeKey) ->
            NavigationBarItem(
                selected = currentTab == tabName,
                onClick = { onTabSelected(tabName) },
                icon = { 
                    Icon(
                        imageVector = icon, 
                        contentDescription = tabName,
                        modifier = Modifier.size(22.dp)
                    ) 
                },
                label = {
                    Text(
                        text = QuettaLocale.get(localeKey, currentLang),
                        fontSize = 10.sp,
                        fontWeight = if (currentTab == tabName) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2563EB),
                    selectedTextColor = Color(0xFF2563EB),
                    indicatorColor = Color(0xFFEFF6FF), // extremely subtle blue active pill
                    unselectedIconColor = Color(0xFF94A3B8),
                    unselectedTextColor = Color(0xFF94A3B8)
                ),
                modifier = Modifier.testTag("nav_item_${tabName.lowercase().replace(" ", "_")}")
            )
        }
    }
}

// ==========================================
// ==========================================
// SCREEN 1: CITIZEN HOME SCREEN
// ==========================================
@Composable
fun HomeScreen(viewModel: QuettaViewModel, currentLang: String, onSosClicked: () -> Unit) {
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val complaints by viewModel.complaints.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB)) // Crisp minimal background
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Hero Banner with local Quetta greeting
        item {
            QuettaWelcomeHeroBanner(currentLang)
        }

        // Emergency SOS Section: Urgent Utility pulsing banner
        item {
            QuettaSosBanner(onClick = onSosClicked)
        }

        // Live Weather & Air Quality Status
        item {
            WeatherAndAirQualityCard(currentLang)
        }

        // Emergency alerts marquee/list if present
        val alertList = announcements.filter { it.isAlert }
        if (alertList.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFEF2F2))
                        .border(BorderStroke(1.dp, Color(0xFFFEE2E2)), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "⚠️ " + QuettaLocale.get("announcements", currentLang).uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF991B1B),
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    alertList.forEach { alert ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Alert",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = alert.title,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7F1D1D),
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = alert.content,
                                    color = Color(0xFF991B1B).copy(alpha = 0.9f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Civic Services Section
        item {
            Text(
                text = "CIVIC SERVICES",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
                color = Color(0xFF94A3B8), // slate-400
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Core Service Grid (6 items)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        HomeScreenServiceItem(
                            title = "Report Issues",
                            icon = Icons.Default.ReportProblem,
                            iconColor = Color(0xFF2563EB),
                            bgColor = Color(0xFFEFF6FF)
                        ) {
                            viewModel.selectTab("Complaints")
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        HomeScreenServiceItem(
                            title = "Hospitals",
                            icon = Icons.Default.LocalHospital,
                            iconColor = Color(0xFF8B5CF6),
                            bgColor = Color(0xFFF5F3FF)
                        ) {
                            viewModel.selectTab("Map")
                            viewModel.updateMapFilter("Hospitals")
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        HomeScreenServiceItem(
                            title = "Doc Portal",
                            icon = Icons.Default.Assignment,
                            iconColor = Color(0xFFF59E0B),
                            bgColor = Color(0xFFFFFBEB)
                        ) {
                            viewModel.selectTab("Services")
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        HomeScreenServiceItem(
                            title = "Tourism",
                            icon = Icons.Default.Landscape,
                            iconColor = Color(0xFF10B981),
                            bgColor = Color(0xFFECFDF5)
                        ) {
                            viewModel.selectTab("Services")
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        HomeScreenServiceItem(
                            title = "Job Hub",
                            icon = Icons.Default.Work,
                            iconColor = Color(0xFF6366F1),
                            bgColor = Color(0xFFEEF2FF)
                        ) {
                            viewModel.selectTab("Services")
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        HomeScreenAiGuideItem(
                            title = "AI Guide"
                        ) {
                            viewModel.selectTab("AI Assistant")
                        }
                    }
                }
            }
        }

        // Dynamic Feed: Official Highlight Update Banner
        item {
            QuettaHighlightBanner()
        }

        // Recent Public Complaints
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CIVIC DASHBOARD",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFF94A3B8) // slate-400
                )
                TextButton(
                    onClick = { viewModel.selectTab("Complaints") },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "View All (${complaints.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB)
                    )
                }
            }
        }

        // Stream of recent 2 complaints
        items(complaints.take(2)) { complaint ->
            ComplaintListItem(complaint = complaint, onStatusClick = {})
        }

        // General News & announcements
        item {
            Text(
                text = "CITY ANNOUNCEMENTS",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
                color = Color(0xFF94A3B8),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        val generalAnnouncements = announcements.filter { !it.isAlert }
        items(generalAnnouncements) { ann ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ann.type.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2563EB),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                        val dateString = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(ann.timestamp))
                        Text(
                            text = dateString,
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = ann.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ann.content,
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuettaWelcomeHeroBanner(currentLang: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = QuettaLocale.get("welcome", currentLang).uppercase(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                letterSpacing = 0.5.sp,
                color = Color(0xFF2563EB) // Royal Blue 600
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Welcome back to Quetta's premier digital gateway. Real-time city tracking, emergency responses, and civic services, transparently managed.",
                fontSize = 12.sp,
                color = Color(0xFF475569), // Slate 600
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun QuettaSosBanner(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
        border = BorderStroke(1.dp, Color(0xFFFEE2E2)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFDC2626), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "SOS",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Emergency SOS",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color(0xFF7F1D1D)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Immediate police & rescue response",
                    fontSize = 11.sp,
                    color = Color(0xFF991B1B).copy(alpha = 0.8f)
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFF87171),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun WeatherAndAirQualityCard(currentLang: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Weather Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFF1F5F9)) // border-slate-100
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp)), // bg-amber-100
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Weather",
                        tint = Color(0xFFD97706), // text-amber-600
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = QuettaLocale.get("weather", currentLang).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8) // text-slate-400
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "24°C Clear",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A) // text-slate-900
                    )
                }
            }
        }

        // Air Quality Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFF1F5F9)) // border-slate-100
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFD1FAE5), RoundedCornerShape(8.dp)), // bg-emerald-100
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = "AQI",
                        tint = Color(0xFF059669), // text-emerald-600
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = QuettaLocale.get("air_quality", currentLang).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8) // text-slate-400
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "42 Good",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A) // text-slate-900
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreenServiceItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)), // border-slate-100
        modifier = Modifier.height(105.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(bgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center,
                lineHeight = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HomeScreenAiGuideItem(
    title: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2563EB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.height(105.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun QuettaHighlightBanner() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF1E3A8A).copy(alpha = 0.6f), Color.Transparent),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, 0f)
                    )
                    drawRect(brush)
                }
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Official Update",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2563EB))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
                Text(
                    text = "New Metro Green Line expansion announced for Quetta Cantonment area.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White,
                    lineHeight = 17.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Posted 2h ago • Ministry of Transport",
                    fontSize = 10.sp,
                    color = Color(0xFF93C5FD)
                )
            }
        }
    }
}

// ==========================================
// SCREEN 2: COMPLAINTS REGISTER / REPORTING
// ==========================================
@Composable
fun ComplaintsScreen(viewModel: QuettaViewModel, currentLang: String) {
    val complaints by viewModel.complaints.collectAsStateWithLifecycle()
    val isCategorizing by viewModel.isCategorizing.collectAsStateWithLifecycle()

    var showReportDialog by remember { mutableStateOf(false) }

    var titleInput by remember { mutableStateOf("") }
    var descInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Road Damage") }
    var selectedDept by remember { mutableStateOf("Quetta Metropolitan Corporation (QMC)") }
    var isEmergencyChecked by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showReportDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("report_new_complaint_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Complaint")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = QuettaLocale.get("active_complaints", currentLang),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Citizens-powered urban tracking board. Transparently monitored by government agencies.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (complaints.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No complaints filed yet.", color = MaterialTheme.colorScheme.secondary)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(complaints) { complaint ->
                        ComplaintListItem(
                            complaint = complaint,
                            onStatusClick = {
                                // Double click to cycle status if admin/moderator roles for interactive showcase
                                val nextStatus = when (complaint.status) {
                                    "Pending" -> "Assigned"
                                    "Assigned" -> "In Progress"
                                    "In Progress" -> "Resolved"
                                    else -> "Pending"
                                }
                                viewModel.updateComplaintStatus(complaint.id, nextStatus)
                                Toast.makeText(context, "Status updated to $nextStatus!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }

    // Report Complaint Dialog with AI Auto-Categorize feature
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text(
                    text = QuettaLocale.get("report_complaint", currentLang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Complaint Title (e.g., Broken pipe)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = descInput,
                        onValueChange = { descInput = it },
                        label = { Text(QuettaLocale.get("desc", currentLang)) },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // AI Auto-Categorizer Button
                    Button(
                        onClick = {
                            if (descInput.isBlank()) {
                                Toast.makeText(context, "Enter a description first so Saeen AI can read it!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.analyzeComplaintWithAI(descInput) { cat, dept, isEmerg ->
                                selectedCategory = cat
                                selectedDept = dept
                                isEmergencyChecked = isEmerg
                                Toast.makeText(context, "Saeen AI Auto-Categorized as: $cat!", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer, contentColor = MaterialTheme.colorScheme.onTertiaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isCategorizing) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.primary)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(QuettaLocale.get("auto_cat", currentLang))
                        }
                    }

                    // Display resolved fields
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Category:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            Text(selectedCategory, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Assigned Department:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            Text(selectedDept, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isEmergencyChecked,
                            onCheckedChange = { isEmergencyChecked = it }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("High Emergency (Requires urgent dispatch)", fontSize = 13.sp, color = Color.Red, fontWeight = FontWeight.SemiBold)
                    }

                    // Simulated Geolocation Coordinates
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("GPS: 30.1895° N, 66.9922° E (Sariab Road, Quetta)", fontSize = 11.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isBlank() || descInput.isBlank()) {
                            Toast.makeText(context, "Fill in title and description!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.submitComplaint(
                            title = titleInput,
                            description = descInput,
                            category = selectedCategory,
                            dept = selectedDept,
                            lat = 30.1895,
                            lng = 66.9922,
                            citizen = "Quetta Citizen",
                            onSuccess = {
                                Toast.makeText(context, "Complaint filed successfully in local ledger!", Toast.LENGTH_SHORT).show()
                                showReportDialog = false
                                titleInput = ""
                                descInput = ""
                            }
                        )
                    }
                ) {
                    Text(QuettaLocale.get("submit", currentLang))
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ComplaintListItem(complaint: Complaint, onStatusClick: () -> Unit) {
    val statusColor = when (complaint.status) {
        "Pending" -> Color(0xFFEF4444)
        "Assigned" -> Color(0xFFF59E0B)
        "In Progress" -> Color(0xFF3B82F6)
        else -> Color(0xFF10B981)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStatusClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(statusColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = complaint.status.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = statusColor
                    )
                }
                val timeString = SimpleDateFormat("h:mm a, d MMM", Locale.getDefault()).format(Date(complaint.timestamp))
                Text(timeString, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(complaint.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                complaint.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Department", fontSize = 9.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Text(complaint.assigneeDept, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(
                    text = complaint.category,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            if (!complaint.citizenComments.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💬 \"${complaint.citizenComments}\"",
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(6.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ==========================================
// SCREEN 3: INTERACTIVE MAP & PIN FILTER
// ==========================================
@Composable
fun MapScreen(viewModel: QuettaViewModel, currentLang: String) {
    val context = LocalContext.current
    val mapFilter by viewModel.mapFilter.collectAsStateWithLifecycle()
    var selectedMarkerName by remember { mutableStateOf<String?>(null) }
    var selectedMarkerDesc by remember { mutableStateOf<String?>(null) }

    // Mock markers of Quetta
    val markers = listOf(
        MapMarker("Hanna Lake", "Hospitals", 30.2212, 67.1123, "Scenic lake near Quetta, highly visited in summers."),
        MapMarker("Askari Park", "Tourist", 30.2012, 67.0123, "Beautiful family park featuring dynamic rides & cultural festivals."),
        MapMarker("Quetta Civil Hospital", "Hospitals", 30.1912, 67.0012, "Primary medical facility with 24/7 Emergency trauma center."),
        MapMarker("University of Balochistan", "Schools", 30.1582, 66.9931, "The premier seat of higher learning in Balochistan."),
        MapMarker("Police Cantt Station", "Police", 30.2104, 66.9854, "Law enforcement and emergency quick response wing."),
        MapMarker("Quetta Central Masjid", "Mosques", 30.1982, 67.0054, "Historic mosque at the junction of Liaquat Bazaar.")
    )

    val filteredMarkers = if (mapFilter == "All") markers else markers.filter { it.category == mapFilter }

    Column(modifier = Modifier.fillMaxSize()) {
        // Map Top Controls
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categories = listOf("All", "Hospitals", "Police", "Tourist", "Schools", "Mosques")
            items(categories) { cat ->
                val isSelected = mapFilter == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.updateMapFilter(cat) },
                    label = { Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        // The Vector-Drawn Canvas Map (Highly creative smart map representation)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFE2E8F0))
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw Bolan & Chiltan Mountains around the valley
                val mountainBrush = Brush.verticalGradient(listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8)))
                drawPath(
                    path = Path().apply {
                        moveTo(0f, h * 0.3f)
                        lineTo(w * 0.3f, h * 0.1f)
                        lineTo(w * 0.6f, h * 0.45f)
                        lineTo(w * 0.8f, h * 0.2f)
                        lineTo(w, h * 0.5f)
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    },
                    brush = mountainBrush,
                    alpha = 0.4f
                )

                // Draw roads of Quetta (Jinnah Road, Sariab Road, Airport Road)
                val roadColor = Color.White
                // Jinnah Road (Diagonal center)
                drawLine(roadColor, Offset(0f, h * 0.6f), Offset(w, h * 0.3f), strokeWidth = 14f)
                // Sariab Road
                drawLine(roadColor, Offset(w * 0.2f, 0f), Offset(w * 0.8f, h), strokeWidth = 12f)
                // Airport Road
                drawLine(roadColor, Offset(0f, h * 0.2f), Offset(w, h * 0.8f), strokeWidth = 10f)

                // Label Roads
                // (Text would require paint object, keeping it neat and geometric)
            }

            // Overlay markers as clickable composable elements
            filteredMarkers.forEach { marker ->
                // Calculate position relative to container
                val xPercent = ((marker.longitude - 66.90) / 0.30).coerceIn(0.1, 0.9).toFloat()
                val yPercent = ((marker.latitude - 30.10) / 0.15).coerceIn(0.1, 0.9).toFloat()

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val pxX = maxWidth * xPercent
                    val pxY = maxHeight * (1f - yPercent) // Invert Y for maps

                    Box(
                        modifier = Modifier
                            .offset(x = pxX - 18.dp, y = pxY - 18.dp)
                            .size(36.dp)
                            .background(
                                color = when (marker.category) {
                                    "Hospitals" -> Color(0xFFEF4444)
                                    "Police" -> Color(0xFF3B82F6)
                                    "Tourist" -> Color(0xFFF59E0B)
                                    else -> Color(0xFF10B981)
                                },
                                shape = CircleShape
                            )
                            .border(BorderStroke(2.dp, Color.White), CircleShape)
                            .clickable {
                                selectedMarkerName = marker.name
                                selectedMarkerDesc = marker.description
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (marker.category) {
                                "Hospitals" -> Icons.Default.LocalHospital
                                "Police" -> Icons.Default.LocalPolice
                                "Tourist" -> Icons.Default.CardTravel
                                "Schools" -> Icons.Default.School
                                else -> Icons.Default.PinDrop
                            },
                            contentDescription = marker.name,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Bottom popup when clicking a marker
            selectedMarkerName?.let { name ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = { selectedMarkerName = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                        Text(selectedMarkerDesc ?: "", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Calculating quickest route through Jinnah Road...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Navigate")
                            }
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Connecting to local service desk...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call Dept")
                            }
                        }
                    }
                }
            }
        }
    }
}

data class MapMarker(
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
)

// ==========================================
// SCREEN 4: CHATBOT / AI CITY ASSISTANT
// ==========================================
@Composable
fun AiChatScreen(viewModel: QuettaViewModel, currentLang: String) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()

    var chatInput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Saeen AI City Guide",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Ask questions, renew CNIC, review resumes, or find the best tea.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = { viewModel.clearChat() }) {
                Icon(Icons.Default.DeleteSweep, contentDescription = "Clear Chat", tint = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat messages bubble list
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            messages.forEach { (sender, text) ->
                val isModel = sender == "model"
                val alignment = if (isModel) Alignment.Start else Alignment.End
                val containerColor = if (isModel) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.primary
                }
                val textColor = if (isModel) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    Color.White
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(alignment)
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isModel) 2.dp else 16.dp,
                                bottomEnd = if (isModel) 16.dp else 2.dp
                            )
                        )
                        .background(containerColor)
                        .padding(12.dp)
                        .widthIn(max = 280.dp)
                ) {
                    Text(text = text, fontSize = 13.sp, color = textColor, lineHeight = 18.sp)
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start)
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Saeen AI is brewing response...", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }

        // Suggestions row
        Text("Try asking:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suggestions = listOf(
                "Best Sajji in Quetta?",
                "How to get a domicile?",
                "Is it freezing today?",
                "Sariab Road issue"
            )
            items(suggestions) { suggestion ->
                Card(
                    modifier = Modifier.clickable { viewModel.sendMessage(suggestion) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f))
                ) {
                    Text(
                        suggestion,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // TextInput row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = chatInput,
                onValueChange = { chatInput = it },
                placeholder = { Text("Ask Saeen AI...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_chat_input"),
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (chatInput.isNotBlank()) {
                                viewModel.sendMessage(chatInput)
                                chatInput = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            )
        }
    }
}

// ==========================================
// SCREEN 5: EXTRA SERVICES & CITIZENS GUIDES
// ==========================================
@Composable
fun ServicesScreen(viewModel: QuettaViewModel, currentLang: String) {
    val bloodDonors by viewModel.bloodDonors.collectAsStateWithLifecycle()
    var selectedServiceTab by remember { mutableStateOf("Blood") } // "Blood", "Jobs", "Documents", "Tourism"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Civic Services Portal",
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Tabs row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val serviceTabs = listOf("Blood", "Jobs", "Documents", "Tourism")
            serviceTabs.forEach { tab ->
                val isSelected = selectedServiceTab == tab
                Button(
                    onClick = { selectedServiceTab = tab },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(tab, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Panel contents
        Box(modifier = Modifier.weight(1f)) {
            when (selectedServiceTab) {
                "Blood" -> BloodDonationSection(viewModel, bloodDonors)
                "Jobs" -> JobPortalSection(viewModel)
                "Documents" -> DocumentsSection()
                "Tourism" -> TourismSection()
            }
        }
    }
}

@Composable
fun BloodDonationSection(viewModel: QuettaViewModel, donors: List<BloodDonor>) {
    var showRegisterDonor by remember { mutableStateOf(false) }

    var dName by remember { mutableStateOf("") }
    var dBloodGroup by remember { mutableStateOf("O+") }
    var dContact by remember { mutableStateOf("") }
    var dArea by remember { mutableStateOf("Jinnah Town") }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Emergency Blood Donors", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            TextButton(onClick = { showRegisterDonor = true }) {
                Text("+ Register Donor", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(donors) { donor ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFEF4444), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(donor.bloodGroup, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(donor.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Area: ${donor.area}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(
                            onClick = {
                                Toast.makeText(context, "Simulating call to donor: ${donor.contact}", Toast.LENGTH_LONG).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    if (showRegisterDonor) {
        AlertDialog(
            onDismissRequest = { showRegisterDonor = false },
            title = { Text("Register Blood Donor", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = dName, onValueChange = { dName = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = dContact, onValueChange = { dContact = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        var expandedBG by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f)) {
                            Button(onClick = { expandedBG = true }, modifier = Modifier.fillMaxWidth()) {
                                Text("Group: $dBloodGroup")
                            }
                            DropdownMenu(expanded = expandedBG, onDismissRequest = { expandedBG = false }) {
                                listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-").forEach { bg ->
                                    DropdownMenuItem(text = { Text(bg) }, onClick = { dBloodGroup = bg; expandedBG = false })
                                }
                            }
                        }

                        OutlinedTextField(value = dArea, onValueChange = { dArea = it }, label = { Text("Quetta Area") }, modifier = Modifier.weight(1f))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dName.isBlank() || dContact.isBlank()) {
                            Toast.makeText(context, "Fill in name and contact details!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.registerBloodDonor(dName, dBloodGroup, dContact, dArea) {
                            Toast.makeText(context, "You are registered as a life saver, Saeen!", Toast.LENGTH_LONG).show()
                            showRegisterDonor = false
                            dName = ""
                            dContact = ""
                        }
                    }
                ) {
                    Text("Register")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRegisterDonor = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun JobPortalSection(viewModel: QuettaViewModel) {
    var resumeText by remember { mutableStateOf("") }
    var isReviewingResume by remember { mutableStateOf(false) }
    var resumeFeedback by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🤖 AI Resume Feedback Tool", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onTertiaryContainer)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Paste your skills/resume below, Saeen! Our AI will analyze it against Baluchistan Government and private job markets.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = resumeText,
                        onValueChange = { resumeText = it },
                        placeholder = { Text("Paste resume/skills here...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            if (resumeText.isBlank()) {
                                Toast.makeText(context, "Please paste resume text first!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            isReviewingResume = true
                            coroutineScope.launch {
                                // Simulate high quality AI resume feedback using the fallback or general AI text block
                                val feedback = GeminiService.askAssistant("Review my resume and provide feedback for Quetta jobs. Resume details: \"$resumeText\"", emptyList())
                                resumeFeedback = feedback
                                isReviewingResume = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isReviewingResume) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                        } else {
                            Text("Analyze Resume")
                        }
                    }

                    resumeFeedback?.let { feedback ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                            Text(feedback, modifier = Modifier.padding(10.dp), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            Text("Latest Openings in Balochistan", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        val jobs = listOf(
            Triple("Balochistan Revenue Authority", "Admin Assistant (Grade 16)", "Location: Quetta (Govt) • Fee: Rs. 500"),
            Triple("UoB Quetta", "Lecturer in Computer Science", "Location: Sariab Road Campus • Exp: 2 Yrs"),
            Triple("Red Crescent Hospital", "ER Senior Nurse", "Location: Cantt Area • Shift: Night"),
            Triple("Balochistan IT Board (BITB)", "Full Stack Dev (Kotlin/React)", "Location: Quetta • Remote Friendly")
        )

        items(jobs) { job ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(job.first, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    Text(job.second, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(job.third, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            Toast.makeText(context, "Direct application submitted via Quetta Connect integration!", Toast.LENGTH_SHORT).show()
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Apply Online", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentsSection() {
    val docs = listOf(
        DocumentInfo(
            "Domicile Certificate",
            "Proof of residence in Balochistan.",
            "Documents: CNIC/B-Form, Father's CNIC, Matric Certificate, 4 Photos.",
            "Fee: Rs. 150",
            "Processing: 3 Working Days"
        ),
        DocumentInfo(
            "CNIC / Smart Card",
            "National Identity Card issued by NADRA.",
            "Documents: Parents' CNICs, Birth Certificate, Matric Certificate.",
            "Fee: Rs. 400 (Normal), Rs. 1500 (Executive)",
            "Processing: 15 Days (Normal), 2 Days (Executive)"
        ),
        DocumentInfo(
            "Driving License",
            "Issued by Quetta Traffic Police Office.",
            "Documents: CNIC Copy, Medical Fitness Form, Learner Permit.",
            "Fee: Rs. 1000",
            "Processing: On the same day of test passing"
        )
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
        items(docs) { doc ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(doc.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(doc.desc, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(doc.reqDocs, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(doc.fee, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                        Text(doc.time, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

data class DocumentInfo(
    val title: String,
    val desc: String,
    val reqDocs: String,
    val fee: String,
    val time: String
)

@Composable
fun TourismSection() {
    val places = listOf(
        Triple("Hanna Lake Resort", "The shining water jewel framed by the Chiltan Mountains. Excellent afternoon picnic point.", "Rating: ⭐ 4.6 (512 reviews)"),
        Triple("Urak Valley Orchards", "The orchard paradise of Balochistan, famous worldwide for sweet peaches, apples, and cherries.", "Rating: ⭐ 4.8 (340 reviews)"),
        Triple("Quetta Liaquat Bazaar", "Dynamic local market famous for handmade rugs, woolen apparel, and local design crafts.", "Rating: ⭐ 4.2 (190 reviews)"),
        Triple("Sajji Central Lehri", "The historical culinary center of authentic salted mutton Sajji, slow cooked to juicy perfection.", "Rating: ⭐ 4.9 (880 reviews)")
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
        items(places) { place ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(place.first, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(place.second, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(place.third, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

// ==========================================
// SCREEN 6: ADMIN CONTROL ROOM / ANALYTICS
// ==========================================
@Composable
fun AdminScreen(viewModel: QuettaViewModel, currentLang: String) {
    val complaints by viewModel.complaints.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Admin states
    var adminSubTab by remember { mutableStateOf("Analytics") }
    var expandedComplaintId by remember { mutableStateOf<Long?>(null) }

    // Broadcast Board form states
    var broadcastTitle by remember { mutableStateOf("") }
    var broadcastContent by remember { mutableStateOf("") }
    var broadcastType by remember { mutableStateOf("MUNICIPAL UPDATE") }
    var isUrgentAlert by remember { mutableStateOf(false) }

    val pendingCount = complaints.count { it.status == "Pending" }
    val progressCount = complaints.count { it.status == "In Progress" || it.status == "Assigned" }
    val resolvedCount = complaints.count { it.status == "Resolved" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB)) // Clean minimal background
    ) {
        // Stylish Sub-Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "ADMIN COMMAND CENTER",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
                color = Color(0xFF2563EB)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Supervise municipal resources, broadcast warnings, and resolve public complaints live.",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tab Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Analytics", "Complaints Manager", "Broadcast Board").forEach { subTab ->
                    val isSelected = adminSubTab == subTab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFFEFF6FF) else Color.Transparent)
                            .clickable { adminSubTab = subTab }
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color(0xFFBFDBFE) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = subTab,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF2563EB) else Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        // Sub-Tab Contents
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (adminSubTab) {
                "Analytics" -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Quick Stats Row
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Pending
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text("PENDING", fontWeight = FontWeight.Bold, fontSize = 9.sp, color = Color(0xFFDC2626), letterSpacing = 0.5.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("$pendingCount", fontWeight = FontWeight.Black, fontSize = 26.sp, color = Color(0xFF0F172A))
                                    }
                                }
                                // Active
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text("ACTIVE", fontWeight = FontWeight.Bold, fontSize = 9.sp, color = Color(0xFFD97706), letterSpacing = 0.5.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("$progressCount", fontWeight = FontWeight.Black, fontSize = 26.sp, color = Color(0xFF0F172A))
                                    }
                                }
                                // Resolved
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text("RESOLVED", fontWeight = FontWeight.Bold, fontSize = 9.sp, color = Color(0xFF059669), letterSpacing = 0.5.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("$resolvedCount", fontWeight = FontWeight.Black, fontSize = 26.sp, color = Color(0xFF0F172A))
                                    }
                                }
                            }
                        }

                        // Donut Resolution success Rate Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "Civic Resolution Success Rate",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val total = complaints.size.toFloat().coerceAtLeast(1f)
                                    val percent = (resolvedCount.toFloat() / total) * 100f

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(130.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Canvas(modifier = Modifier.size(110.dp)) {
                                            drawCircle(
                                                color = Color(0xFFF1F5F9),
                                                radius = size.minDimension / 2,
                                                style = Stroke(width = 14f)
                                            )
                                            drawArc(
                                                color = Color(0xFF10B981),
                                                startAngle = -90f,
                                                sweepAngle = (percent / 100f) * 360f,
                                                useCenter = false,
                                                style = Stroke(width = 14f)
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "${percent.toInt()}%",
                                                fontWeight = FontWeight.Black,
                                                fontSize = 20.sp,
                                                color = Color(0xFF059669)
                                            )
                                            Text(
                                                text = "Completed Tasks",
                                                fontSize = 9.sp,
                                                color = Color(0xFF94A3B8),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "High resolution rates keep citizens satisfied. WASA Quetta and QMC are coordinating resources actively.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B),
                                        lineHeight = 15.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        // Department Response Metrics Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "Department Response Metrics",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val depts = listOf(
                                        Pair("QMC Corporation", 92),
                                        Pair("WASA Quetta", 65),
                                        Pair("C&W Balochistan", 80),
                                        Pair("Traffic Police", 95)
                                    )

                                    depts.forEach { (name, score) ->
                                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = name,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF334155)
                                                )
                                                Text(
                                                    text = "$score% Compliance",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color(0xFF2563EB)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(8.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Color(0xFFF1F5F9))
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .fillMaxWidth(score / 100f)
                                                        .background(Color(0xFF2563EB))
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Export report toolbox card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "Generate Strategic Reports",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1E40AF)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Compile authenticated ledger metrics. Ideal for Balochistan assembly audits and budget reviews.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF2563EB),
                                        lineHeight = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Button(
                                            onClick = {
                                                Toast.makeText(context, "Exporting audit PDF ledger... Download started!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("PDF Ledger", fontSize = 11.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                Toast.makeText(context, "Exporting metrics Excel ledger... Download started!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2563EB)),
                                            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.GridOn, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Excel Sheet", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Complaints Manager" -> {
                    if (complaints.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.CheckBoxOutlineBlank,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No civic complaints registered yet",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = "Everything is perfectly peaceful in Quetta!",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(20.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Text(
                                    text = "ACTIVE PUBLIC TICKETS (${complaints.size})",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp,
                                    color = Color(0xFF64748B),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }

                            items(complaints) { complaint ->
                                val isExpanded = expandedComplaintId == complaint.id

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            expandedComplaintId = if (isExpanded) null else complaint.id
                                        },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isExpanded) Color(0xFFBFDBFE) else Color(0xFFF1F5F9)
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = complaint.category.uppercase(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFF2563EB),
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFFEFF6FF))
                                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                                            )

                                            // Status Badge
                                            val badgeBg = when (complaint.status) {
                                                "Pending" -> Color(0xFFFEF2F2)
                                                "In Progress", "Assigned" -> Color(0xFFFFFBEB)
                                                "Resolved" -> Color(0xFFECFDF5)
                                                else -> Color(0xFFF1F5F9)
                                            }
                                            val badgeText = when (complaint.status) {
                                                "Pending" -> Color(0xFFDC2626)
                                                "In Progress", "Assigned" -> Color(0xFFD97706)
                                                "Resolved" -> Color(0xFF059669)
                                                else -> Color(0xFF475569)
                                            }

                                            Text(
                                                text = complaint.status,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = badgeText,
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(badgeBg)
                                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = complaint.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF0F172A)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = complaint.description,
                                            fontSize = 12.sp,
                                            color = Color(0xFF475569),
                                            lineHeight = 16.sp
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "By: ${complaint.citizenName}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF94A3B8)
                                            )
                                            Text(
                                                text = "Dept: ${complaint.assigneeDept}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2563EB)
                                            )
                                        }

                                        // Expanded Interactive Quick Actions to change status
                                        if (isExpanded) {
                                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
                                            Text(
                                                text = "UPDATE TICKET STATUS",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 9.sp,
                                                letterSpacing = 1.sp,
                                                color = Color(0xFF64748B),
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                // Pending Chip button
                                                val isPend = complaint.status == "Pending"
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(if (isPend) Color(0xFFFEE2E2) else Color(0xFFF1F5F9))
                                                        .clickable {
                                                            viewModel.updateComplaintStatus(complaint.id, "Pending")
                                                            Toast.makeText(context, "Complaint status reverted to Pending", Toast.LENGTH_SHORT).show()
                                                        }
                                                        .border(
                                                            width = 1.dp,
                                                            color = if (isPend) Color(0xFFFCA5A5) else Color.Transparent,
                                                            shape = RoundedCornerShape(10.dp)
                                                        )
                                                        .padding(vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text("Pending", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isPend) Color(0xFFDC2626) else Color(0xFF475569))
                                                }

                                                // Active / In Progress Chip button
                                                val isActive = complaint.status == "In Progress" || complaint.status == "Assigned"
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(if (isActive) Color(0xFFFEF3C7) else Color(0xFFF1F5F9))
                                                        .clickable {
                                                            viewModel.updateComplaintStatus(complaint.id, "In Progress")
                                                            Toast.makeText(context, "Complaint assigned and marked Active", Toast.LENGTH_SHORT).show()
                                                        }
                                                        .border(
                                                            width = 1.dp,
                                                            color = if (isActive) Color(0xFFFCD34D) else Color.Transparent,
                                                            shape = RoundedCornerShape(10.dp)
                                                        )
                                                        .padding(vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text("Active", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isActive) Color(0xFFD97706) else Color(0xFF475569))
                                                }

                                                // Resolved Chip button
                                                val isRes = complaint.status == "Resolved"
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(if (isRes) Color(0xFFD1FAE5) else Color(0xFFF1F5F9))
                                                        .clickable {
                                                            viewModel.updateComplaintStatus(complaint.id, "Resolved")
                                                            Toast.makeText(context, "Complaint solved! Citizen notified.", Toast.LENGTH_SHORT).show()
                                                        }
                                                        .border(
                                                            width = 1.dp,
                                                            color = if (isRes) Color(0xFF6EE7B7) else Color.Transparent,
                                                            shape = RoundedCornerShape(10.dp)
                                                        )
                                                        .padding(vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text("Resolved", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isRes) Color(0xFF059669) else Color(0xFF475569))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Broadcast Board" -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Text(
                                text = "MUNICIPAL BROADCASTER BOARD",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "Compose New Broadcast",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Title Input
                                    Text("Broadcast Title", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = broadcastTitle,
                                        onValueChange = { broadcastTitle = it },
                                        placeholder = { Text("e.g., WASA Water Line Repair", fontSize = 13.sp) },
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF2563EB),
                                            unfocusedBorderColor = Color(0xFFE2E8F0)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Content Input
                                    Text("Message Content", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = broadcastContent,
                                        onValueChange = { broadcastContent = it },
                                        placeholder = { Text("Describe the update or warning in detail for citizens...", fontSize = 13.sp) },
                                        minLines = 3,
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF2563EB),
                                            unfocusedBorderColor = Color(0xFFE2E8F0)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Category chips
                                    Text("Broadcast Category", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    val categories = listOf("MUNICIPAL UPDATE", "TRAFFIC", "WEATHER ALERT", "WATER LINE", "HEALTH")
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(categories) { cat ->
                                            val isSelected = broadcastType == cat
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(if (isSelected) Color(0xFF2563EB) else Color(0xFFF1F5F9))
                                                    .clickable { broadcastType = cat }
                                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                            ) {
                                                Text(
                                                    text = cat,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color.White else Color(0xFF475569)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Urgent warning Red Toggle
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isUrgentAlert) Color(0xFFFEF2F2) else Color(0xFFF8FAFC))
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Mark as Urgent Red Alert",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = if (isUrgentAlert) Color(0xFF991B1B) else Color(0xFF334155)
                                            )
                                            Text(
                                                text = "Will display instantly on user home screens.",
                                                fontSize = 10.sp,
                                                color = if (isUrgentAlert) Color(0xFFDC2626).copy(alpha = 0.8f) else Color(0xFF64748B)
                                            )
                                        }
                                        Switch(
                                            checked = isUrgentAlert,
                                            onCheckedChange = { isUrgentAlert = it },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = Color(0xFFDC2626)
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Publish button
                                    Button(
                                        onClick = {
                                            if (broadcastTitle.isBlank() || broadcastContent.isBlank()) {
                                                Toast.makeText(context, "Please write both title and content first!", Toast.LENGTH_SHORT).show()
                                                return@Button
                                            }
                                            viewModel.publishAlert(
                                                title = broadcastTitle,
                                                content = broadcastContent,
                                                type = broadcastType,
                                                isAlert = isUrgentAlert
                                            )
                                            Toast.makeText(context, "Broadcast disseminated across Quetta!", Toast.LENGTH_LONG).show()

                                            // Reset inputs
                                            broadcastTitle = ""
                                            broadcastContent = ""
                                            isUrgentAlert = false
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = if (isUrgentAlert) Color(0xFFDC2626) else Color(0xFF2563EB)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = if (isUrgentAlert) Icons.Default.Warning else Icons.Default.Send,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isUrgentAlert) "PUBLISH URGENT RED ALERT" else "PUBLISH BROADCAST NEWS",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
