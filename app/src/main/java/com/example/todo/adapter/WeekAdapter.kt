package com.example.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.ItemWeekDayBinding
import java.text.SimpleDateFormat
import java.util.*

class WeekAdapter(
    private val days: List<Calendar>,
    private val onDateSelected: (Calendar) -> Unit
) : RecyclerView.Adapter<WeekAdapter.DayViewHolder>() {

    private var selectedPosition = -1

    inner class DayViewHolder(val binding: ItemWeekDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemWeekDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val calendar = days[position]
        val dayNumber = SimpleDateFormat("d", Locale.getDefault()).format(calendar.time)
        val dayName = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)

        with(holder.binding) {
            tvDayNumber.text = dayNumber
            tvDayName.text = dayName

            if (position == selectedPosition) {
                itemContainer.setBackgroundResource(R.drawable.bg_day_unselected)
                tvDayNumber.setTextColor(ContextCompat.getColor(root.context, android.R.color.white))
                tvDayName.setTextColor(ContextCompat.getColor(root.context, android.R.color.white))
            } else {
                itemContainer.setBackgroundResource(R.drawable.bg_day_unselected)
                tvDayNumber.setTextColor(ContextCompat.getColor(root.context, android.R.color.black))
                tvDayName.setTextColor(ContextCompat.getColor(root.context, android.R.color.black))
            }

            root.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)
                onDateSelected(calendar)
            }
        }
    }

    override fun getItemCount(): Int = days.size
}
