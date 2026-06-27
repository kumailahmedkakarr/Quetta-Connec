package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// ==========================================
// ENTITIES
// ==========================================

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // e.g. "Road Damage", "Garbage/Waste", "Water Leakage", "Electricity", "Street Lights"
    val status: String, // "Pending", "Assigned", "In Progress", "Resolved"
    val imageUri: String? = null,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val citizenName: String,
    val assigneeDept: String,
    val citizenComments: String? = null,
    val isAIResolved: Boolean = false
)

@Entity(tableName = "blood_donors")
data class BloodDonor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val bloodGroup: String, // "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"
    val contact: String,
    val area: String, // e.g., "Sariab Road", "Jinnah Town", "Cantt", "Satellite Town"
    val isAvailable: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val type: String, // "EMERGENCY", "WEATHER", "TRAFFIC", "EDUCATION", "GENERAL"
    val timestamp: Long = System.currentTimeMillis(),
    val isAlert: Boolean = false
)

// ==========================================
// DAOS
// ==========================================

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
    fun getAllComplaints(): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE id = :id")
    suspend fun getComplaintById(id: Long): Complaint?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: Complaint): Long

    @Update
    suspend fun updateComplaint(complaint: Complaint)

    @Query("DELETE FROM complaints WHERE id = :id")
    suspend fun deleteComplaintById(id: Long)
}

@Dao
interface BloodDonorDao {
    @Query("SELECT * FROM blood_donors ORDER BY timestamp DESC")
    fun getAllDonors(): Flow<List<BloodDonor>>

    @Query("SELECT * FROM blood_donors WHERE bloodGroup = :bloodGroup ORDER BY timestamp DESC")
    fun getDonorsByGroup(bloodGroup: String): Flow<List<BloodDonor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonor(donor: BloodDonor): Long

    @Delete
    suspend fun deleteDonor(donor: BloodDonor)
}

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM announcements ORDER BY timestamp DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement): Long

    @Query("DELETE FROM announcements WHERE id = :id")
    suspend fun deleteAnnouncementById(id: Long)
}
