package com.example.node_swift.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.node_swift.dao.TodoItem
import com.example.node_swift.viewmodel.TodoViewModel
import com.example.note_swift.R


@Composable
fun TodoList(viewModel: TodoViewModel) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var todoToDelete by remember { mutableStateOf<TodoItem?>(null) }
    val todos by viewModel.allTodos.observeAsState(initial = emptyList<TodoItem>())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {

        // 使用 LazyColumn 显示待办事项列表
        LazyColumn {
            items(todos) { todo ->
                TodoItemScreen(
                    todo = todo,
                    onDelete = {
                        showDeleteDialog = true
                        todoToDelete = todo
                    },onToggleCompletion = { updatedTodo ->
                        viewModel.toggleTodoCompletion(updatedTodo)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }

    // 确认删除对话框
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = stringResource(id = R.string.confirmDelete)) },
            text = { Text(text = stringResource(id = R.string.isConfirmDeleted)) },
            confirmButton = {
                Button(
                    onClick = {
                        todoToDelete?.let { viewModel.delete(it) }
                        showDeleteDialog = false
                        todoToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = stringResource(id = R.string.delete), color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}
