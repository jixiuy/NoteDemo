package com.example.node_swift.dao
import androidx.room.PrimaryKey
import org.litepal.crud.LitePalSupport

class TodoItem : LitePalSupport() {
    @PrimaryKey
    var id: Long = 0
    var title: String = ""
    var date: Long = 0L // 这里使用Long来存储时间戳
    var time: String = ""
    var color: Int = 0
    var isCompleted: Boolean = false
}
