package com.teacoffeecrm.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ContentType {
    IMAGE, VIDEO_SCRIPT, POST_CAPTION,
    HASHTAGS, FOLLOW_UP_MESSAGE, EMAIL_TEMPLATE
}

enum class VideoType {
    INSTAGRAM_REEL, YOUTUBE_SHORT,
    WHATSAPP_STATUS, PRODUCT_DEMO
}

@Entity(tableName = "generated_content")
data class GeneratedContent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: ContentType = ContentType.IMAGE,
    val prompt: String = "",
    val content: String = "",
    val localPath: String = "",
    val platform: String = "",
    val productCategory: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isUsed: Boolean = false
)
