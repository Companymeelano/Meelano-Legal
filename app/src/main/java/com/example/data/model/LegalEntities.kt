package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "legal_cases")
data class LegalCase(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val caseNumber: String,
    val archiveClassNumber: String,
    val courtBranch: String,
    val caseTitle: String,
    val clientName: String,
    val clientRole: String, // خواهان، خوانده، شاکی، متهم
    val oppositeParty: String,
    val caseStatus: String, // در جریان رسیدگی، در حال تجدیدنظر، اجرای احکام، مختومه
    val priority: String, // بحرانی، فوری، عادی
    val summary: String,
    val defenseStrategy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = true
)

@Entity(tableName = "judicial_deadlines")
data class JudicialDeadline(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val caseId: Long = 0,
    val caseNumber: String,
    val title: String,
    val deadlineType: String, // تجدیدنظرخواهی، واخواهی، اعتراض به کارشناسی، تبادل لوایح، حضور در جلسه
    val servedDate: String,
    val dueDate: String,
    val daysRemaining: Int,
    val isCompleted: Boolean = false,
    val urgencyLevel: String, // بحرانی، فوری، عادی
    val legalBasis: String, // مثلاً ماده ۳۳۶ قانون آیین دادرسی مدنی
    val notes: String = ""
)

@Entity(tableName = "eblagh_items")
data class EblaghItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eblaghNumber: String,
    val caseNumber: String,
    val branchName: String,
    val dateStr: String,
    val subject: String,
    val content: String,
    val actionDeadlineDays: Int = 20,
    val isProcessed: Boolean = false
)

@Entity(tableName = "legal_drafts")
data class LegalDraft(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val draftType: String, // لایحه دفاعیه، دادخواست، شکواییه، اظهارنامه
    val caseNumber: String = "",
    val courtHeading: String,
    val bodyText: String,
    val legalArticles: String,
    val dateCreated: String = ""
)

data class DatabaseConnectionInfo(
    val databaseName: String = "meelanoe_legal",
    val userName: String = "meelanoe_legaluser",
    val password: String = "Milad@1369",
    val host: String = "meelano.ir",
    val port: Int = 3306,
    val isConnected: Boolean = true,
    val lastSyncTime: String = "هم‌اکنون",
    val statusMessage: String = "اتصال به پایگاه داده meelanoe_legal برقرار است (MySQL/MariaDB)",
    val pingLatencyMs: Long = 42
)
