package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.util.NumberUtil

@Composable
fun CustomZekrDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, Int) -> Unit,
    usePersian: Boolean
) {
    var title by remember { mutableStateOf("") }
    var arabicText by remember { mutableStateOf("") }
    var goalStr by remember { mutableStateOf("100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("افزودن ذکر جدید", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان ذکر (مثلا: ذکر آرامش)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = arabicText,
                    onValueChange = { arabicText = it },
                    label = { Text("متن عربی / ذکر") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = goalStr,
                    onValueChange = { goalStr = it },
                    label = { Text("هدف روزانه") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val goal = goalStr.toIntOrNull() ?: 100
                    onAdd(title, arabicText, goal)
                    onDismiss()
                }
            ) {
                Text("افزودن")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

@Composable
fun GoalDialog(
    currentGoal: Int,
    onDismiss: () -> Unit,
    onSaveGoal: (Int) -> Unit,
    usePersian: Boolean
) {
    var goalStr by remember { mutableStateOf(currentGoal.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("تظیم هدف روزانه", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("هدف مورد نظر خود را وارد کنید (پیشنهاد: ۳۳، ۱۰۰، ۳۱۳)")
                OutlinedTextField(
                    value = goalStr,
                    onValueChange = { goalStr = it },
                    label = { Text("هدف روزانه") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(33, 100, 313, 1000).forEach { g ->
                        OutlinedButton(
                            onClick = { goalStr = g.toString() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(NumberUtil.formatNumber(g, usePersian))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val g = goalStr.toIntOrNull() ?: 100
                    onSaveGoal(g)
                    onDismiss()
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

@Composable
fun ShareDialog(
    zekrTitle: String,
    count: Long,
    onDismiss: () -> Unit,
    onCopy: () -> Unit,
    usePersian: Boolean
) {
    val countStr = NumberUtil.formatNumber(count, usePersian)
    val shareText = "امروز $countStr مرتبه ذکر «$zekrTitle» را خواندم 🌿\nاللهم صل علی محمد و آل محمد\n«هر ذکر، یک آرامش؛ هر صلوات، یک نور»"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("اشتراک‌گذاری ذکر", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(shareText, style = MaterialTheme.colorScheme.onPrimaryContainer.let { MaterialTheme.typography.bodyLarge })
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onCopy()
                    onDismiss()
                }
            ) {
                Text("کپی متن")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("بستن")
            }
        }
    )
}
