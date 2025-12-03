package com.example.todo.ui.fragments

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.ViewModel.EditViewModel
import com.example.todo.databinding.FragmentEditBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding
    private lateinit var viewModel: EditViewModel
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        binding.editfragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        val bundle: EditFragmentArgs by navArgs()
        val toTask = bundle.Task
        binding.task = toTask

        Log.d("ZAMAN_KONTROL", "startDate: ${toTask.startDate}, endDate: ${toTask.endDate}")
        Log.d("ZAMAN_KONTROL", "date: ${toTask.date}")

        binding.editTextstartTime.setText(formatTime(toTask.startDate))
        binding.editTextendtime.setText(formatTime(toTask.endDate))
        binding.calendarView.setDate(toTask.date)

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis.toString()
            Log.d("CalendarView", "Seçilen Tarih: $selectedDate")
        }

        val navController = findNavController()
        binding.buttondelete.setOnClickListener {
            Snackbar.make(it, "${binding.task!!.title} is delete?", Snackbar.LENGTH_SHORT)
                .setAction("Yes") {
                    viewModel.delete(toTask.id)
                    navController.navigate(R.id.action_editFragment_to_homeFragment)
                }.show()
        }


        return binding.root
    }

    private fun formatTime(seconds: Long): String {
        if (seconds <= 0L) return "Bilinmiyor"
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(TimeUnit.SECONDS.toMillis(seconds)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: EditViewModel by viewModels()
        viewModel = tempViewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCreateeditClick(it: View) {
        try {
            Log.d("CalendarView", "Seçilen Tarih: $selectedDate")
            val title = binding.title.text.toString()
            val explain = binding.explain.text.toString()

            val startTimeString = binding.editTextstartTime.text.toString()
            val endTimeString = binding.editTextendtime.text.toString()

            if (startTimeString.isEmpty() || endTimeString.isEmpty()) {
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
                Snackbar.make(it, "Please choose a date.", Snackbar.LENGTH_SHORT).show()
                return
            }

            viewModel.update(
                binding.task!!.id,
                title,
                explain,
                startTimeLong,
                endTimeLong,
                selectedDate.toLong()
            )
            Snackbar.make(it, "Your task is edited.", Snackbar.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("SaveTask", "Bir hata oluştu", e)
        }
    }
}