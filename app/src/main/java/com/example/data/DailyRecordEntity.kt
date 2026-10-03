package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_records")
data class DailyRecordEntity(
    @PrimaryKey
    val date: String, // format "YYYY-MM-DD"
    val zekrCount: Long = 0L,
    val salawatCount: Long = 0L
)
