package com.example.data.service

import com.example.data.model.IranianLegalKnowledgeBase
import com.example.data.model.LegalArticle
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Meelano Legal AI Service - Luxury Edition
 * Provides Persian legal consultation using Gemini
 * All responses are based on Iranian official laws
 */
class GeminiLegalService {

    private val legalSystemPrompt = """
        تو دستیار تخصصی حقوقی میلانو لگال هستی - یک وکیل پایه یک دادگستری با 20 سال سابقه در حقوق ایران.
        
        وظایف تو:
        1. پاسخ به سوالات حقوقی بر اساس قوانین رسمی جمهوری اسلامی ایران:
           - قانون مدنی
           - قانون آیین دادرسی مدنی
           - قانون آیین دادرسی کیفری
           - قانون مجازات اسلامی
           - قانون تجارت
           - قانون صدور چک
           - قانون ثبت اسناد و املاک
        
        2. همیشه مستند به مواد قانونی پاسخ بده. مثلاً: "طبق ماده 336 قانون آیین دادرسی مدنی..."
        3. زبان پاسخ: فارسی رسمی و حقوقی، قابل فهم برای موکل
        4. اگر سوال خارج از حقوق ایران بود، بگو فقط در چارچوب قوانین ایران پاسخ می‌دهی
        5. در انتهای هر پاسخ، مواد مرتبط را لیست کن
        6. هشدار بده که این مشاوره جایگزین وکیل نیست
        
        لحن: حرفه‌ای، محترمانه، دقیق، با اعتماد به نفس یک وکیل خبره
    """.trimIndent()

    // Firebase AI Logic - will work when google-services.json and API key are configured
    private fun getGenerativeModel() = try {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = "gemini-1.5-flash",
            systemInstruction = com.google.firebase.ai.type.content { text(legalSystemPrompt) }
        )
    } catch (e: Exception) {
        null
    }

    suspend fun askLegalQuestion(
        question: String,
        chatHistory: List<Pair<String, String>> = emptyList(),
        legalContext: String? = null
    ): Result<LegalAiResponse> = withContext(Dispatchers.IO) {
        try {
            // Search local knowledge base first
            val relevantArticles = IranianLegalKnowledgeBase.searchArticles(question)
            
            // Try Gemini if available
            val model = getGenerativeModel()
            if (model != null) {
                val enhancedPrompt = buildString {
                    append("سوال حقوقی: $question\n\n")
                    if (legalContext != null) append("زمینه پرونده: $legalContext\n\n")
                    if (relevantArticles.isNotEmpty()) {
                        append("مواد قانونی مرتبط از پایگاه دانش:\n")
                        relevantArticles.take(3).forEach { article ->
                            append("- ${article.lawName} ${article.articleNumber}: ${article.articleText}\n")
                        }
                        append("\n")
                    }
                    append("لطفاً بر اساس قوانین رسمی ایران پاسخ دقیق بده.")
                }

                val response = model.generateContent(enhancedPrompt)
                val text = response.text ?: "پاسخ دریافت نشد"

                Result.success(
                    LegalAiResponse(
                        answer = text,
                        relevantArticles = relevantArticles,
                        confidence = 0.95f,
                        needsLawyerReview = text.contains("پیچیده") || question.contains("کیفری")
                    )
                )
            } else {
                // Fallback to local knowledge base (offline mode)
                val fallbackAnswer = generateLocalAnswer(question, relevantArticles)
                Result.success(fallbackAnswer)
            }
        } catch (e: Exception) {
            // Offline fallback
            val relevantArticles = IranianLegalKnowledgeBase.searchArticles(question)
            val fallback = generateLocalAnswer(question, relevantArticles)
            Result.success(fallback)
        }
    }

    private fun generateLocalAnswer(
        question: String,
        articles: List<LegalArticle>
    ): LegalAiResponse {
        val answer = when {
            question.contains("تجدیدنظر") || question.contains("مهلت") -> {
                """
                طبق ماده ۳۳۶ قانون آیین دادرسی دادگاه‌های عمومی و انقلاب در امور مدنی، مهلت تجدیدنظرخواهی برای اشخاص مقیم ایران ۲۰ روز از تاریخ ابلاغ دادنامه است.
                
                نکات کلیدی:
                • روز ابلاغ و روز اقدام جزء مهلت محسوب نمی‌شود (ماده ۴۴۳)
                • اگر روز آخر مصادف با تعطیل رسمی باشد، مهلت تا اولین روز بعد از تعطیلی تمدید می‌شود (ماده ۴۴۵)
                • برای اشخاص مقیم خارج، این مهلت ۲ ماه است
                
                توصیه: حتماً از طریق دفاتر خدمات الکترونیک قضایی اقدام نمایید و رسید ثبت را نگهداری کنید.
                """.trimIndent()
            }
            question.contains("چک") || question.contains("وجه") -> {
                """
                در خصوص مطالبه وجه چک، مستند به مواد ۳۱۰ و ۳۱۱ قانون تجارت و قانون اصلاح قانون صدور چک مصوب ۱۳۹۷:
                
                • چک سند تجاری مستقل و تجریدی است - دارنده با ارائه گواهی عدم پرداخت می‌تواند مستقیماً اجراییه بگیرد
                • خسارت تاخیر تادیه بر اساس شاخص تورم بانک مرکزی از تاریخ سررسید تا پرداخت محاسبه می‌شود (ماده ۵۲۲ ق.آ.د.م)
                • طبق تبصره الحاقی ماده ۲ قانون صدور چک، صدور چک بلامحل واجد وصف کیفری است مگر موارد استثنایی
                
                مدارک لازم: اصل چک، گواهی عدم پرداخت، اظهارنامه رسمی
                """.trimIndent()
            }
            question.contains("کلاهبرداری") || question.contains("خیانت") -> {
                """
                جرم کلاهبرداری طبق ماده ۱ قانون تشدید مجازات مرتکبین ارتشاء، اختلاس و کلاهبرداری:
                
                ارکان مادی: توسل به وسایل متقلبانه، فریب قربانی، بردن مال دیگری
                ارکان معنوی: سوءنیت عام و خاص (قصد بردن مال)
                مجازات: حبس ۱ تا ۷ سال + جزای نقدی معادل مال ماخوذه + رد مال
                
                برای خیانت در امانت (ماده ۶۷۴ ق.م.ا تعزیرات): حبس ۶ ماه تا ۳ سال
                
                توصیه: شکواییه را با مستندات (پرینت حساب، پیام‌ها، شهادت شهود) به دادسرای جرایم اقتصادی تقدیم کنید.
                """.trimIndent()
            }
            question.contains("کارشناس") || question.contains("اعتراض") -> {
                """
                مهلت اعتراض به نظریه کارشناس رسمی دادگستری:
                
                طبق ماده ۲۶۰ قانون آیین دادرسی مدنی، پس از ابلاغ نظریه کارشناس، طرفین ظرف یک هفته (۷ روز) می‌توانند اعتراض خود را به صورت کتبی به دادگاه اعلام کنند.
                
                نکات:
                • اعتراض باید مستدل و با ذکر جهات باشد
                • دادگاه می‌تواند قرار ارجاع به هیات کارشناسان را صادر کند
                • هزینه کارشناسی مجدد معمولاً بر عهده معترض است مگر اینکه نظر هیات متفاوت باشد
                """.trimIndent()
            }
            else -> {
                """
                سوال شما در خصوص "$question" دریافت شد.
                
                برای ارائه مشاوره دقیق، لطفاً جزئیات بیشتری بفرمایید:
                • موضوع پرونده چیست؟ (حقوقی، کیفری، خانواده، ثبتی)
                • در چه مرحله‌ای است؟ (بدوی، تجدیدنظر، اجرا)
                • مستندات شما چیست؟
                
                سامانه میلانو لگال بر اساس آخرین قوانین و مقررات جمهوری اسلامی ایران (تا سال ۱۴۰۳) به شما مشاوره می‌دهد.
                
                ${if (articles.isNotEmpty()) "مواد مرتبط یافت شده: ${articles.joinToString { it.articleNumber }}" else ""}
                """.trimIndent()
            }
        }

        return LegalAiResponse(
            answer = answer,
            relevantArticles = articles,
            confidence = if (articles.isNotEmpty()) 0.85f else 0.6f,
            needsLawyerReview = true
        )
    }

    suspend fun generateLegalDraftWithAi(
        draftType: String,
        subject: String,
        facts: String,
        clientName: String,
        opponentName: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val model = getGenerativeModel()
            val prompt = """
                یک $draftType حرفه‌ای بنویس:
                موضوع: $subject
                موکل: $clientName
                طرف مقابل: $opponentName
                شرح واقع: $facts
                
                باید شامل:
                - سربرگ دادگاه
                - مشخصات طرفین
                - شرح دادخواست/لایحه با استناد به مواد قانونی ایران
                - خواسته/دفاعیات
                - مستندات قانونی دقیق
            """.trimIndent()

            if (model != null) {
                val response = model.generateContent(prompt)
                Result.success(response.text ?: "خطا در تولید")
            } else {
                // Fallback template
                Result.success(
                    """
                    ریاست محترم دادگاه عمومی حقوقی
                    
                    خواهان: $clientName
                    خوانده: $opponentName
                    خواسته: $subject
                    
                    با سلام و احترام، به استحضار می‌رساند:
                    $facts
                    
                    با عنایت به مواد ۱۰، ۲۱۹ و ۲۲۰ قانون مدنی و ماده ۵۱۹ قانون آیین دادرسی مدنی، تقاضای رسیدگی و صدور حکم شایسته مورد استدعاست.
                    """.trimIndent()
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class LegalAiResponse(
    val answer: String,
    val relevantArticles: List<LegalArticle>,
    val confidence: Float,
    val needsLawyerReview: Boolean
)
