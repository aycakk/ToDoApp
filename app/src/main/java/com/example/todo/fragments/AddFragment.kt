package com.example.todo.fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.ViewModel.AddViewModel
import com.example.todo.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AddFragment : Fragment() {
private lateinit var databinding:FragmentAddBinding
private lateinit var viewModel: AddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding=DataBindingUtil.inflate(inflater,R.layout.fragment_add, container, false)

        databinding.addfragment=this
        databinding.lifecycleOwner = viewLifecycleOwner




        return databinding.root

    }
    /*val title = databinding.title.text.toString()
    val explain = databinding.explain.text.toString()
    val startTimeLong = databinding.editTextstartTime.text.toString().toLong()
    val endTimeLong = databinding.editTextendtime.text.toString().toLong()*/
    fun onCreateButtonClick(it:View) {


        try {
            val title = databinding.title.text.toString()
            val explain = databinding.explain.text.toString()

            // Zaman girişlerini kontrol etme ve dönüştürme
            val startTimeString = databinding.editTextstartTime.text.toString()
            val endTimeString = databinding.editTextendtime.text.toString()

            if (startTimeString.isEmpty() || endTimeString.isEmpty()) {
                Log.e("SaveTask", "Start Time veya End Time boş olamaz")
                return
            }

            val startTimeLong: Long
            val endTimeLong: Long

            try {
                startTimeLong = startTimeString.toLong()
                endTimeLong = endTimeString.toLong()
            } catch (e: NumberFormatException) {
                Log.e("SaveTask", "Geçersiz zaman formatı", e)
                return
            }

            viewModel.save(title, explain, startTimeLong, endTimeLong)
            Log.d("SaveTask", "onCreateButtonClick")
            Snackbar.make(it,"New task is created",Snackbar.LENGTH_SHORT).show()
            Navigation.findNavController(it).navigate(R.id.action_addFragment_to_homeFragment)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("SaveTask", "Bir hata oluştu", e)
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tepmviewmodel:AddViewModel by viewModels()
        viewModel=tepmviewmodel
    }






}