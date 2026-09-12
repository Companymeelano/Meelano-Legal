package com.example.data.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * بخش ۱۴: اسکنر هوشمند - OCR فارسی
 * ML Kit + Document AI شبیه‌سازی
 */
data class OcrResult(
    val fullText: String,
    val blocks: List<OcrBlock>,
    val confidence: Float,
    val detectedLanguage: String,
    val isLegalDocument: Boolean,
    val extractedEntities: ExtractedEntities
)

data class OcrBlock(
    val text: String,
    val boundingBox: String,
    val confidence: Float
)

data class ExtractedEntities(
    val caseNumbers: List<String>,
    val dates: List<String>,
    val amounts: List<String>,
    val names: List<String>,
    val legalArticles: List<String>
)

class OcrService(private val context: Context) {

    suspend fun scanPersianDocument(bitmap: Bitmap): OcrResult = withContext(Dispatchers.IO) {
        delay(2000) // شبیه‌سازی ML Kit

        // شبیه‌سازی OCR فارسی
        val simulatedText = """
            بسمه تعالی
            دادخواست مطالبه وجه
            خواهان: شرکت بازرگانی میلانو نوین
            خوانده: شرکت ساختمانی فراز گستر
            خواسته: مطالبه وجه ۳ فقره چک به مبلغ ۳۵/۰۰۰/۰۰۰/۰۰۰ ریال
            شماره پرونده: ۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳
            تاریخ: ۱۴۰۳/۰۶/۱۵
            مستندات: مواد ۳۱۰ و ۳۱۱ قانون تجارت
        """.trimIndent()

        val entities = ExtractedEntities(
            caseNumbers = listOf("۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳"),
            dates = listOf("۱۴۰۳/۰۶/۱۵"),
            amounts = listOf("۳۵/۰۰۰/۰۰۰/۰۰۰ ریال"),
            names = listOf("شرکت بازرگانی میلانو نوین", "شرکت ساختمانی فراز گستر"),
            legalArticles = listOf("ماده ۳۱۰", "ماده ۳۱۱")
        )

        OcrResult(
            fullText = simulatedText,
            blocks = listOf(OcrBlock(simulatedText, "0,0,100,100", 0.92f)),
            confidence = 0.92f,
            detectedLanguage = "fa",
            isLegalDocument = true,
            extractedEntities = entities
        )
    }

    suspend fun scanFromUri(uri: Uri): OcrResult = withContext(Dispatchers.IO) {
        delay(1500)
        scanPersianDocument(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
    }

    fun isPersianText(text: String): Boolean {
        return text.any { it in 'آ'..'ی' || it in '۰'..'۹' }
    }
}
