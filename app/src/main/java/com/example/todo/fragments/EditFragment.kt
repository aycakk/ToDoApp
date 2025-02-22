package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.todo.R
import androidx.navigation.fragment.navArgs
import com.example.todo.databinding.FragmentEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=DataBindingUtil.inflate(inflater,R.layout.fragment_edit, container, false)
        binding.editfragment=this
        binding.lifecycleOwner=viewLifecycleOwner
        val  bundle:EditFragmentArgs by navArgs()
        val toTask=bundle.Task
        binding.task=toTask

        return binding.root
    }

}
