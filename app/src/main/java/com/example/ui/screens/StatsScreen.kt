package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.DailyRecordEntity
import com.example.data.ZekrEntity
import com.example.util.NumberUtil

@Composable
fun StatsScreen(
    zekrs: List<ZekrEntity>,
    dailyRecords: List<DailyRecordEntity>,
    todayZekrCount: Long,
    todaySalawatCount: Long,
    usePersian: Boolean
) {
    val totalZekrAll = zekrs.sumOf { it.count }
    val totalSalawatAll = zekrs.find { it.category == "salawat" || it.title.contains("صلوات") }?.count ?: 0L
    val activeDaysCount = dailyRecords.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "گزارش و آمار معنوی",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "ذکر امروز",
                    value = NumberUtil.formatNumber(todayZekrCount, usePersian),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "صلوات امروز",
                    value = NumberUtil.formatNumber(todaySalawatCount, usePersian),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "مجموع کل اذکار",
                    value = NumberUtil.formatNumber(totalZekrAll, usePersian),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "روزهای فعال",
                    value = NumberUtil.formatNumber(activeDaysCount, usePersian),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 7-Day Activity Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "فعالیت ۷ روز اخیر",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (dailyRecords.isEmpty()) {
                        Text("هنوز داده‌ای ثبت نشده است.", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        val recentRecords = dailyRecords.take(7).reversed()
                        val maxCount = recentRecords.maxOfOrNull { maxOf(it.zekrCount, 1L) } ?: 1L

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            recentRecords.forEach { record ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = record.date, style = MaterialTheme.typography.bodySmall)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width((150 * (record.zekrCount.toFloat() / maxCount)).coerceAtLeast(10f).dp)
                                                .height(16.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                        Text(
                                            text = NumberUtil.formatNumber(record.zekrCount, usePersian),
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
