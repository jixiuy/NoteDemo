package com.example.node_swift.brodcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.node_swift.dao.TodoItem
import com.example.node_swift.utils.ToastUtils
import com.example.note_swift.R
import org.litepal.LitePal

class AlarmReceiver : BroadcastReceiver() {
    var todoId : Long = 0
    override fun onReceive(context: Context, intent: Intent) {
        todoId = intent.getLongExtra("todoId", -1)
        if (todoId != -1L) {
            val todo = LitePal.find(TodoItem::class.java, todoId)
            if (todo != null) {
                // 处理闹钟逻辑，例如显示通知等
                showNotification(context, todo.title)
            }
        }
    }

    private fun showNotification(context: Context, title: String) {
        // 创建和显示通知
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, "todo_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Todo提醒")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
        context.ToastUtils("任务时间到了")
        notificationManager.notify(todoId.toInt(), notificationBuilder.build())
    }
}
