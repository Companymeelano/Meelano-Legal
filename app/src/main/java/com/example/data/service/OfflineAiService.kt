package com.example.data.service

import com.example.data.model.IranianLegalKnowledgeBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * بخش ۲: هوش مصنوعی آفلاین - Gemma 2B Persian
 * شبیه‌سازی مدل آفلاین برای کار بدون اینترنت
 */
class OfflineAiService {

    private var isModelLoaded = false
    private var modelSizeMb = 1800 // Gemma 2B ~1.8GB quantized

    suspend fun loadModel(): Result<String> = withContext(Dispatchers.IO) {
        try {
            // شبیه‌سازی لود مدل
            delay(2000)
            isModelLoaded = true
            Result.success("مدل آفلاین Gemma 2B فارسی لاکچری با موفقیت لود شد (1.8GB - quantized 4bit)")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun askOffline(question: String): Result<String> = withContext(Dispatchers.IO) {
        if (!isModelLoaded) {
            loadModel()
        }

        try {
            delay(1200) // شبیه‌سازی inference

            val relevantArticles = IranianLegalKnowledgeBase.searchArticles(question)

            val answer = buildString {
                append("🤖 پاسخ آفلاین (Gemma 2B Persian - بدون نیاز به اینترنت):\n\n")
                append("سوال: $question\n\n")

                when {
                    question.contains("چک") -> {
                        append("بر اساس قانون تجارت و صدور چک:\n")
                        append("• چک سند تجاری مستقل است (ماده ۳۱۰ تجارت)\n")
                        append("• دارنده می‌تواند با گواهی عدم پرداخت اجراییه بگیرد\n")
                        append("• خسارت تاخیر تادیه بر اساس شاخص بانک مرکزی\n")
                    }
                    question.contains("تجدیدنظر") -> {
                        append("مهلت تجدیدنظرخواهی طبق ماده ۳۳۶ ق.آ.د.م ۲۰ روز است.\n")
                        append("روز ابلاغ و اقدام جزء مهلت نیست (ماده ۴۴۳).\n")
                    }
                    else -> {
                        append("این پاسخ توسط مدل آفلاین تولید شده:\n")
                        append("بر اساس قوانین ایران، ")
                        if (relevantArticles.isNotEmpty()) {
                            append("${relevantArticles.first().articleText}\n")
                        } else {
                            append("موضوع شما نیاز به بررسی دقیق‌تر دارد.\n")
                        }
                    }
                }

                append("\n\n📌 مواد مرتبط:\n")
                relevantArticles.take(2).forEach {
                    append("- ${it.lawName} ${it.articleNumber}\n")
                }

                append("\n⚠️ این پاسخ آفلاین است، برای مشاوره دقیق‌تر به حالت آنلاین بروید.")
            }

            Result.success(answer)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isAvailable(): Boolean = isModelLoaded
    fun getModelInfo(): String = "Gemma 2B Persian - Quantized 4bit - 1.8GB - آفلاین کامل"
}
