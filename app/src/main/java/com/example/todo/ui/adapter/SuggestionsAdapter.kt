package com.example.todo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.entity.SuggestedTask
import com.example.todo.databinding.TaskcardBinding

class SuggestionsAdapter(
    private val list: List<SuggestedTask>,
    private val onClick: (SuggestedTask) -> Unit
) : RecyclerView.Adapter<SuggestionsAdapter.SugHolder>() {

    inner class SugHolder(val binding: TaskcardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugHolder {
        val binding = TaskcardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SugHolder(binding)
    }

    override fun onBindViewHolder(holder: SugHolder, position: Int) {
        val item = list[position]
        val t = holder.binding

        // ✔ Başlık ve açıklama
        t.taskTitle.text= item.title
        t.taskDescription.text = item.explain



        t.checkBox.visibility = View.GONE
        t.taskTime.visibility=View.GONE
        t.taskDate.visibility= View.GONE



        // ✔ Kart tıklanınca göreve dönüşsün
        t.taskcard.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount() = list.size
}
