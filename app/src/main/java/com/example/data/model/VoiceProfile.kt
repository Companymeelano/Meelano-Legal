package com.example.data.model

data class VoiceProfile(
    val id: String = "default",
    val name: String = "صدای پیش‌فرض فارسی",
    val gender: String = "female", // female, male
    val speed: Float = 0.9f,
    val pitch: Float = 1.0f,
    val tone: String = "رسمی", // رسمی، دوستانه، قاطع، آرام
    val language: String = "fa-IR"
)

object VoiceProfiles {
    val allProfiles = listOf(
        VoiceProfile("female_official", "خانم وکیل - رسمی", "female", 0.9f, 1.0f, "رسمی"),
        VoiceProfile("female_friendly", "خانم وکیل - دوستانه", "female", 1.0f, 1.1f, "دوستانه"),
        VoiceProfile("male_authoritative", "آقای وکیل - قاطع", "male", 0.85f, 0.9f, "قاطع"),
        VoiceProfile("male_calm", "آقای وکیل - آرام", "male", 0.8f, 0.95f, "آرام"),
        VoiceProfile("luxury_gold", "صدای لاکچری طلایی", "female", 0.85f, 1.05f, "لاکچری")
    )
}

data class SttResult(
    val text: String,
    val confidence: Float,
    val isFinal: Boolean,
    val language: String = "fa-IR"
)
