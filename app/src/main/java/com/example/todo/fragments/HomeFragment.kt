package com.example.todo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.todo.adapter.TasksAdapter
import com.example.todo.R
import com.example.todo.R.id.action_homeFragment_to_addFragment
import com.example.todo.ViewModel.AddViewModel
import com.example.todo.ViewModel.HomeViewModel
import com.example.todo.databinding.HomeFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
private lateinit var binding:HomeFragmentBinding
private lateinit var viewModel: HomeViewModel
private lateinit var auth:FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)
        binding.homeobject=this
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.tasklist.observe(viewLifecycleOwner){
            val taskadapter=TasksAdapter(requireContext(),it,viewModel)
            binding.taskadapter=taskadapter

        }
        onResume()
        





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
        auth = Firebase.auth
    }



    override fun onResume() {
        super.onResume()
    }

}