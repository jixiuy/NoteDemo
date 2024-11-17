package com.example.node_swift.view

import android.app.DatePickerDialog
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun DatePicker(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val context = LocalContext.current
    var datePickerDialog by remember { mutableStateOf<DatePickerDialog?>(null) }
    var pickedDate by remember { mutableStateOf(selectedDate) }

    // 创建DatePickerDialog
    if (datePickerDialog == null) {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth) // 月份从0开始
            pickedDate = selectedDate
            onDateChange(selectedDate)
        }

        datePickerDialog = DatePickerDialog(
            context,
            dateListener,
            selectedDate.year,
            selectedDate.monthValue - 1, // 月份从0开始
            selectedDate.dayOfMonth
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 显示日期选择器
        Button(onClick = {
            datePickerDialog?.show()
        }) {
            Text("选择日期")
        }

        Text(
            text = pickedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.padding(8.dp)
        )

    }

}