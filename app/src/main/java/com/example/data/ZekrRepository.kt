package com.example.data

import kotlinx.coroutines.flow.Flow

class ZekrRepository(private val dao: ZekrDao) {
    val allZekrs: Flow<List<ZekrEntity>> = dao.getAllZekrs()
    val allDailyRecords: Flow<List<DailyRecordEntity>> = dao.getAllDailyRecords()
    val allReminders: Flow<List<ReminderEntity>> = dao.getAllReminders()

    suspend fun getZekrById(id: Long): ZekrEntity? = dao.getZekrById(id)

    suspend fun insertZekr(zekr: ZekrEntity): Long = dao.insertZekr(zekr)

    suspend fun updateZekr(zekr: ZekrEntity) = dao.updateZekr(zekr)

    suspend fun deleteZekr(zekr: ZekrEntity) = dao.deleteZekr(zekr)

    suspend fun getDailyRecord(date: String): DailyRecordEntity? = dao.getDailyRecord(date)

    suspend fun insertOrUpdateDailyRecord(record: DailyRecordEntity) = dao.insertOrUpdateDailyRecord(record)

    suspend fun insertReminder(reminder: ReminderEntity) = dao.insertReminder(reminder)

    suspend fun updateReminder(reminder: ReminderEntity) = dao.updateReminder(reminder)

    suspend fun deleteReminder(reminder: ReminderEntity) = dao.deleteReminder(reminder)

    suspend fun getSetting(key: String): String? = dao.getSetting(key)

    suspend fun setSetting(key: String, value: String) = dao.setSetting(SettingsEntity(key, value))
}
