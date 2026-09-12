package com.example.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * بخش ۱۷: یادآور هوشمند - WorkManager
 * اعلان فارسی با صدا
 */
class DeadlineReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getDatabase(applicationContext, kotlinx.coroutines.CoroutineScope(Dispatchers.IO))
            val activeDeadlines = database.judicialDeadlineDao().getActiveDeadlines()

            // در نسخه واقعی، اینجا Notification فارسی با TTS ارسال می‌شود
            // شبیه‌سازی: لاگ کردن مواعد بحرانی

            // val criticalDeadlines = activeDeadlines.first().filter { it.daysRemaining <= 3 }

            // NotificationManagerCompat.from(applicationContext).notify(...)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "meelano_deadline_reminder"
    }
}

// Helper برای زمان‌بندی
object ReminderScheduler {
    fun getReminderMessage(daysRemaining: Int, title: String): String {
        return when {
            daysRemaining <= 1 -> "⚠️ هشدار بحرانی: فقط $daysRemaining روز تا مهلت $title باقی مانده!"
            daysRemaining <= 3 -> "⏰ هشدار فوری: $daysRemaining روز تا $title"
            daysRemaining <= 7 -> "📅 یادآوری: $daysRemaining روز تا $title"
            else -> "موعد $title: $daysRemaining روز باقی‌مانده"
        }
    }

    fun getPersianTtsReminder(daysRemaining: Int, title: String): String {
        return "وکیل محترم، ${getReminderMessage(daysRemaining, title)}. لطفاً اقدام قانونی لازم را انجام دهید. میلانو لگال"
    }
}
