package com.example.node_swift.view

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun TimePicker(selectedTime: LocalTime, onTimeChange: (LocalTime) -> Unit) {
    val context = LocalContext.current
    var timePickerDialog by remember { mutableStateOf<TimePickerDialog?>(null) }
    var pickedTime by remember { mutableStateOf(selectedTime) }

    // 创建TimePickerDialog
    if (timePickerDialog == null) {
        val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val selectedTime = LocalTime.of(hourOfDay, minute)
            pickedTime = selectedTime
            onTimeChange(selectedTime)
        }

        timePickerDialog = TimePickerDialog(
            context,
            timeListener,
            selectedTime.hour,
            selectedTime.minute,
            false // 设置为24小时格式
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 显示时间选择器
        Button(onClick = {
            timePickerDialog?.show()
        }) {
            Text("选择时间")
        }

        Text(
            text = pickedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.padding(8.dp)
        )

    }
}
