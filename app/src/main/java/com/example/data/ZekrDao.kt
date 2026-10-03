package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ZekrDao {
    @Query("SELECT * FROM zekrs")
    fun getAllZekrs(): Flow<List<ZekrEntity>>

    @Query("SELECT * FROM zekrs WHERE id = :id")
    suspend fun getZekrById(id: Long): ZekrEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZekr(zekr: ZekrEntity): Long

    @Update
    suspend fun updateZekr(zekr: ZekrEntity)

    @Delete
    suspend fun deleteZekr(zekr: ZekrEntity)

    @Query("SELECT * FROM daily_records ORDER BY date DESC")
    fun getAllDailyRecords(): Flow<List<DailyRecordEntity>>

    @Query("SELECT * FROM daily_records WHERE date = :date")
    suspend fun getDailyRecord(date: String): DailyRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyRecord(record: DailyRecordEntity)

    @Query("SELECT * FROM reminders")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM settings")
    suspend fun getAllSettings(): List<SettingsEntity>

    @Query("SELECT value FROM settings WHERE `key` = :key")
    suspend fun getSetting(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(setting: SettingsEntity)
}
