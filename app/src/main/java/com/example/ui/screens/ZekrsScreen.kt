package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ZekrEntity
import com.example.ui.components.CustomZekrDialog
import com.example.util.NumberUtil

@Composable
fun ZekrsScreen(
    zekrs: List<ZekrEntity>,
    selectedZekrId: Long?,
    onSelectZekr: (ZekrEntity) -> Unit,
    onAddCustomZekr: (String, String, Int) -> Unit,
    onDeleteCustomZekr: (ZekrEntity) -> Unit,
    usePersian: Boolean
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "افزودن ذکر جدید", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "انتخاب ذکر",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(zekrs) { zekr ->
                    val isSelected = zekr.id == selectedZekrId
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectZekr(zekr) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = zekr.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = zekr.arabicText,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "شمارش: ${NumberUtil.formatNumber(zekr.count, usePersian)} | هدف: ${NumberUtil.formatNumber(zekr.goal, usePersian)}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            if (zekr.isCustom) {
                                IconButton(onClick = { onDeleteCustomZekr(zekr) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "حذف ذکر",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        CustomZekrDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, arabic, goal -> onAddCustomZekr(title, arabic, goal) },
            usePersian = usePersian
        )
    }
}
