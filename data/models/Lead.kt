package com.teacoffeecrm.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.teacoffeecrm.data.database.Converters

enum class LeadStatus {
    NEW, CONTACTED, INTERESTED, NEGOTIATING, CONVERTED, LOST
}

enum class ProductCategory {
    TEA_PREMIX,
    COFFEE_PREMIX,
    NESCAFE_PREMIX,
    TEA_MACHINE,
    COFFEE_MACHINE,
    NESCAFE_MACHINE,
    COMBO_PACK,
    OTHER
}

enum class ClientType {
    SOCIETY, CAFE, RESTAURANT, OFFICE,
    TEA_STALL, CAFETERIA, HOTEL, HOSPITAL,
    CORPORATE, MANUFACTURER, OTHER
}

@Entity(tableName = "leads")
@TypeConverters(Converters::class)
data class Lead(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val contactNumber: String = "",
    val email: String = "",
    val productCategories: List<ProductCategory> = emptyList(),
    val clientType: ClientType = ClientType.OTHER,
    val status: LeadStatus = LeadStatus.NEW,
    val inquiryDetails: String = "",
    val orderValue: Double = 0.0,
    val location: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "India",
    val sourceEmail: String = "",
    val notes: String = "",
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val lastContactedAt: Long = 0L,
    val nextFollowUpAt: Long = 0L,
    val isWhatsAppSent: Boolean = false,
    val isEmailSent: Boolean = false,
    val priority: Int = 2,
    val aiScore: Float = 0f,
    val gmailMessageId: String = ""
)
