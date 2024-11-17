package com.example.node_swift

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.node_swift.view.AddTodoDialog
import com.example.node_swift.view.TodoList
import com.example.node_swift.view.fragment.TimelineFragment
import com.example.node_swift.viewmodel.TodoViewModel
import com.example.note_swift.R
import com.example.note_swift.databinding.FragmentContainerViewBinding

class MainActivity : FragmentActivity() {

    private lateinit var todoViewModel: TodoViewModel

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 用户已授予权限，可以进行设置闹钟的操作
        } else {
            // 用户拒绝了权限请求，你可能需要提示用户手动在设置中启用此权限
            Toast.makeText(this, "需要通知权限才能设置闹钟", Toast.LENGTH_LONG).show()
            openAppSettings()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestAlarmPermission()
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        setContent {
            MyApp(todoViewModel)
        }
    }

    private fun checkAndRequestAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(viewModel: TodoViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var currentView by remember { mutableStateOf("Todo") }
    val activity = LocalContext.current as? FragmentActivity

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (currentView) {
                        "Todo" ->  Text(text = stringResource(id = R.string.toDO))
                        "Timeline" ->  Text(text = stringResource(id = R.string.weekView))
                    }


                        },
                actions = {
                    IconButton(onClick = {
                        currentView = if (currentView == "Todo") "Timeline" else "Todo"
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "Switch to Timeline"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            when (currentView) {
                "Todo" -> {
                    FloatingActionButton(onClick = { showDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Todo")
                    }
                }
            }

        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (showDialog) {
                AddTodoDialog(viewModel, onDismiss = { showDialog = false })
            }
            when (currentView) {
                "Todo" -> TodoList(viewModel)
                "Timeline" ->   AndroidViewBinding(FragmentContainerViewBinding::inflate) {
                    val myFragment = fragmentContainerView.getFragment<TimelineFragment>()
                }

            }
        }
    }


}

