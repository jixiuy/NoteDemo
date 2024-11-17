package com.example.node_swift.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ColorPicker(selectedColor: Color, onColorChange: (Color) -> Unit) {
    var showColorDialog by remember { mutableStateOf(false) }

    Column {
        // 显示当前选中的颜色
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(selectedColor, CircleShape)
                .clickable { showColorDialog = true }
                .padding(8.dp)
        )

        // 颜色选择对话框
        if (showColorDialog) {
            AlertDialog(
                onDismissRequest = { showColorDialog = false },
                title = { Text("选择颜色") },
                text = {
                    Column {
                        ColorOptions { color ->
                            onColorChange(color)
                            showColorDialog = false
                        }
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showColorDialog = false }) {
                            Text("确定")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ColorOptions(onColorSelected: (Color) -> Unit) {
    val colors = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta
    )

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, CircleShape)
                    .clickable { onColorSelected(color) }
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
        }
    }
}
