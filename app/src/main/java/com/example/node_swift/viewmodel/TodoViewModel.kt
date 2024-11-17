package com.example.node_swift.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.node_swift.brodcast.AlarmReceiver
import com.example.node_swift.dao.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.litepal.LitePal
import java.util.Calendar


class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val db: SQLiteDatabase

    private val _allTodos = MutableLiveData<List<TodoItem>>()
    val allTodos: LiveData<List<TodoItem>> = _allTodos

    private val _addTodoEvent = MutableLiveData<Unit>()
    val addTodoEvent: LiveData<Unit> = _addTodoEvent


    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        db = LitePal.getDatabase()
        loadTodos()
    }

    // 加载或更新数据
    fun loadTodos() {
        _allTodos.value = LitePal.findAll(TodoItem::class.java)
    }

    // 在添加、更新、删除操作后调用此方法
    fun refreshTodos() {
        loadTodos()
    }

    fun insert(title:String,date:Long,time:String,color:Int,isCompleted:Boolean) = viewModelScope.launch {
        val todo = TodoItem()
        todo.title = title
        todo.date = date
        todo.time = time
        todo.color = color
        todo.isCompleted = isCompleted
        todo.save()
        _addTodoEvent.value = Unit // 触发添加事件
        scheduleAlarm(todo) // 设置闹钟
        refreshTodos() // 更新所有待办事项
    }

    fun delete(todo: TodoItem) = viewModelScope.launch {
        // 根据ID删除记录
        LitePal.delete(TodoItem::class.java, todo.id)
        cancelAlarm(todo)
        refreshTodos() // 更新所有待办事项
    }

    fun update(todo: TodoItem) = viewModelScope.launch {
        // 保存更新后的todo
        todo.saveOrUpdate("id = ?", todo.id.toString())
        refreshTodos() // 更新所有待办事项
    }

    // 点击条目时切换完成状态
    fun toggleTodoCompletion(todo: TodoItem) = viewModelScope.launch {
        val updatedTodo = TodoItem()
        updatedTodo.id =todo.id
        updatedTodo.title = todo.title
        updatedTodo.date = todo.date
        updatedTodo.time = todo.time
        updatedTodo.color = todo.color
        updatedTodo.isCompleted = !todo.isCompleted
        update(updatedTodo)
    }

    // 设置闹钟
    private fun scheduleAlarm(todo: TodoItem) {
        // 获取当前时间
        val currentTimeMillis = System.currentTimeMillis()

        // 解析时间字符串
        val timeParts = todo.time.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        // 创建Calendar对象来设置闹钟时间
        val calendar = Calendar.getInstance().apply {
            timeInMillis = todo.date
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 确保闹钟时间晚于当前时间
        val alarmTime = calendar.timeInMillis

        if (alarmTime > currentTimeMillis) {
            val pendingIntent = createPendingIntent(todo.id)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
            }
        }
    }
    // 创建PendingIntent
    private fun createPendingIntent(todoId: Long): PendingIntent {
        val intent = Intent(getApplication(), AlarmReceiver::class.java).apply {
            action = "com.example.todo.ACTION_ALARM"
            putExtra("todoId", todoId)
        }
        return PendingIntent.getBroadcast(getApplication(), todoId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
    // 取消闹钟
    private fun cancelAlarm(todo: TodoItem) {
        val pendingIntent = createPendingIntent(todo.id)
        alarmManager.cancel(pendingIntent)
    }

    private var _currentWeek = MutableStateFlow(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR))
    val currentWeek: StateFlow<Int> = _currentWeek

    // 新增：获取当前周的开始和结束时间
    private fun getWeekRange(weekOfYear: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.WEEK_OF_YEAR, weekOfYear)
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_WEEK, 6)
        val end = cal.timeInMillis
        return Pair(start, end)
    }

    fun changeWeek(delta: Int) {
        _currentWeek.value = _currentWeek.value?.plus(delta) ?: Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
    }


}
