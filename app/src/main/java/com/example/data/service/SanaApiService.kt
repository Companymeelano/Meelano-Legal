package com.example.data.service

import com.example.data.model.EblaghItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * بخش ۱۹: اتصال به سامانه ثنا - API واقعی
 * شبیه‌سازی اتصال به adliran.ir
 */
data class SanaAuthRequest(
    val username: String,
    val password: String,
    val captcha: String
)

data class SanaEblaghResponse(
    val success: Boolean,
    val eblaghs: List<EblaghItem>,
    val totalCount: Int,
    val hasMore: Boolean
)

class SanaApiService {

    private var isAuthenticated = false
    private var sessionToken: String? = null

    suspend fun authenticate(username: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            delay(1500)
            // شبیه‌سازی لاگین ثنا
            if (username.length >= 10) {
                isAuthenticated = true
                sessionToken = "SANA_TOKEN_${System.currentTimeMillis()}_LUXURY"
                Result.success(sessionToken!!)
            } else {
                Result.failure(Exception("نام کاربری ثنا نامعتبر است"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchEblaghs(page: Int = 1): Result<SanaEblaghResponse> = withContext(Dispatchers.IO) {
        if (!isAuthenticated) {
            return@withContext Result.failure(Exception("ابتدا به سامانه ثنا وارد شوید"))
        }

        try {
            delay(2000)

            // شبیه‌سازی دریافت ابلاغیه‌ها
            val mockEblaghs = listOf(
                EblaghItem(
                    eblaghNumber = "۱۴۰۳${(100000000..999999999).random()}",
                    caseNumber = "۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳",
                    branchName = "شعبه ۱۰۸ دادگاه عمومی حقوقی تهران",
                    dateStr = "۱۴۰۳/۰۹/۲۰",
                    subject = "ابلاغیه جدید ثنا - دادنامه",
                    content = "ابلاغیه جدید از طریق سامانه ثنا دریافت شد. این ابلاغیه به صورت خودکار توسط میلانو لگال لاکچری پردازش شد.",
                    actionDeadlineDays = 20,
                    isProcessed = false
                )
            )

            Result.success(
                SanaEblaghResponse(
                    success = true,
                    eblaghs = mockEblaghs,
                    totalCount = mockEblaghs.size,
                    hasMore = false
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAsSeen(eblaghNumber: String): Result<Boolean> = withContext(Dispatchers.IO) {
        delay(500)
        Result.success(true)
    }

    fun isConnected(): Boolean = isAuthenticated

    fun getConnectionStatus(): String {
        return if (isAuthenticated) "متصل به سامانه ثنا (توکن فعال)" else "غیرمتصل - نیاز به ورود"
    }
}
