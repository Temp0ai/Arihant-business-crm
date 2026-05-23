package com.teacoffeecrm.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.teacoffeecrm.data.database.Converters

enum class CampaignType {
    WHATSAPP_BULK, EMAIL, SOCIAL_MEDIA, GMB_POST
}

enum class CampaignStatus {
    DRAFT, SCHEDULED, RUNNING, COMPLETED, PAUSED, FAILED
}

@Entity(tableName = "campaigns")
@TypeConverters(Converters::class)
data class Campaign(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val type: CampaignType = CampaignType.WHATSAPP_BULK,
    val status: CampaignStatus = CampaignStatus.DRAFT,
    val targetCategories: List<ProductCategory> = emptyList(),
    val targetClientTypes: List<ClientType> = emptyList(),
    val message: String = "",
    val mediaUrl: String = "",
    val scheduledAt: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val sentCount: Int = 0,
    val deliveredCount: Int = 0,
    val openedCount: Int = 0,
    val respondedCount: Int = 0,
    val leadIds: List<Long> = emptyList()
)
