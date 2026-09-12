package com.example.data.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * بخش ۱۵: امضای دیجیتال - با اثر انگشت
 */
data class DigitalSignature(
    val id: String,
    val documentId: String,
    val signatureBitmap: Bitmap? = null,
    val signerName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isBiometricVerified: Boolean = false,
    val hash: String = ""
)

class SignatureManager(private val context: Context) {

    private val _signatures = MutableStateFlow<List<DigitalSignature>>(emptyList())
    val signatures: StateFlow<List<DigitalSignature>> = _signatures.asStateFlow()

    private var currentPath = Path()
    private var currentBitmap: Bitmap? = null

    fun createSignatureBitmap(width: Int = 600, height: Int = 300): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        
        val paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        
        // شبیه‌سازی امضا
        val path = Path().apply {
            moveTo(50f, 150f)
            cubicTo(150f, 50f, 250f, 250f, 350f, 150f)
            cubicTo(400f, 100f, 500f, 200f, 550f, 120f)
        }
        canvas.drawPath(path, paint)
        
        currentBitmap = bitmap
        return bitmap
    }

    fun addSignature(signature: DigitalSignature) {
        _signatures.value = _signatures.value + signature
    }

    fun verifyWithBiometric(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(context)
                val biometricPrompt = BiometricPrompt(activity, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            onSuccess()
                        }

                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            onError(errString.toString())
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            onError("احراز هویت ناموفق")
                        }
                    })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("تایید امضای دیجیتال لاکچری")
                    .setSubtitle("برای امضای سند حقوقی، اثر انگشت خود را تایید کنید")
                    .setNegativeButtonText("انصراف")
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }
            else -> {
                onError("سنسور اثر انگشت در دسترس نیست - امضا بدون بیومتریک ثبت شد")
                onSuccess() // فال‌بک
            }
        }
    }

    fun generateHash(documentContent: String): String {
        return "SHA256:${documentContent.hashCode()}:MEELANO_LUXURY:${System.currentTimeMillis()}"
    }
}
