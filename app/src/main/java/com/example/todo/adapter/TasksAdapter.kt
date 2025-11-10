package com.example.todo.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.ViewModel.HomeViewModel
import com.example.todo.data.entity.Tasks
import com.example.todo.databinding.TaskcardBinding
import com.example.todo.fragments.HomeFragmentDirections
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class TasksAdapter(
    private val mContext: Context,
    private var taskList: List<Tasks>,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<TasksAdapter.TaskCardHolder>() {

    // Zaman ve tarih formatlayıcılar
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    inner class TaskCardHolder(val binding: TaskcardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCardHolder {
        val binding: TaskcardBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.taskcard,
                parent,
                false
            )
        return TaskCardHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskCardHolder, position: Int) {
        val task = taskList[position]
        val t = holder.binding
        t.taskobject = task

        // 🔹 Tarih – Saat formatlama
        // startdate ve end_date saniye cinsinden, Date için milisaniyeye çevirelim
        val startText = if (task.startdate != null && task.startdate > 0L) {
            timeFormat.format(Date(TimeUnit.SECONDS.toMillis(task.startdate)))
        } else {
            "--:--"
        }

        val endText = if (task.end_date != null && task.end_date > 0L) {
            timeFormat.format(Date(TimeUnit.SECONDS.toMillis(task.end_date)))
        } else {
            "--:--"
        }

        t.taskTime.text = "$startText - $endText"

        val dateText = if (task.date != null && task.date > 0L) {
            dateFormat.format(Date(task.date))
        } else {
            ""
        }
        t.taskDate.text = dateText

        // 🔹 Kart tıklanınca EditFragment'a git
        t.taskcard.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(Task = task)
            Navigation.findNavController(it).navigate(action)
        }

        // 🔹 Checkbox state'ini önce temizle, sonra güncel durumu ata
        t.checkBox.setOnCheckedChangeListener(null)
        t.checkBox.isChecked = task.isCompleted

        // Başlangıçta checkbox rengi (tamamlanmışsa pembe, değilse gri)
        val initialColor = if (task.isCompleted)
            ContextCompat.getColor(t.root.context, R.color.button)
        else
            ContextCompat.getColor(t.root.context, android.R.color.darker_gray)

        t.checkBox.buttonTintList = ColorStateList.valueOf(initialColor)

        // 🔹 Checkbox tıklanınca renk + veritabanı güncelle
        t.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val color = if (isChecked)
                ContextCompat.getColor(t.root.context, R.color.button)
            else
                ContextCompat.getColor(t.root.context, android.R.color.darker_gray)

            t.checkBox.buttonTintList = ColorStateList.valueOf(color)

            // ✅ Veritabanını güncelle (ViewModel fonksiyonunu çağır)
            viewModel.isChecked(task.id, isChecked)
        }
    }

    override fun getItemCount(): Int = taskList.size
}
