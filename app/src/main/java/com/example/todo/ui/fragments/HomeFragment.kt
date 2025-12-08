package com.example.todo.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todo.ui.adapter.TasksAdapter
import com.example.todo.R

import com.example.todo.ViewModel.AddViewModel
import com.example.todo.ViewModel.HomeViewModel
import com.example.todo.data.entity.Tasks

import com.example.todo.ui.adapter.SuggestionsAdapter
import com.example.todo.databinding.HomeFragmentBinding
import com.example.todo.utils.JsonHelper
import com.example.todo.worker.SyncWorker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
private lateinit var binding: HomeFragmentBinding
private lateinit var viewModel: HomeViewModel
private lateinit var addviewModel: AddViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }


        binding=DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)
        binding.homeobject=this
        binding.lifecycleOwner = viewLifecycleOwner

        with(binding,){
            with(viewModel){
                suggestButton.setOnClickListener {
                    showSuggestionsSheet()


                }
                sync.setOnClickListener { syncTasks()
                    Toast.makeText(requireContext(), "Sync başlatıldı!", Toast.LENGTH_SHORT).show()
                    loading()


                }

                progress.observe(viewLifecycleOwner) { percent ->
                    progressText.text = "$percent% is completed"
                    progressBar.progress = percent
                }

                calculateProgress(tasklist.value ?: emptyList())

                tasklist.observe(viewLifecycleOwner){
                    val taskadapter=TasksAdapter(requireContext(),it,viewModel)
                    binding.taskadapter=taskadapter
                }
            }

        }


        println("ASSETS: " + requireContext().assets.list("")?.toList())










        return binding.root
    }

   fun onAddTaskButtonClick(it:View){
    Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addFragment)
    Log.e("gecis", "gecis ", )
}
    private fun showSuggestionsSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottomsheet_suggestions, null)
        dialog.setContentView(view)

        val recycler = view.findViewById<RecyclerView>(R.id.suggestionsRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val suggestions = JsonHelper.loadSuggestions(requireContext())

        val adapter = SuggestionsAdapter(suggestions) { item ->

            val now = System.currentTimeMillis()



            dialog.dismiss()
        }

        recycler.adapter = adapter
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tepmviewmodel: HomeViewModel by viewModels()
        viewModel=tepmviewmodel
        val addtempviewmodel: AddViewModel by viewModels()
        addviewModel=addtempviewmodel

    }


    override fun onResume() {
        super.onResume()
    }

}