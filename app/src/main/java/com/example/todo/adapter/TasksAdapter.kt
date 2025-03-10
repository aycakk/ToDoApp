package com.example.todo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.ViewModel.HomeViewModel
import com.example.todo.data.entity.Tasks
import com.example.todo.databinding.TaskcardBinding
import com.example.todo.fragments.HomeFragmentDirections
import com.google.android.material.snackbar.Snackbar


class TasksAdapter(var mcontext: Context,var tasklist:List<Tasks> ,var viewModel: HomeViewModel):RecyclerView.Adapter<TasksAdapter.taskcardholder>() {
    inner class taskcardholder(var design:TaskcardBinding):RecyclerView.ViewHolder(design.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):taskcardholder {
        val binding:TaskcardBinding=
            inflate(LayoutInflater.from(mcontext),R.layout.taskcard ,parent,false)
        return taskcardholder(binding)
    }


    override fun onBindViewHolder(holder: taskcardholder, position: Int) {
        val Task=tasklist.get(position)
        val t=holder.design
        t.taskobject=Task

        t.taskcard.setOnClickListener {
            val action=HomeFragmentDirections.actionHomeFragmentToEditFragment(Task=Task)
            Navigation.findNavController(it).navigate(action)

        }




    }


    override fun getItemCount(): Int {
        return tasklist.size
    }

}