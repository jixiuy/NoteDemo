package com.example.node_swift.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.node_swift.dao.TodoItem
import com.example.node_swift.utils.DateConverter
import com.example.note_swift.R


@Composable
fun TodoItemScreen(todo: TodoItem, onDelete: () -> Unit, onToggleCompletion: (TodoItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                // 点击事件，切换完成状态
                onToggleCompletion(todo)
            },
        shape = RoundedCornerShape(8.dp),

        ) {
        Column {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Color(todo.color))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {

                    Row {

                        Text(

                            text = DateConverter.convertToDateString(todo.date),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Card(
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(8.dp)
                            )
                        ) {
                            Text(
                                text = "${
                                    if (todo.isCompleted) stringResource(id = R.string.finish) else stringResource(
                                        id = R.string.notFinish
                                    )
                                }",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                    }

                    Text(
                        text = todo.time,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = todo.title,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,

                        )

                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Todo", tint = Color.Red)
                }
            }
        }

    }
}