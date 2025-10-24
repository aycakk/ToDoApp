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
import com.google.android.material.snackbar.Snackbar

class TasksAdapter(
    private val mContext: Context,
    private var taskList: List<Tasks>,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<TasksAdapter.TaskCardHolder>() {

    inner class TaskCardHolder(val binding: TaskcardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCardHolder {
        val binding: TaskcardBinding =
            DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.taskcard, parent, false)
        return TaskCardHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskCardHolder, position: Int) {
        val task = taskList[position]
        val t = holder.binding
        t.taskobject = task

        // 🔹 Düzenleme ekranına git
        t.taskcard.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(Task = task)
            Navigation.findNavController(it).navigate(action)
        }

        // 🔹 Checkbox güncel durumunu göster
        t.checkBox.setOnCheckedChangeListener(null) // eski listener'ı temizle
        t.checkBox.isChecked = task.isCompleted

        // 🔹 Checkbox tıklanınca renk + veritabanı güncelle
        t.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val color = if (isChecked)
                ContextCompat.getColor(t.root.context, R.color.button)
            else
                ContextCompat.getColor(t.root.context, android.R.color.darker_gray)

            t.checkBox.buttonTintList = ColorStateList.valueOf(color)

            // ✅ Veritabanını güncelle (ViewModel fonksiyonunu çağır)
            viewModel.isChecked(task.id,isChecked)
        }
    }

    override fun getItemCount(): Int = taskList.size
}
