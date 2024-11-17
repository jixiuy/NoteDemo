package com.example.node_swift.service

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.node_swift.dao.TodoItem
import com.example.note_swift.R
import org.litepal.LitePal

class AlarmWorker(appContext: Context, params: WorkerParameters) : Worker(appContext, params) {
    var todoId : Int = 0
    override fun doWork(): Result {
        todoId = inputData.getInt("todoId", -1)
        if (todoId != -1) {
            val todo = LitePal.find(TodoItem::class.java, todoId.toLong())
            if (todo != null) {
                // 这里处理闹钟逻辑，例如显示通知等
                showNotification(todo.title)
                return Result.success()
            }
        }
        return Result.failure()
    }

    private fun showNotification(title: String) {
        // 创建和显示通知
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "todo_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Todo提醒")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)

        notificationManager.notify(todoId, notificationBuilder.build())
    }
}
