package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zekrs")
data class ZekrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val arabicText: String,
    val count: Long = 0L,
    val goal: Int = 100,
    val isCustom: Boolean = false,
    val category: String = "general" // "salawat", "common", "spiritual", "custom"
)
