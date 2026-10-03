package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DailyRecordEntity
import com.example.data.ReminderEntity
import com.example.data.ZekrEntity
import com.example.data.ZekrRepository
import com.example.util.JalaliCalendar
import com.example.util.TextToSpeechUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ZekrViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ZekrRepository

    val allZekrs: Flow<List<ZekrEntity>>
    val allDailyRecords: Flow<List<DailyRecordEntity>>
    val allReminders: Flow<List<ReminderEntity>>

    private val _selectedZekr = MutableStateFlow<ZekrEntity?>(null)
    val selectedZekr: StateFlow<ZekrEntity?> = _selectedZekr.asStateFlow()

    private val _currentScreen = MutableStateFlow("home")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _themeName = MutableStateFlow("emerald")
    val themeName: StateFlow<String> = _themeName.asStateFlow()

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _vibrationEnabled = MutableStateFlow(true)
    val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled.asStateFlow()

    private val _speechEnabled = MutableStateFlow(false)
    val speechEnabled: StateFlow<Boolean> = _speechEnabled.asStateFlow()

    private val _persianNumbers = MutableStateFlow(true)
    val persianNumbers: StateFlow<Boolean> = _persianNumbers.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _todayZekrCount = MutableStateFlow(0L)
    val todayZekrCount: StateFlow<Long> = _todayZekrCount.asStateFlow()

    private val _todaySalawatCount = MutableStateFlow(0L)
    val todaySalawatCount: StateFlow<Long> = _todaySalawatCount.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).zekrDao()
        repository = ZekrRepository(dao)
        allZekrs = repository.allZekrs
        allDailyRecords = repository.allDailyRecords
        allReminders = repository.allReminders

        TextToSpeechUtil.init(application)

        viewModelScope.launch {
            val theme = repository.getSetting("theme") ?: "emerald"
            val dark = repository.getSetting("darkMode")?.toBoolean() ?: false
            val sound = repository.getSetting("sound")?.toBoolean() ?: true
            val vib = repository.getSetting("vibration")?.toBoolean() ?: true
            val speech = repository.getSetting("speech")?.toBoolean() ?: false
            val pers = repository.getSetting("persianNumbers")?.toBoolean() ?: true

            _themeName.value = theme
            _darkMode.value = dark
            _soundEnabled.value = sound
            _vibrationEnabled.value = vib
            _speechEnabled.value = speech
            _persianNumbers.value = pers

            val zekrs = allZekrs.first()
            if (zekrs.isNotEmpty()) {
                val salawat = zekrs.find { it.category == "salawat" } ?: zekrs.first()
                _selectedZekr.value = salawat
            }

            loadTodayStats()
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun selectZekr(zekr: ZekrEntity) {
        _selectedZekr.value = zekr
        showToast("انتخاب شد: ${zekr.title}")
    }

    suspend fun loadTodayStats() {
        val jDate = JalaliCalendar.getCurrentJalaliDate()
        val dateStr = JalaliCalendar.getFormattedDateString(jDate.year, jDate.month, jDate.day)
        val record = repository.getDailyRecord(dateStr)
        _todayZekrCount.value = record?.zekrCount ?: 0L
        _todaySalawatCount.value = record?.salawatCount ?: 0L
    }

    fun incrementCount() {
        val zekr = _selectedZekr.value ?: return
        viewModelScope.launch {
            val newCount = zekr.count + 1
            val updated = zekr.copy(count = newCount)
            repository.updateZekr(updated)
            _selectedZekr.value = updated

            // Speak count or phrase if speech is enabled
            if (_speechEnabled.value) {
                TextToSpeechUtil.speak("${zekr.title}, $newCount", true)
            }

            val jDate = JalaliCalendar.getCurrentJalaliDate()
            val dateStr = JalaliCalendar.getFormattedDateString(jDate.year, jDate.month, jDate.day)
            val existing = repository.getDailyRecord(dateStr) ?: DailyRecordEntity(dateStr, 0L, 0L)

            val isSalawat = zekr.category == "salawat" || zekr.title.contains("صلوات")
            val newZekrTotal = existing.zekrCount + 1
            val newSalawatTotal = if (isSalawat) existing.salawatCount + 1 else existing.salawatCount

            repository.insertOrUpdateDailyRecord(
                DailyRecordEntity(
                    date = dateStr,
                    zekrCount = newZekrTotal,
                    salawatCount = newSalawatTotal
                )
            )
            loadTodayStats()
        }
    }

    fun decrementCount() {
        val zekr = _selectedZekr.value ?: return
        if (zekr.count <= 0) return
        viewModelScope.launch {
            val newCount = zekr.count - 1
            val updated = zekr.copy(count = newCount)
            repository.updateZekr(updated)
            _selectedZekr.value = updated
            loadTodayStats()
        }
    }

    fun resetCount() {
        val zekr = _selectedZekr.value ?: return
        viewModelScope.launch {
            val updated = zekr.copy(count = 0L)
            repository.updateZekr(updated)
            _selectedZekr.value = updated
            showToast("شمارش بازنشانی شد")
        }
    }

    fun updateGoal(newGoal: Int) {
        val zekr = _selectedZekr.value ?: return
        viewModelScope.launch {
            val updated = zekr.copy(goal = newGoal)
            repository.updateZekr(updated)
            _selectedZekr.value = updated
            showToast("هدف جدید ثبت شد: $newGoal")
        }
    }

    fun addCustomZekr(title: String, arabicText: String, goal: Int) {
        if (title.isBlank() || arabicText.isBlank()) {
            showToast("عنوان و متن ذکر نمی‌تواند خالی باشد")
            return
        }
        viewModelScope.launch {
            val newZekr = ZekrEntity(
                title = title,
                arabicText = arabicText,
                count = 0L,
                goal = goal,
                isCustom = true,
                category = "custom"
            )
            repository.insertZekr(newZekr)
            showToast("ذکر جدید اضافه شد 🌿")
        }
    }

    fun deleteZekr(zekr: ZekrEntity) {
        if (!zekr.isCustom) {
            showToast("امکان حذف اذکار پیش‌فرض وجود ندارد")
            return
        }
        viewModelScope.launch {
            repository.deleteZekr(zekr)
            val zekrs = allZekrs.first()
            if (zekrs.isNotEmpty()) {
                _selectedZekr.value = zekrs.first()
            }
            showToast("ذکر حذف شد")
        }
    }

    fun updateTheme(theme: String) {
        _themeName.value = theme
        viewModelScope.launch {
            repository.setSetting("theme", theme)
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
        viewModelScope.launch {
            repository.setSetting("darkMode", enabled.toString())
        }
    }

    fun toggleSound(enabled: Boolean) {
        _soundEnabled.value = enabled
        viewModelScope.launch {
            repository.setSetting("sound", enabled.toString())
        }
    }

    fun toggleVibration(enabled: Boolean) {
        _vibrationEnabled.value = enabled
        viewModelScope.launch {
            repository.setSetting("vibration", enabled.toString())
        }
    }

    fun toggleSpeech(enabled: Boolean) {
        _speechEnabled.value = enabled
        viewModelScope.launch {
            repository.setSetting("speech", enabled.toString())
        }
    }

    fun togglePersianNumbers(enabled: Boolean) {
        _persianNumbers.value = enabled
        viewModelScope.launch {
            repository.setSetting("persianNumbers", enabled.toString())
        }
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun addReminder(time: String, message: String) {
        viewModelScope.launch {
            repository.insertReminder(ReminderEntity(time = time, message = message))
            showToast("یادآوری اضافه شد")
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
            showToast("یادآوری حذف شد")
        }
    }

    override fun onCleared() {
        super.onCleared()
        TextToSpeechUtil.shutdown()
    }
}
