package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.DailyRecordEntity
import com.example.util.JalaliCalendar
import com.example.util.NumberUtil

@Composable
fun CalendarScreen(
    dailyRecords: List<DailyRecordEntity>,
    usePersian: Boolean
) {
    val jDate = JalaliCalendar.getCurrentJalaliDate()
    var selectedDay by remember { mutableStateOf(jDate.day) }

    val recordMap = remember(dailyRecords) {
        dailyRecords.associateBy { it.date }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "تقویم معنوی (جلالی)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${jDate.monthName} ${NumberUtil.formatNumber(jDate.year, usePersian)}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Days Grid Header
        val weekDays = listOf("ش", "ی", "د", "س", "چ", "پ", "ج")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Days Grid (30 days for current month demo)
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(30) { index ->
                val dayNum = index + 1
                val isSelected = dayNum == selectedDay
                val dateStr = JalaliCalendar.getFormattedDateString(jDate.year, jDate.month, dayNum)
                val hasRecord = recordMap.containsKey(dateStr)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                hasRecord -> MaterialTheme.colorScheme.secondaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                        .clickable { selectedDay = dayNum },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = NumberUtil.formatNumber(dayNum, usePersian),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (hasRecord) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
        }

        // Selected Day Details
        val selectedDateStr = JalaliCalendar.getFormattedDateString(jDate.year, jDate.month, selectedDay)
        val selectedRecord = recordMap[selectedDateStr]

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "گزارش روز: ${jDate.monthName} ${NumberUtil.formatNumber(selectedDay, usePersian)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "مجموع ذکر: ${NumberUtil.formatNumber(selectedRecord?.zekrCount ?: 0L, usePersian)}")
                Text(text = "مجموع صلوات: ${NumberUtil.formatNumber(selectedRecord?.salawatCount ?: 0L, usePersian)}")
            }
        }
    }
}
