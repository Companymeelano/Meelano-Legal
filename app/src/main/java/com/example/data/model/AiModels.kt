package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_chat_messages")
data class AiChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: String, // user, assistant, system
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isPersian: Boolean = true,
    val legalCategory: String = "عمومی", // حقوقی، کیفری، خانواده، ثبتی
    val relatedLawArticles: String = "",
    val isBookmarked: Boolean = false
)

data class LegalArticle(
    val id: String,
    val lawName: String, // قانون مدنی، آیین دادرسی مدنی، مجازات اسلامی
    val articleNumber: String,
    val articleText: String,
    val category: String,
    val keywords: List<String>,
    val relatedCases: String = ""
)

data class VoiceSettings(
    val isTtsEnabled: Boolean = true,
    val speechRate: Float = 1.0f,
    val pitch: Float = 1.0f,
    val language: String = "fa-IR",
    val voiceGender: String = "female"
)

data class AiAssistantState(
    val isListening: Boolean = false,
    val isSpeaking: Boolean = false,
    val isThinking: Boolean = false,
    val currentLanguage: String = "fa-IR",
    val errorMessage: String? = null
)

object IranianLegalKnowledgeBase {
    val articles = listOf(
        LegalArticle(
            id = "civil_10",
            lawName = "قانون مدنی",
            articleNumber = "ماده ۱۰",
            articleText = "قراردادهای خصوصی نسبت به کسانی که آن را منعقد نموده‌اند در صورتی که مخالف صریح قانون نباشد نافذ است.",
            category = "قراردادها",
            keywords = listOf("قرارداد", "تعهد", "اراده", "توافق")
        ),
        LegalArticle(
            id = "civil_219",
            lawName = "قانون مدنی",
            articleNumber = "ماده ۲۱۹",
            articleText = "عقودی که بر طبق قانون واقع شده باشد بین متعاملین و قائم مقام آن‌ها لازم الاتباع است مگر اینکه به رضای طرفین اقاله یا به علت قانونی فسخ شود.",
            category = "قراردادها",
            keywords = listOf("عقد", "لازم", "فسخ", "اقاله")
        ),
        LegalArticle(
            id = "civil_190",
            lawName = "قانون مدنی",
            articleNumber = "ماده ۱۹۰",
            articleText = "برای صحت هر معامله شرایط ذیل اساسی است: ۱- قصد طرفین و رضای آن‌ها ۲- اهلیت طرفین ۳- موضوع معین که مورد معامله باشد ۴- مشروعیت جهت معامله",
            category = "معاملات",
            keywords = listOf("معامله", "اهلیت", "قصد", "رضا")
        ),
        LegalArticle(
            id = "proc_336",
            lawName = "قانون آیین دادرسی مدنی",
            articleNumber = "ماده ۳۳۶",
            articleText = "مهلت درخواست تجدیدنظر برای اشخاص مقیم ایران بیست روز و برای اشخاص مقیم خارج از کشور دو ماه از تاریخ ابلاغ رای یا انقضای مهلت واخواهی می‌باشد.",
            category = "مواعد",
            keywords = listOf("تجدیدنظر", "مهلت", "ابلاغ", "۲۰ روز")
        ),
        LegalArticle(
            id = "proc_305",
            lawName = "قانون آیین دادرسی مدنی",
            articleNumber = "ماده ۳۰۵",
            articleText = "محکوم علیه غائب حق دارد به حکم غیابی اعتراض نماید. این اعتراض واخواهی نامیده می‌شود.",
            category = "واخواهی",
            keywords = listOf("واخواهی", "حکم غیابی", "اعتراض")
        ),
        LegalArticle(
            id = "proc_260",
            lawName = "قانون آیین دادرسی مدنی",
            articleNumber = "ماده ۲۶۰",
            articleText = "پس از وصول نظر کارشناس، دادگاه آن را به طرفین ابلاغ می‌کند. طرفین می‌توانند ظرف یک هفته از تاریخ ابلاغ به نظر کارشناس اعتراض کنند.",
            category = "کارشناسی",
            keywords = listOf("کارشناس", "اعتراض", "۷ روز")
        ),
        LegalArticle(
            id = "penal_1",
            lawName = "قانون تشدید مجازات مرتکبین ارتشاء و اختلاس و کلاهبرداری",
            articleNumber = "ماده ۱",
            articleText = "هر کس از راه حیله و تقلب مردم را به وجود شرکت‌ها یا تجارتخانه‌ها یا کارخانه‌ها یا موسسات موهوم یا به داشتن اموال و اختیارات واهی فریب دهد یا به امور غیرواقع امیدوار نماید یا از حوادث و پیش‌آمدهای غیرواقع بترساند و یا اسم و یا عنوان مجعول اختیار کند و به یکی از وسایل مذکور و یا وسایل تقلبی دیگر وجوه و یا اموال یا اسناد یا حوالجات یا قبوض یا مفاصا حساب و امثال آن‌ها تحصیل کرده و از این راه مال دیگری را ببرد کلاهبردار محسوب و علاوه بر رد اصل مال به صاحبش، به حبس از یک تا هفت سال و پرداخت جزای نقدی معادل مالی که اخذ کرده است محکوم می‌شود.",
            category = "کلاهبرداری",
            keywords = listOf("کلاهبرداری", "فریب", "حیله", "تقلب")
        ),
        LegalArticle(
            id = "penal_674",
            lawName = "قانون مجازات اسلامی - تعزیرات",
            articleNumber = "ماده ۶۷۴",
            articleText = "هرگاه اموال منقول یا غیرمنقول یا نوشته‌هایی از قبیل سفته و چک و قبض و نظایر آن به عنوان اجاره یا امانت یا رهن یا برای وکالت یا هر کار با اجرت یا بی‌اجرت به کسی داده شده و بنا بر این بوده است که اشیاء مذکور مسترد شود یا به مصرف معینی برسد و شخصی که آن اشیاء نزد او بوده آن‌ها را به ضرر مالکین یا متصرفین آن‌ها استعمال یا تصاحب یا تلف یا مفقود نماید به حبس از شش ماه تا سه سال محکوم خواهد شد.",
            category = "خیانت در امانت",
            keywords = listOf("خیانت", "امانت", "تصاحب", "تلف")
        ),
        LegalArticle(
            id = "check_2",
            lawName = "قانون صدور چک",
            articleNumber = "تبصره الحاقی ماده ۲",
            articleText = "چک‌هایی که در ایران عهده بانک‌های واقع در خارج از کشور صادر شده باشند از لحاظ کیفری مشمول مقررات این قانون خواهند بود.",
            category = "چک",
            keywords = listOf("چک", "مسئولیت کیفری", "بانک")
        ),
        LegalArticle(
            id = "proc_519",
            lawName = "قانون آیین دادرسی مدنی",
            articleNumber = "ماده ۵۱۹",
            articleText = "خسارات دادرسی عبارتست از هزینه دادرسی و حق الوکاله وکیل و هزینه‌های دیگری که به طور مستقیم مربوط به دادرسی و برای اثبات دعوا یا دفاع ضرورت داشته باشد.",
            category = "خسارات",
            keywords = listOf("خسارات دادرسی", "هزینه", "حق الوکاله")
        )
    )

    fun searchArticles(query: String): List<LegalArticle> {
        return articles.filter { article ->
            article.articleText.contains(query, ignoreCase = true) ||
            article.articleNumber.contains(query) ||
            article.keywords.any { it.contains(query, ignoreCase = true) } ||
            article.lawName.contains(query, ignoreCase = true)
        }
    }

    fun getByCategory(category: String): List<LegalArticle> {
        return articles.filter { it.category == category }
    }
}
