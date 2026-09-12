package com.example.data.service

import com.example.data.model.LegalCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * بخش ۷: پیش‌بینی رای - تحلیل ۱۰۰۰ دادنامه
 * ML شبیه‌سازی شده
 */
data class PredictionResult(
    val successProbability: Int, // 0-100
    val confidence: Float,
    val predictedOutcome: String,
    val keyFactors: List<String>,
    val similarCases: List<SimilarCase>,
    val recommendations: List<String>,
    val riskLevel: String // کم، متوسط، زیاد
)

data class SimilarCase(
    val caseNumber: String,
    val title: String,
    val outcome: String,
    val similarity: Int
)

object PredictionService {

    suspend fun predictCaseOutcome(legalCase: LegalCase): PredictionResult = withContext(Dispatchers.IO) {
        delay(1500) // شبیه‌سازی ML

        // منطق پیش‌بینی بر اساس نوع پرونده
        val (prob, outcome, factors) = when {
            legalCase.caseTitle.contains("چک") -> Triple(
                85,
                "محکومیت خوانده به پرداخت وجه چک + خسارت تاخیر تادیه",
                listOf("وجود گواهی عدم پرداخت", "اصل تجریدی بودن چک", "عدم اثبات پرداخت توسط خوانده")
            )
            legalCase.caseTitle.contains("خلع ید") -> Triple(
                70,
                "صدور حکم خلع ید در صورت احراز مالکیت",
                listOf("سند مالکیت رسمی", "تصرف عدوانی احراز شده", "نظر کارشناس ثبتی")
            )
            legalCase.caseTitle.contains("کلاهبرداری") -> Triple(
                60,
                "قرار جلب به دادرسی و سپس محکومیت در صورت احراز فریب",
                listOf("وجود مانور متقلبانه", "بردن مال دیگری", "شهادت شهود")
            )
            legalCase.caseTitle.contains("مهریه") -> Triple(
                90,
                "محکومیت زوج به پرداخت مهریه به نرخ روز",
                listOf("عقدنامه رسمی", "مهریه عندالمطالبه", "استطاعت مالی زوج")
            )
            else -> Triple(
                65,
                "نیاز به بررسی دقیق‌تر مدارک و دفاعیات",
                listOf("مدارک ناقص", "نیاز به کارشناسی", "دفاعیات طرف مقابل")
            )
        }

        val similarCases = listOf(
            SimilarCase("۱۴۰۲۹۱۰۰۰۱۲۳۴", "مطالبه وجه چک مشابه", "محکومیت خوانده", 92),
            SimilarCase("۱۴۰۲۹۱۰۰۰۵۶۷۸", "چک صیادی ۲ میلیاردی", "محکومیت + خسارت", 88),
            SimilarCase("۱۴۰۳۹۱۰۰۰۰۹۱۲", "مطالبه چک حقوقی", "سازش", 75)
        )

        val recommendations = when {
            prob > 80 -> listOf("مدارک شما قوی است، پیشنهاد پیگیری جدی", "درخواست تامین خواسته کنید", "لایحه دفاعیه قوی تنظیم کنید")
            prob > 60 -> listOf("تکمیل مدارک با شهادت شهود", "درخواست کارشناسی", "مذاکره برای سازش")
            else -> listOf("جمع‌آوری مستندات بیشتر", "مشاوره با وکیل متخصص", "بررسی راه‌های جایگزین")
        }

        val riskLevel = when {
            prob > 80 -> "کم"
            prob > 60 -> "متوسط"
            else -> "زیاد"
        }

        PredictionResult(
            successProbability = prob,
            confidence = 0.85f,
            predictedOutcome = outcome,
            keyFactors = factors,
            similarCases = similarCases,
            recommendations = recommendations,
            riskLevel = riskLevel
        )
    }

    fun getSuccessRateByType(caseType: String): Int {
        return when {
            caseType.contains("چک") -> 85
            caseType.contains("مهریه") -> 90
            caseType.contains("خلع ید") -> 70
            caseType.contains("کلاهبرداری") -> 60
            else -> 65
        }
    }
}
