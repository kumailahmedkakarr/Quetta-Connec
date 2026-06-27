package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val database: AppDatabase) {
    val complaints: Flow<List<Complaint>> = database.complaintDao().getAllComplaints()
    val bloodDonors: Flow<List<BloodDonor>> = database.bloodDonorDao().getAllDonors()
    val announcements: Flow<List<Announcement>> = database.announcementDao().getAllAnnouncements()

    suspend fun getComplaintById(id: Long): Complaint? = withContext(Dispatchers.IO) {
        database.complaintDao().getComplaintById(id)
    }

    suspend fun insertComplaint(complaint: Complaint): Long = withContext(Dispatchers.IO) {
        database.complaintDao().insertComplaint(complaint)
    }

    suspend fun updateComplaint(complaint: Complaint) = withContext(Dispatchers.IO) {
        database.complaintDao().updateComplaint(complaint)
    }

    suspend fun deleteComplaintById(id: Long) = withContext(Dispatchers.IO) {
        database.complaintDao().deleteComplaintById(id)
    }

    suspend fun insertDonor(donor: BloodDonor): Long = withContext(Dispatchers.IO) {
        database.bloodDonorDao().insertDonor(donor)
    }

    suspend fun deleteDonor(donor: BloodDonor) = withContext(Dispatchers.IO) {
        database.bloodDonorDao().deleteDonor(donor)
    }

    suspend fun insertAnnouncement(announcement: Announcement): Long = withContext(Dispatchers.IO) {
        database.announcementDao().insertAnnouncement(announcement)
    }

    suspend fun deleteAnnouncementById(id: Long) = withContext(Dispatchers.IO) {
        database.announcementDao().deleteAnnouncementById(id)
    }

    // Prepopulate database with realistic Quetta mock data if empty
    suspend fun prepopulateIfEmpty() = withContext(Dispatchers.IO) {
        val currentAnnouncements = database.announcementDao().getAllAnnouncements().first()
        if (currentAnnouncements.isEmpty()) {
            // Seed announcements
            database.announcementDao().insertAnnouncement(
                Announcement(
                    title = "Severe Dust Storm Warning",
                    content = "PDMA Balochistan has issued a high alert. Extreme wind and dust storms are moving from Chaman towards Quetta. Keep windows closed and stay indoors.",
                    type = "EMERGENCY",
                    isAlert = true
                )
            )
            database.announcementDao().insertAnnouncement(
                Announcement(
                    title = "Severe Cold Wave",
                    content = "Temperatures are expected to drop to -5°C tonight. Gas pressure may fluctuate. Please use dry heaters with caution.",
                    type = "WEATHER",
                    isAlert = false
                )
            )
            database.announcementDao().insertAnnouncement(
                Announcement(
                    title = "Brewery Road Water Maintenance",
                    content = "WASA Quetta will carry out pipeline repairs on Brewery Road. Water supply in adjacent neighborhoods will be suspended from 10:00 AM to 6:00 PM.",
                    type = "TRAFFIC",
                    isAlert = false
                )
            )
            database.announcementDao().insertAnnouncement(
                Announcement(
                    title = "Balochistan Cultural Festival 2026",
                    content = "A celebration of Balochi, Pashto, and Brahui heritage at Askari Park Quetta. Craft stalls, local music, Sajji competition, and folk dance.",
                    type = "GENERAL",
                    isAlert = false
                )
            )

            // Seed complaints
            database.complaintDao().insertComplaint(
                Complaint(
                    title = "Massive Garbage Dump on Sariab Road",
                    description = "Garbage has accumulated on the main divider, causing heavy odor and blocking pedestrian pathways. Urgent cleaning required near Balochistan University.",
                    category = "Garbage/Waste",
                    status = "Pending",
                    latitude = 30.1585,
                    longitude = 66.9934,
                    citizenName = "Sarfraz Mengal",
                    assigneeDept = "Quetta Metropolitan Corporation (QMC)",
                    citizenComments = "Saeen! The smell is reaching classrooms. Even our university's local stray cats are holding their noses!"
                )
            )
            database.complaintDao().insertComplaint(
                Complaint(
                    title = "Broken Water Valve on Jinnah Road",
                    description = "Clean drinking water is being wasted on the main highway due to a broken distribution valve of WASA. Huge puddle formed.",
                    category = "Water Leakage",
                    status = "Assigned",
                    latitude = 30.1982,
                    longitude = 67.0112,
                    citizenName = "Jahanzeb Alizai",
                    assigneeDept = "WASA Quetta",
                    citizenComments = "It is drinking water wasting on the road, while we are crying for water inside our homes! Kindly fix this ASAP."
                )
            )
            database.complaintDao().insertComplaint(
                Complaint(
                    title = "Broken Streetlights on Airport Road",
                    description = "The entire stretch near the entry gate is pitch black at night, making it highly unsafe and prone to minor accidents.",
                    category = "Street Lights",
                    status = "In Progress",
                    latitude = 30.2241,
                    longitude = 66.9654,
                    citizenName = "Mir Gul Khan",
                    assigneeDept = "QMC Streetlights Wing",
                    citizenComments = "Warrants immediate focus. Electrician team was seen inspecting yesterday, hope it gets completed."
                )
            )
            database.complaintDao().insertComplaint(
                Complaint(
                    title = "Pothole Pit on Spinny Road",
                    description = "A large deep hole has developed right in the middle of the turn. Highly dangerous for motorbikes.",
                    category = "Road Damage",
                    status = "Resolved",
                    latitude = 30.2052,
                    longitude = 66.9890,
                    citizenName = "Kamran Kakar",
                    assigneeDept = "Balochistan C&W Department",
                    citizenComments = "Thanks to the fast team, they filled it up with concrete today. Excellent speed!"
                )
            )

            // Seed Blood Donors
            database.bloodDonorDao().insertDonor(
                BloodDonor(
                    name = "Asif Baloch",
                    bloodGroup = "O+",
                    contact = "+92 333 7894561",
                    area = "Sariab Road",
                    isAvailable = true
                )
            )
            database.bloodDonorDao().insertDonor(
                BloodDonor(
                    name = "Zeenat Brahui",
                    bloodGroup = "AB-",
                    contact = "+92 345 1122334",
                    area = "Satellite Town",
                    isAvailable = true
                )
            )
            database.bloodDonorDao().insertDonor(
                BloodDonor(
                    name = "Wali Kakar",
                    bloodGroup = "B+",
                    contact = "+92 312 9988776",
                    area = "Jinnah Town",
                    isAvailable = true
                )
            )
            database.bloodDonorDao().insertDonor(
                BloodDonor(
                    name = "Dr. Shakoor Ahmed",
                    bloodGroup = "O-",
                    contact = "+92 300 5544332",
                    area = "Cantt Area",
                    isAvailable = false
                )
            )
            database.bloodDonorDao().insertDonor(
                BloodDonor(
                    name = "Gul Bashra",
                    bloodGroup = "A+",
                    contact = "+92 321 4455667",
                    area = "Brewery Road",
                    isAvailable = true
                )
            )
        }
    }
}
