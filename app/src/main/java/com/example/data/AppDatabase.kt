package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ZekrEntity::class, DailyRecordEntity::class, ReminderEntity::class, SettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun zekrDao(): ZekrDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zekr_salawat_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.zekrDao())
                    }
                }
            }
        }

        private suspend fun populateInitialData(dao: ZekrDao) {
            val initialZekrs = listOf(
                ZekrEntity(
                    title = "صلوات",
                    arabicText = "اللهم صل علی محمد و آل محمد",
                    count = 0L,
                    goal = 100,
                    isCustom = false,
                    category = "salawat"
                ),
                ZekrEntity(
                    title = "سبحان‌الله",
                    arabicText = "سُبْحَانَ ٱللَّهِ",
                    count = 0L,
                    goal = 33,
                    isCustom = false,
                    category = "common"
                ),
                ZekrEntity(
                    title = "الحمدلله",
                    arabicText = "ٱلْحَمْدُ لِلَّهِ",
                    count = 0L,
                    goal = 33,
                    isCustom = false,
                    category = "common"
                ),
                ZekrEntity(
                    title = "الله‌اکبر",
                    arabicText = "ٱللَّهُ أَكْبَرُ",
                    count = 0L,
                    goal = 34,
                    isCustom = false,
                    category = "common"
                ),
                ZekrEntity(
                    title = "لا اله الا الله",
                    arabicText = "لَا إِلَهَ إِلَّا ٱللَّهُ",
                    count = 0L,
                    goal = 100,
                    isCustom = false,
                    category = "common"
                ),
                ZekrEntity(
                    title = "استغفرالله",
                    arabicText = "أَسْتَغْفِرُ ٱللَّهَ رَبِّي وَأَتُوبُ إِلَيْهِ",
                    count = 0L,
                    goal = 100,
                    isCustom = false,
                    category = "spiritual"
                ),
                ZekrEntity(
                    title = "یا الله",
                    arabicText = "يَا اللَّهُ",
                    count = 0L,
                    goal = 100,
                    isCustom = false,
                    category = "spiritual"
                ),
                ZekrEntity(
                    title = "یا رحمان",
                    arabicText = "يَا رَحْمَانُ",
                    count = 0L,
                    goal = 100,
                    isCustom = false,
                    category = "spiritual"
                ),
                ZekrEntity(
                    title = "یا رحیم",
                    arabicText = "يَا رَحِيمُ",
                    count = 0L,
                    goal = 100,
                    isCustom = false,
                    category = "spiritual"
                ),
                ZekrEntity(
                    title = "یا علی",
                    arabicText = "يَا عَلِيُّ",
                    count = 0L,
                    goal = 110,
                    isCustom = false,
                    category = "spiritual"
                ),
                ZekrEntity(
                    title = "یا حسین",
                    arabicText = "يَا حُسَيْنُ",
                    count = 0L,
                    goal = 128,
                    isCustom = false,
                    category = "spiritual"
                )
            )
            for (zekr in initialZekrs) {
                dao.insertZekr(zekr)
            }

            // Default reminders
            dao.insertReminder(ReminderEntity(time = "07:30", message = "لحظه‌ای برای آرامش... یک صلوات بفرستید. 🌿"))
            dao.insertReminder(ReminderEntity(time = "12:30", message = "یک صلوات، یک لحظه آرامش. اللهم صل علی محمد و آل محمد"))
            dao.insertReminder(ReminderEntity(time = "21:00", message = "دل را با یاد خدا آرام کنیم."))
        }
    }
}
