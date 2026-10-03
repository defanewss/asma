package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ZekrEntity
import com.example.ui.components.GoalDialog
import com.example.ui.components.ShareDialog
import com.example.util.AudioVibrateUtil
import com.example.util.NumberUtil

@Composable
fun HomeScreen(
    selectedZekr: ZekrEntity?,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onUpdateGoal: (Int) -> Unit,
    onNavigateZekrs: () -> Unit,
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    persianNumbers: Boolean
) {
    val context = LocalContext.current
    var showGoalDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }

    val currentCount = selectedZekr?.count ?: 0L
    val goal = selectedZekr?.goal ?: 100
    val progress = if (goal > 0) (currentCount.toFloat() / goal).coerceIn(0f, 1f) else 0f

    val isGoalReached = currentCount >= goal && goal > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateZekrs() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = selectedZekr?.title ?: "صلوات",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedZekr?.arabicText ?: "اللهم صل علی محمد و آل محمد",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    AudioVibrateUtil.playClick(context, soundEnabled, vibrationEnabled)
                    if (currentCount + 1L == goal.toLong()) {
                        AudioVibrateUtil.playGoalComplete(context, soundEnabled, vibrationEnabled)
                    }
                    onIncrement()
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = selectedZekr?.title ?: "صلوات",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = NumberUtil.formatNumber(currentCount, persianNumbers),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 64.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "هدف: ${NumberUtil.formatNumber(goal, persianNumbers)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )
                if (isGoalReached) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "✨ ماشاءالله - هدف کامل شد",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.errorContainer
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { showGoalDialog = true }) {
                    Text("تنظیم هدف (${NumberUtil.formatNumber(goal, persianNumbers)})")
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onDecrement() },
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            ) {
                Icon(Icons.Filled.Remove, contentDescription = "کاهش")
            }

            IconButton(
                onClick = { onReset() },
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "بازنشانی", tint = MaterialTheme.colorScheme.onErrorContainer)
            }

            IconButton(
                onClick = { showShareDialog = true },
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
            ) {
                Icon(Icons.Filled.Share, contentDescription = "اشتراک‌گذاری")
            }
        }
    }

    if (showGoalDialog && selectedZekr != null) {
        GoalDialog(
            currentGoal = goal,
            onDismiss = { showGoalDialog = false },
            onSaveGoal = { newG -> onUpdateGoal(newG) },
            usePersian = persianNumbers
        )
    }

    if (showShareDialog && selectedZekr != null) {
        ShareDialog(
            zekrTitle = selectedZekr.title,
            count = currentCount,
            onDismiss = { showShareDialog = false },
            onCopy = {},
            usePersian = persianNumbers
        )
    }
}
