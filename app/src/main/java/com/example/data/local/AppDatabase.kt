package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.EblaghItem
import com.example.data.model.JudicialDeadline
import com.example.data.model.LegalCase
import com.example.data.model.LegalDraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        LegalCase::class,
        JudicialDeadline::class,
        EblaghItem::class,
        LegalDraft::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun legalCaseDao(): LegalCaseDao
    abstract fun judicialDeadlineDao(): JudicialDeadlineDao
    abstract fun eblaghDao(): EblaghDao
    abstract fun legalDraftDao(): LegalDraftDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meelanoe_legal_local.db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialLegalData(database)
                    }
                }
            }
        }

        suspend fun populateInitialLegalData(database: AppDatabase) {
            val caseDao = database.legalCaseDao()
            val deadlineDao = database.judicialDeadlineDao()
            val eblaghDao = database.eblaghDao()
            val draftDao = database.legalDraftDao()

            val cases = listOf(
                LegalCase(
                    caseNumber = "۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳",
                    archiveClassNumber = "۰۳۰۰۲۱۴",
                    courtBranch = "شعبه ۱۰۸ دادگاه عمومی حقوقی مجتمع قضایی شهید بهشتی تهران",
                    caseTitle = "مطالبه وجه ۳ فقره چک صیادی به همراه خسارت تاخیر تادیه",
                    clientName = "شرکت بازرگانی میلانو نوین",
                    clientRole = "خواهان",
                    oppositeParty = "شرکت ساختمانی فراز گستر پایدار",
                    caseStatus = "در جریان رسیدگی",
                    priority = "فوری",
                    summary = "صدور گواهی عدم پرداخت چک‌ها به مبلغ ۳۵ میلیارد ریال از بانک صادرات شعبه وزرا به دلیل کسری موجودی حساب جاری خوانده.",
                    defenseStrategy = "استناد به مواد ۳۱۱ و ۳۱۳ قانون تجارت، قانون اصلاح قانون صدور چک مصوب ۹۷ و تبصره الحاقی به ماده ۲ قانون صدور چک مجمع تشخیص مصلحت نظام."
                ),
                LegalCase(
                    caseNumber = "۱۴۰۳۹۲۰۰۰۱۸۹۵۲۴",
                    archiveClassNumber = "۰۳۰۰۸۹۰",
                    courtBranch = "شعبه ۲۴ دادگاه تجدیدنظر استان تهران",
                    caseTitle = "تجدیدنظرخواهی از دادنامه خلع ید و قلع و قمع مستحدثات",
                    clientName = "مهندس میلاد رسولی",
                    clientRole = "تجدیدنظرخواه",
                    oppositeParty = "شهرداری منطقه یک تهران",
                    caseStatus = "در حال تجدیدنظر",
                    priority = "بحرانی",
                    summary = "اعتراض به دادنامه بدوی دایر بر رد دعوای تصرف عدوانی با استناد به سند مالکیت تک‌برگ رسمی و رای کمیسیون ماده ۱۰۰.",
                    defenseStrategy = "استناد به مواد ۲۲ و ۷۳ قانون ثبت اسناد و املاک، ماده ۳۴۸ قانون آیین دادرسی دادگاه‌های عمومی و انقلاب در امور مدنی."
                ),
                LegalCase(
                    caseNumber = "۱۴۰۳۸۱۰۰۰۲۳۹۸۷۱",
                    archiveClassNumber = "۰۲۰۰۶۵۱",
                    courtBranch = "شعبه ۳ بازپرسی دادسرای عمومی و انقلاب ناحیه ۳۲ (جرایم اقتصادی)",
                    caseTitle = "شکایت خیانت در امانت و کلاهبرداری شبکه‌ای",
                    clientName = "دکتر آرمین رستمی",
                    clientRole = "شاکی",
                    oppositeParty = "بهروز نامداری و شرکا",
                    caseStatus = "تحقیقات مقدماتی دادسرا",
                    priority = "فوری",
                    summary = "حیف و میل وجوه سرمایه‌گذاری ملکی و ارائه اسناد غیرواقعی به ارزش ۸۰ میلیارد ریال.",
                    defenseStrategy = "استناد به ماده ۱ قانون تشدید مجازات مرتکبین ارتشاء، اختلاس و کلاهبرداری و ماده ۶۷۴ قانون مجازات اسلامی (تعزیرات)."
                )
            )
            caseDao.insertCases(cases)

            val deadlines = listOf(
                JudicialDeadline(
                    caseId = 2,
                    caseNumber = "۱۴۰۳۹۲۰۰۰۱۸۹۵۲۴",
                    title = "مهلت ۲۰ روزه تجدیدنظرخواهی و ثبت لایحه تبادل لوایح",
                    deadlineType = "تجدیدنظرخواهی",
                    servedDate = "۱۴۰۳/۰۶/۱۰",
                    dueDate = "۱۴۰۳/۰۶/۳۰",
                    daysRemaining = 3,
                    isCompleted = false,
                    urgencyLevel = "بحرانی",
                    legalBasis = "ماده ۳۳۶ قانون آیین دادرسی مدنی (مهلت تجدیدنظر ۲۰ روز برای مقیمین ایران)",
                    notes = "پیوست اصل رسید پستی و وکالت‌نامه رسمی در دفاتر خدمات الکترونیک قضایی"
                ),
                JudicialDeadline(
                    caseId = 1,
                    caseNumber = "۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳",
                    title = "مهلت ۷ روزه اعتراض به نظریه کارشناس رسمی دادگستری",
                    deadlineType = "اعتراض به کارشناسی",
                    servedDate = "۱۴۰۳/۰۶/۱۲",
                    dueDate = "۱۴۰۳/۰۶/۱۹",
                    daysRemaining = 6,
                    isCompleted = false,
                    urgencyLevel = "فوری",
                    legalBasis = "ماده ۲۶۰ قانون آیین دادرسی مدنی (مهلت یک هفته برای اعتراض به نظر کارشناس)",
                    notes = "محاسبه دقیق خسارت تاخیر تادیه بر اساس شاخص بانک مرکزی در زمان تادیه چک"
                ),
                JudicialDeadline(
                    caseId = 3,
                    caseNumber = "۱۴۰۳۸۱۰۰۰۲۳۹۸۷۱",
                    title = "حضور در جلسه تحقیق و بازپرسی با حضور شهود",
                    deadlineType = "حضور در دادسرا",
                    servedDate = "۱۴۰۳/۰۶/۰۵",
                    dueDate = "۱۴۰۳/۰۶/۲۵",
                    daysRemaining = 12,
                    isCompleted = false,
                    urgencyLevel = "عادی",
                    legalBasis = "ماده ۱۶۸ قانون آیین دادرسی کیفری (احضار متهم و شهود)",
                    notes = "ارائه پرینت پیامک‌ها و تراکنش‌های بانکی تایید شده ساتنا"
                )
            )
            deadlineDao.insertDeadlines(deadlines)

            val eblaghs = listOf(
                EblaghItem(
                    eblaghNumber = "۱۴۰۳۰۲۸۱۹۰۰۰۵۴۸",
                    caseNumber = "۱۴۰۳۹۲۰۰۰۱۸۹۵۲۴",
                    branchName = "شعبه ۲۴ تجدیدنظر استان تهران",
                    dateStr = "۱۴۰۳/۰۶/۱۱",
                    subject = "ابلاغ دادنامه بدوی و آغاز مهلت تجدیدنظرخواهی",
                    content = "رای دادگاه بدوی در خصوص پرونده کلاسه ۰۳۰۰۸۹۰ صادر گردید. مهلت تجدیدنظرخواهی ۲۰ روز از تاریخ رویت ابلاغیه الکترونیکی می‌باشد.",
                    actionDeadlineDays = 20,
                    isProcessed = false
                ),
                EblaghItem(
                    eblaghNumber = "۱۴۰۳۰۲۸۱۹۰۰۰۳۲۱",
                    caseNumber = "۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳",
                    branchName = "شعبه ۱۰۸ دادگاه عمومی حقوقی تهران",
                    dateStr = "۱۴۰۳/۰۶/۱۲",
                    subject = "ابلاغ نظریه هیات کارشناسان رسمی",
                    content = "گزارش و نظریه کارشناس حسابداری و حسابرسی واصل گردید. طرفین ظرف ۷ روز می‌توانند نظر خود را کتباً اعلام نمایند.",
                    actionDeadlineDays = 7,
                    isProcessed = false
                )
            )
            eblaghDao.insertAllEblagh(eblaghs)

            val drafts = listOf(
                LegalDraft(
                    title = "لایحه دفاعیه مطالبه وجه چک و خسارت تاخیر تادیه",
                    draftType = "لایحه دفاعیه",
                    caseNumber = "۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳",
                    courtHeading = "ریاست و مستشاران محترم دادگاه عمومی حقوقی مجتمع قضایی شهید بهشتی تهران",
                    bodyText = "با سلام و احترام،\nدر خصوص پرونده کلاسه ۰۳۰۰۲۱۴ مطروحه نزد آن شعبه محترم، بوکالت از خواهان (شرکت بازرگانی میلانو نوین) خاطر عالی را مستحضر می‌دارد:\nخوانده محترم متعهد به پرداخت وجه چک‌های موضوع دعوا بوده و به رغم حلول سررسید و برگشت چک‌ها، از تادیه دین استنکاف ورزیده است. نظر به اصل تجریدی بودن اسناد تجاری و عدم اثبات هرگونه پرداخت توسط خوانده، صدور حکم بر محکومیت خوانده به پرداخت اصل خواسته به همراه خسارت تاخیر تادیه بر مبنای شاخص تورم اعلامی بانک مرکزی جمهوری اسلامی ایران تا زمان اجرای حکم و کلیه خسارات دادرسی و حق‌الوکاله وکیل مورد استدعاست.",
                    legalArticles = "مواد ۳۱۰، ۳۱۱ و ۳۱۳ قانون تجارت - تبصره الحاقی به ماده ۲ قانون صدور چک مجمع تشخیص مصلحت نظام - مواد ۵۱۹ و ۵۲۲ قانون آیین دادرسی مدنی",
                    dateCreated = "۱۴۰۳/۰۶/۱۳"
                ),
                LegalDraft(
                    title = "شکواییه کلاهبرداری و تحصیل مال از طریق نامشروع",
                    draftType = "شکواییه",
                    caseNumber = "۱۴۰۳۸۱۰۰۰۲۳۹۸۷۱",
                    courtHeading = "دادستان محترم عمومی و انقلاب تهران / ریاست محترم شعبه ۳ بازپرسی",
                    bodyText = "با عرض سلام و ادای احترام،\nمشتکی‌عنه با مانورهای متقلبانه و تاسیس دفاتر واهی اقدام به اغوای موکل و اخذ وجوه نموده است. با عنایت به وقوع ارکان مادی و معنوی جرم و فریب موکل در قالب سرمایه‌گذاری غیرواقعی، تعقیب کیفری و مجازات مشتکی‌عنه و رد مال به انضمام کلیه خسارات وارده مورد استدعاست.",
                    legalArticles = "ماده ۱ قانون تشدید مجازات مرتکبین ارتشاء، اختلاس و کلاهبرداری - ماده ۲ قانون تشدید (تحصیل نامشروع مال)",
                    dateCreated = "۱۴۰۳/۰۶/۱۰"
                )
            )
            draftDao.insertDrafts(drafts)
        }
    }
}
