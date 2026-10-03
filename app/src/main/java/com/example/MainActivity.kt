package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopHeader
import com.example.ui.screens.*
import com.example.ui.theme.ZekrSalawatTheme
import com.example.viewmodel.ZekrViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ZekrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeName by viewModel.themeName.collectAsState()
            val darkMode by viewModel.darkMode.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val zekrs by viewModel.allZekrs.collectAsState(initial = emptyList())
            val selectedZekr by viewModel.selectedZekr.collectAsState()
            val dailyRecords by viewModel.allDailyRecords.collectAsState(initial = emptyList())
            val reminders by viewModel.allReminders.collectAsState(initial = emptyList())
            val soundEnabled by viewModel.soundEnabled.collectAsState()
            val vibrationEnabled by viewModel.vibrationEnabled.collectAsState()
            val speechEnabled by viewModel.speechEnabled.collectAsState()
            val persianNumbers by viewModel.persianNumbers.collectAsState()
            val todayZekrCount by viewModel.todayZekrCount.collectAsState()
            val todaySalawatCount by viewModel.todaySalawatCount.collectAsState()
            val toastMessage by viewModel.toastMessage.collectAsState()

            toastMessage?.let { msg ->
                LaunchedEffect(msg) {
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    viewModel.clearToast()
                }
            }

            ZekrSalawatTheme(themeName = themeName, darkTheme = darkMode) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopHeader(
                                currentScreen = currentScreen,
                                onNavigate = { screen -> viewModel.navigateTo(screen) }
                            )
                        },
                        bottomBar = {
                            BottomNavBar(
                                currentScreen = currentScreen,
                                onNavigate = { screen -> viewModel.navigateTo(screen) }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (currentScreen) {
                                "home" -> HomeScreen(
                                    selectedZekr = selectedZekr,
                                    onIncrement = { viewModel.incrementCount() },
                                    onDecrement = { viewModel.decrementCount() },
                                    onReset = { viewModel.resetCount() },
                                    onUpdateGoal = { goal -> viewModel.updateGoal(goal) },
                                    onNavigateZekrs = { viewModel.navigateTo("zekrs") },
                                    soundEnabled = soundEnabled,
                                    vibrationEnabled = vibrationEnabled,
                                    persianNumbers = persianNumbers
                                )
                                "salawat" -> SalawatScreen(
                                    salawatZekr = zekrs.find { it.category == "salawat" || it.title.contains("صلوات") } ?: selectedZekr,
                                    onIncrement = { viewModel.incrementCount() },
                                    onReset = { viewModel.resetCount() },
                                    todaySalawatCount = todaySalawatCount,
                                    soundEnabled = soundEnabled,
                                    vibrationEnabled = vibrationEnabled,
                                    persianNumbers = persianNumbers
                                )
                                "zekrs" -> ZekrsScreen(
                                    zekrs = zekrs,
                                    selectedZekrId = selectedZekr?.id,
                                    onSelectZekr = { zekr ->
                                        viewModel.selectZekr(zekr)
                                        viewModel.navigateTo("home")
                                    },
                                    onAddCustomZekr = { title, arabic, goal ->
                                        viewModel.addCustomZekr(title, arabic, goal)
                                    },
                                    onDeleteCustomZekr = { zekr ->
                                        viewModel.deleteZekr(zekr)
                                    },
                                    usePersian = persianNumbers
                                )
                                "stats" -> StatsScreen(
                                    zekrs = zekrs,
                                    dailyRecords = dailyRecords,
                                    todayZekrCount = todayZekrCount,
                                    todaySalawatCount = todaySalawatCount,
                                    usePersian = persianNumbers
                                )
                                "calendar" -> CalendarScreen(
                                    dailyRecords = dailyRecords,
                                    usePersian = persianNumbers
                                )
                                "history" -> HistoryScreen(
                                    dailyRecords = dailyRecords,
                                    usePersian = persianNumbers
                                )
                                "reminders" -> RemindersScreen(
                                    reminders = reminders,
                                    onDeleteReminder = { reminder -> viewModel.deleteReminder(reminder) }
                                )
                                "settings" -> SettingsScreen(
                                    currentTheme = themeName,
                                    onThemeChange = { th -> viewModel.updateTheme(th) },
                                    darkMode = darkMode,
                                    onDarkModeChange = { dm -> viewModel.toggleDarkMode(dm) },
                                    soundEnabled = soundEnabled,
                                    onSoundChange = { s -> viewModel.toggleSound(s) },
                                    vibrationEnabled = vibrationEnabled,
                                    onVibrationChange = { v -> viewModel.toggleVibration(v) },
                                    speechEnabled = speechEnabled,
                                    onSpeechChange = { sp -> viewModel.toggleSpeech(sp) },
                                    persianNumbers = persianNumbers,
                                    onPersianNumbersChange = { p -> viewModel.togglePersianNumbers(p) },
                                    onNavigateReminders = { viewModel.navigateTo("reminders") }
                                )
                                "about" -> AboutScreen()
                                else -> HomeScreen(
                                    selectedZekr = selectedZekr,
                                    onIncrement = { viewModel.incrementCount() },
                                    onDecrement = { viewModel.decrementCount() },
                                    onReset = { viewModel.resetCount() },
                                    onUpdateGoal = { goal -> viewModel.updateGoal(goal) },
                                    onNavigateZekrs = { viewModel.navigateTo("zekrs") },
                                    soundEnabled = soundEnabled,
                                    vibrationEnabled = vibrationEnabled,
                                    persianNumbers = persianNumbers
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
