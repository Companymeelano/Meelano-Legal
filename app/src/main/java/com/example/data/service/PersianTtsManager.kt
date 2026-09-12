package com.example.data.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Persian TTS Manager - Luxury Edition
 * Supports fa-IR with fallback to fa and en
 * Reads legal texts with proper Persian pronunciation
 */
class PersianTtsManager(context: Context) {

    private var tts: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _currentText = MutableStateFlow("")
    val currentText: StateFlow<String> = _currentText.asStateFlow()

    private var speechRate = 0.9f // Slightly slower for legal clarity
    private var pitch = 1.0f

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Try Persian locale with fallbacks
                val persianLocales = listOf(
                    Locale("fa", "IR"),
                    Locale("fa"),
                    Locale.forLanguageTag("fa-IR"),
                    Locale("fa", "AF"),
                    Locale.US // fallback
                )

                var localeSet = false
                for (locale in persianLocales) {
                    val result = tts?.setLanguage(locale)
                    if (result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        localeSet = true
                        break
                    }
                }

                tts?.setSpeechRate(speechRate)
                tts?.setPitch(pitch)

                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                        _currentText.value = ""
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        _isSpeaking.value = false
                    }
                })

                _isInitialized.value = true
            }
        }
    }

    fun speakPersian(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        // Clean legal text for better TTS
        val cleanedText = cleanLegalTextForTts(text)
        _currentText.value = cleanedText
        
        // Split long texts into chunks for better handling
        val chunks = splitTextForTts(cleanedText, 300)
        
        chunks.forEachIndexed { index, chunk ->
            val mode = if (index == 0) queueMode else TextToSpeech.QUEUE_ADD
            tts?.speak(chunk, mode, null, "meelano_${System.currentTimeMillis()}_$index")
        }
    }

    fun speakWithHighlight(text: String, onWordSpoken: (Int) -> Unit) {
        // For future word-by-word highlighting
        speakPersian(text)
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _currentText.value = ""
    }

    fun pause() {
        // TTS doesn't have pause, so we stop
        stop()
    }

    fun setSpeechRate(rate: Float) {
        speechRate = rate.coerceIn(0.25f, 2.0f)
        tts?.setSpeechRate(speechRate)
    }

    fun setPitch(pitchValue: Float) {
        pitch = pitchValue.coerceIn(0.5f, 2.0f)
        tts?.setPitch(pitch)
    }

    fun isLanguageAvailable(): Boolean {
        return tts?.isLanguageAvailable(Locale("fa", "IR")) == TextToSpeech.LANG_AVAILABLE ||
               tts?.isLanguageAvailable(Locale("fa")) == TextToSpeech.LANG_AVAILABLE
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    private fun cleanLegalTextForTts(text: String): String {
        return text
            .replace("ماده", "ماده‌ی")
            .replace("ق.آ.د.م", "قانون آیین دادرسی مدنی")
            .replace("ق.م", "قانون مدنی")
            .replace("ق.م.ا", "قانون مجازات اسلامی")
            .replace(Regex("[\\[\\]{}]"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun splitTextForTts(text: String, maxLength: Int): List<String> {
        if (text.length <= maxLength) return listOf(text)
        
        val sentences = text.split(Regex("(?<=[.!؟])\\s+"))
        val chunks = mutableListOf<String>()
        var currentChunk = StringBuilder()

        for (sentence in sentences) {
            if (currentChunk.length + sentence.length > maxLength) {
                if (currentChunk.isNotEmpty()) {
                    chunks.add(currentChunk.toString())
                    currentChunk = StringBuilder()
                }
                // If single sentence is too long, split by comma
                if (sentence.length > maxLength) {
                    val parts = sentence.split("،", ",")
                    var partChunk = StringBuilder()
                    for (part in parts) {
                        if (partChunk.length + part.length > maxLength) {
                            chunks.add(partChunk.toString())
                            partChunk = StringBuilder(part)
                        } else {
                            if (partChunk.isNotEmpty()) partChunk.append("، ")
                            partChunk.append(part)
                        }
                    }
                    if (partChunk.isNotEmpty()) currentChunk.append(partChunk)
                } else {
                    currentChunk.append(sentence)
                }
            } else {
                if (currentChunk.isNotEmpty()) currentChunk.append(" ")
                currentChunk.append(sentence)
            }
        }
        if (currentChunk.isNotEmpty()) chunks.add(currentChunk.toString())
        return chunks
    }
}
