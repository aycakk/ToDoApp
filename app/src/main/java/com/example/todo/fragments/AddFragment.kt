package com.example.todo.fragments

import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.ViewModel.AddViewModel
import com.example.todo.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class AddFragment : Fragment() {
private lateinit var binding:FragmentAddBinding
private lateinit var viewModel: AddViewModel
private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_add, container, false)

        binding.addfragment=this
        binding.lifecycleOwner = viewLifecycleOwner


        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis.toString() // Timestamp olarak sakla
            Log.d("CalendarView", "Seçilen Tarih: $selectedDate")
        }

        binding.editTextstartTime.setOnClickListener {
            showTimePicker(binding.editTextstartTime)
        }

        binding.editTextendtime.setOnClickListener {
            showTimePicker(binding.editTextendtime)
        }



        return binding.root

    }


    private fun showTimePicker(editText: android.widget.EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(selectedTime)
            },
            hour,
            minute,
            true // 24 saat formatı
        )
        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCreateButtonClick(it:View) {
        try {

            Log.d("CalendarView", "Seçilen Tarih: $selectedDate")
            val title = binding.title.text.toString()
            val explain = binding.explain.text.toString()

            // Zaman girişlerini kontrol etme ve dönüştürme
            val startTimeString = binding.editTextstartTime.text.toString()
            val endTimeString = binding.editTextendtime.text.toString()

            if (startTimeString.isEmpty() || endTimeString.isEmpty()) {
                Log.e("SaveTask", "Start Time veya End Time boş olamaz")
                Snackbar.make(it, "Start Time or End Time is empty", Snackbar.LENGTH_SHORT).show()
                return
            }

            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
            val startTime = LocalTime.parse(startTimeString, timeFormatter)
            val endTime = LocalTime.parse(endTimeString, timeFormatter)


            if (startTime.isAfter(endTime)) {
                Snackbar.make(it, "Start time is after the end time", Snackbar.LENGTH_SHORT).show()
                return
            }

            val startTimeLong = startTime.toSecondOfDay().toLong()
            val endTimeLong = endTime.toSecondOfDay().toLong()

            if (selectedDate.isEmpty()) {
                Snackbar.make(it, "Please choose of date.", Snackbar.LENGTH_SHORT).show()
                return
            }


            viewModel.save(title, explain, startTimeLong, endTimeLong,selectedDate.toLong())
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