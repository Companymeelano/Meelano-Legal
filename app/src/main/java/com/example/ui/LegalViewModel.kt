package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AiChatMessage
import com.example.data.model.DatabaseConnectionInfo
import com.example.data.model.EblaghItem
import com.example.data.model.JudicialDeadline
import com.example.data.model.LegalArticle
import com.example.data.model.LegalCase
import com.example.data.model.LegalDraft
import com.example.data.repository.LegalRepository
import com.example.data.service.PersianTtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LegalViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val repository = LegalRepository(database)

    // TTS Manager - Luxury Persian Voice
    private val ttsManager = PersianTtsManager(application.applicationContext)
    val isTtsSpeaking: StateFlow<Boolean> = ttsManager.isSpeaking
    val isTtsInitialized: StateFlow<Boolean> = ttsManager.isInitialized

    val dbConnection: StateFlow<DatabaseConnectionInfo> = repository.dbConnectionState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow("همه")
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _selectedLegalCategory = MutableStateFlow("همه")
    val selectedLegalCategory: StateFlow<String> = _selectedLegalCategory.asStateFlow()

    // Filtered Cases Flow
    val filteredCases: StateFlow<List<LegalCase>> = combine(
        repository.allCases,
        _searchQuery,
        _statusFilter
    ) { cases, query, filter ->
        cases.filter { caseItem ->
            val matchesQuery = query.isBlank() ||
                    caseItem.caseTitle.contains(query, ignoreCase = true) ||
                    caseItem.caseNumber.contains(query, ignoreCase = true) ||
                    caseItem.clientName.contains(query, ignoreCase = true) ||
                    caseItem.courtBranch.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "همه" -> true
                else -> caseItem.caseStatus == filter
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deadlines: StateFlow<List<JudicialDeadline>> = repository.allDeadlines
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeDeadlinesCount: StateFlow<Int> = repository.activeDeadlines
        .combine(repository.allDeadlines) { active, _ -> active.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val eblaghs: StateFlow<List<EblaghItem>> = repository.allEblagh
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val drafts: StateFlow<List<LegalDraft>> = repository.allDrafts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Chat - Luxury Edition
    val chatMessages: StateFlow<List<AiChatMessage>> = repository.allChatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val legalArticles: StateFlow<List<LegalArticle>> = MutableStateFlow(
        repository.searchLegalArticles("")
    ).asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(filter: String) {
        _statusFilter.value = filter
    }

    fun setLegalCategory(category: String) {
        _selectedLegalCategory.value = category
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // CRUD Actions - Enhanced
    fun addCase(
        caseNumber: String,
        archiveClass: String,
        courtBranch: String,
        title: String,
        client: String,
        role: String,
        opponent: String,
        status: String,
        priority: String,
        summary: String,
        strategy: String
    ) {
        viewModelScope.launch {
            val newCase = LegalCase(
                caseNumber = caseNumber,
                archiveClassNumber = archiveClass,
                courtBranch = courtBranch,
                caseTitle = title,
                clientName = client,
                clientRole = role,
                oppositeParty = opponent,
                caseStatus = status,
                priority = priority,
                summary = summary,
                defenseStrategy = strategy
            )
            repository.insertCase(newCase)
            _toastMessage.value = "✨ پرونده جدید با موفقیت در سامانه لاکچری ثبت شد"
        }
    }

    fun deleteCase(id: Long) {
        viewModelScope.launch {
            repository.deleteCase(id)
            _toastMessage.value = "پرونده حذف شد"
        }
    }

    fun addDeadline(
        caseNumber: String,
        title: String,
        type: String,
        servedDate: String,
        days: Int,
        legalBasis: String,
        notes: String
    ) {
        viewModelScope.launch {
            val urgency = when {
                days <= 3 -> "بحرانی"
                days <= 7 -> "فوری"
                else -> "عادی"
            }
            val deadline = JudicialDeadline(
                caseNumber = caseNumber,
                title = title,
                deadlineType = type,
                servedDate = servedDate,
                dueDate = "سررسید ($days روز بعد) - مطابق ماده ۴۴۳ ق.آ.د.م",
                daysRemaining = days,
                urgencyLevel = urgency,
                legalBasis = legalBasis,
                notes = notes
            )
            repository.insertDeadline(deadline)
            _toastMessage.value = "⏰ موعد قضایی جدید با تحلیل هوشمند افزوده شد"
        }
    }

    fun toggleDeadlineStatus(deadline: JudicialDeadline) {
        viewModelScope.launch {
            repository.updateDeadline(deadline.copy(isCompleted = !deadline.isCompleted))
            if (!deadline.isCompleted) {
                _toastMessage.value = "✅ اقدام موعد قضایی ثبت شد"
            }
        }
    }

    fun deleteDeadline(id: Long) {
        viewModelScope.launch {
            repository.deleteDeadline(id)
            _toastMessage.value = "موعد قضایی حذف شد"
        }
    }

    fun markEblaghProcessed(item: EblaghItem) {
        viewModelScope.launch {
            repository.markEblaghProcessed(item)
            _toastMessage.value = "📩 ابلاغیه ثنا با موفقیت تعیین تکلیف شد"
        }
    }

    fun addDraft(
        type: String,
        title: String,
        courtHeading: String,
        caseNumber: String,
        body: String,
        articles: String
    ) {
        viewModelScope.launch {
            val draft = LegalDraft(
                title = title,
                draftType = type,
                courtHeading = courtHeading,
                caseNumber = caseNumber,
                bodyText = body,
                legalArticles = articles
            )
            repository.insertDraft(draft)
            _toastMessage.value = "📝 پیش‌نویس لاکچری ذخیره شد"
        }
    }

    fun createAutoDraft(
        type: String,
        court: String,
        caseNumber: String,
        client: String,
        opponent: String,
        subject: String
    ) {
        viewModelScope.launch {
            val draft = repository.generateLegalDraft(
                type = type,
                courtHeading = court,
                caseNumber = caseNumber,
                clientName = client,
                opponentName = opponent,
                subject = subject
            )
            repository.insertDraft(draft)
            _toastMessage.value = "✨ پیش‌نویس حقوقی هوشمند با تحلیل قوانین ایران تنظیم شد"
        }
    }

    fun deleteDraft(id: Long) {
        viewModelScope.launch {
            repository.deleteDraft(id)
            _toastMessage.value = "پیش‌نویس حذف شد"
        }
    }

    // AI Chat Actions - Luxury Persian AI
    fun sendAiMessage(question: String, context: String? = null) {
        if (question.isBlank()) return
        
        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                val result = repository.askAiLegalQuestion(question, context)
                result.onSuccess { message ->
                    // Auto speak the response in Persian
                    if (message.role == "assistant") {
                        speakPersianText(message.content)
                    }
                }.onFailure {
                    _toastMessage.value = "خطا در ارتباط با هوش مصنوعی"
                }
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChatHistory()
            _toastMessage.value = "تاریخچه گفتگو پاک شد"
        }
    }

    fun deleteChatMessage(id: Long) {
        viewModelScope.launch {
            repository.deleteChatMessage(id)
        }
    }

    // TTS Actions - Persian Voice
    fun speakPersianText(text: String) {
        try {
            // Limit to first 1000 chars for TTS performance
            val speakText = if (text.length > 1000) text.take(1000) + "... ادامه متن در صفحه قابل مطالعه است" else text
            ttsManager.speakPersian(speakText)
        } catch (e: Exception) {
            _toastMessage.value = "خطا در پخش صوت فارسی"
        }
    }

    fun stopSpeaking() {
        ttsManager.stop()
    }

    fun setTtsSpeed(speed: Float) {
        ttsManager.setSpeechRate(speed)
    }

    // Database Actions - Luxury
    fun testDbConnection(host: String, db: String, user: String, pass: String) {
        viewModelScope.launch {
            _isSyncing.value = true
            val result = repository.testDatabaseConnection(host, db, user, pass)
            _isSyncing.value = false
            result.onSuccess {
                _toastMessage.value = "✅ اتصال امن لاکچری به دیتابیس meelanoe_legal تایید شد"
            }.onFailure {
                _toastMessage.value = "❌ خطا در اتصال امن"
            }
        }
    }

    fun syncDatabase() {
        viewModelScope.launch {
            _isSyncing.value = true
            val result = repository.syncWithRemoteDatabase()
            _isSyncing.value = false
            result.onSuccess { count ->
                _toastMessage.value = "🔄 همگام‌سازی هوشمند $count پرونده با تحلیل قوانین به‌روز تکمیل شد"
            }.onFailure {
                _toastMessage.value = "خطا در همگام‌سازی"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
