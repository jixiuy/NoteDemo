package com.example.node_swift.view.fragment

import android.app.AlertDialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.node_swift.dao.TodoItem
import com.example.node_swift.viewmodel.TodoViewModel
import com.example.note_swift.R
import org.litepal.LitePal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class TimelineFragment : Fragment() {

    private lateinit var viewModel: TodoViewModel
    private lateinit var weekTextView: TextView
    private lateinit var backButton: ImageView
    private lateinit var forwardButton: ImageView
    private lateinit var currentWeek: TextView

    var todoList: List<TodoItem> = listOf()
    val dateMap = mutableMapOf<Pair<Long, String>, Triple<Int, Int, Float>>()

    // 全局变量 dayViews
    private lateinit var dayViews: Map<Int, View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timeline, container, false)
        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        todoList = LitePal.findAll(TodoItem::class.java)
        // 初始化视图
        weekTextView = view.findViewById(R.id.textView)
        backButton = view.findViewById(R.id.back)
        forwardButton = view.findViewById(R.id.forward)
        currentWeek = view.findViewById(R.id.current_week)

        dayViews = mapOf(
            1 to view.findViewById<View>(R.id.monday_view),
            2 to view.findViewById<View>(R.id.tuesday_view),
            3 to view.findViewById<View>(R.id.wednesday_view),
            4 to view.findViewById<View>(R.id.thursday_view),
            5 to view.findViewById<View>(R.id.friday_view),
            6 to view.findViewById<View>(R.id.saturday_view),
            7 to view.findViewById<View>(R.id.sunday_view)
        )

        dealWithDate()
        setupObservers()
        setupListeners()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        dealWithView()

    }

    val currentCircles = mutableListOf<View>()

    val weekMap = mutableMapOf<Int, MutableList<Triple<TodoItem, Int, Float>>>() // 存储结果

    private fun dealWithDate() {
        val startDate = LocalDate.of(2024, 1, 1) // 起始日期


        val todoList = LitePal.findAll(TodoItem::class.java)
        todoList.forEach { todoItem ->
            val itemDate =
                Instant.ofEpochMilli(todoItem.date).atZone(ZoneId.systemDefault()).toLocalDate()

            // 计算周次
            val weekNumber = ChronoUnit.WEEKS.between(startDate, itemDate).toInt()

            // 计算星期几
            val dayOfWeek = itemDate.dayOfWeek.value

            // 计算一天中的百分比
            val timeParts = todoItem.time.split(":").map { it.toInt() }
            val hour = timeParts[0]
            val minute = timeParts[1]
            val timePercentage = (hour * 60 + minute) / (24f * 60f)

            // 将数据存入 map
            val todoData = Triple(todoItem, dayOfWeek, timePercentage)
            weekMap.computeIfAbsent(weekNumber) { mutableListOf() }.add(todoData)

        }
    }

    private fun setupObservers() {
        currentWeek.text = "当前第 ${viewModel.currentWeek.value} 周"
        lifecycleScope.launchWhenStarted {
            viewModel.currentWeek.collect { week ->
                weekTextView.text = "第 $week 周"

                // 提取对应周次的数据
                val weekData = weekMap[week] ?: emptyList()

                // 清除之前绘制的所有圆圈
                clearWeekCircles()

                // 遍历周数据，在直线 View 上绘制小圆圈
                weekData.forEach { (todo, dayOfWeek, percentage) ->
                    // 获取目标 View
                    val targetView = dayViews[dayOfWeek] ?: return@forEach

                    // 延迟添加圆圈，确保布局完成
                    targetView.post {
                        // 创建小圆圈
                        val circle = View(context).apply {
                            layoutParams = ConstraintLayout.LayoutParams(40, 40) // 圆圈大小
                            background = GradientDrawable().apply {
                                shape = GradientDrawable.OVAL
                                setColor(todo.color) // 使用任务颜色
                            }
                            // 设置点击事件
                            setOnClickListener {
                                showTodoDialog(todo)
                            }
                        }

                        // 将小圆圈添加到父视图中
                        (targetView.parent as? ConstraintLayout)?.addView(circle)
                        val targetViewPosition = IntArray(2)
                        targetView.getLocationOnScreen(targetViewPosition)
                        val parentView = targetView.parent as View
                        val parentViewPosition = IntArray(2)
                        parentView.getLocationOnScreen(parentViewPosition)
                        val targetViewPositionInParent = IntArray(2)
                        targetViewPositionInParent[0] =
                            targetViewPosition[0] - parentViewPosition[0] // X轴偏移
                        targetViewPositionInParent[1] =
                            targetViewPosition[1] - parentViewPosition[1] // Y轴偏移

                        val circleY =
                            targetViewPositionInParent[1] + targetView.height * percentage - circle.layoutParams.height / 2
                        val circleX =
                            targetViewPosition[0] + targetView.width / 2 - circle.layoutParams.width / 2 // X轴居中

                        // 设置圆圈的位置
                        circle.translationX = circleX.toFloat()
                        circle.translationY = circleY.toFloat()

                        // 将圆圈添加到 currentCircles 中
                        currentCircles.add(circle)
                    }
                }
            }
        }
    }


    // 清除所有当前周的圆圈
    private fun clearWeekCircles() {
        // 清除之前添加的所有圆圈
        currentCircles.forEach { circle ->
            (circle.parent as? ConstraintLayout)?.removeView(circle)
        }
        // 清空 currentCircles 列表
        currentCircles.clear()
    }

    private fun showTodoDialog(todo: TodoItem) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("任务详情")
            .setMessage(
                """
            标题: ${todo.title}
            时间: ${todo.time}
            完成状态: ${if (todo.isCompleted) "已完成" else "未完成"}
        """.trimIndent()
            )
            .setPositiveButton("确定", null)
            .create()
        dialog.show()
    }

    // 定义用于显示数据的方法
    private fun displayWeekData(weekData: List<Triple<TodoItem, Int, Float>>) {
        if (weekData.isEmpty()) {
            println("本周无数据")
            return
        }

        weekData.forEach { (todo, dayOfWeek, percentage) ->
            println("任务: ${todo.title}, 星期几: $dayOfWeek, 一天百分比: $percentage")
            // 如果是 RecyclerView 或其他控件，替换为适配器更新逻辑
            // exampleAdapter.submitList(weekData)
        }
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            viewModel.changeWeek(-1)
        }
        forwardButton.setOnClickListener {
            viewModel.changeWeek(1)
        }
    }


}
