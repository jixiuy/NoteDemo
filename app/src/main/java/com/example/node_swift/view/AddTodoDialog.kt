package com.example.node_swift.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.node_swift.utils.DateConverter
import com.example.node_swift.viewmodel.TodoViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(viewModel: TodoViewModel, onDismiss: () -> Unit) {
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var time by remember {
        mutableStateOf(
            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        )
    }
    var detail by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Blue.toArgb()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加待办事项") },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                OutlinedTextField(
                    value = detail,
                    onValueChange = { detail = it},
                    label = { Text("待办事项", color = MaterialTheme.colorScheme.primary) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "List Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                DatePicker(
                    selectedDate = LocalDate.parse(date),
                    onDateChange = { newDate -> date = newDate.toString() }
                )
                Spacer(modifier = Modifier.height(5.dp))
                TimePicker(
                    selectedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")),
                    onTimeChange = { newTime ->
                        time = newTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                    }
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 显示时间选择器

                    Text(
                        text = "选择颜色",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                        modifier = Modifier.padding(8.dp)
                    )

                    ColorPicker(
                        selectedColor = Color(color),
                        onColorChange = { newColor -> color = newColor.toArgb() }
                    )

                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.insert(detail,DateConverter.convertToTimestamp(date),time,color,false)
                    onDismiss()
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

