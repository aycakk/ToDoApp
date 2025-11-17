package com.example.todo.fragments

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todo.adapter.TasksAdapter
import com.example.todo.R
import com.example.todo.ReminderWorker
import com.example.todo.ViewModel.AddViewModel
import com.example.todo.ViewModel.HomeViewModel
import com.example.todo.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {
private lateinit var binding:HomeFragmentBinding
private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }

        binding=DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)
        binding.homeobject=this
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.progress.observe(viewLifecycleOwner) { percent ->

            binding.progressText.text = "$percent% is completed"
            binding.progressBar.progress = percent

        }
        viewModel.calculateProgress(viewModel.tasklist.value ?: emptyList())




        viewModel.tasklist.observe(viewLifecycleOwner){
            val taskadapter=TasksAdapter(requireContext(),it,viewModel)
            binding.taskadapter=taskadapter


        }
        onResume()
        // 🔔 Test WorkManager (5 saniye sonra bildirim)
        val data = workDataOf(
            "title" to "Test",
            "message" to "WorkManager doğrudan çağrıldı"
        )

        val work = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(work)

        return binding.root
    }

   fun onAddTaskButtonClick(it:View){
    Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addFragment)
    Log.e("gecis", "gecis ", )
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tepmviewmodel: HomeViewModel by viewModels()
        viewModel=tepmviewmodel

    }


    override fun onResume() {
        super.onResume()
    }

}