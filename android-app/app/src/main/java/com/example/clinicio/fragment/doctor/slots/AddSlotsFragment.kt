package com.example.clinicio.fragment.doctor.slots

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.clinicio.R
import com.example.clinicio.data.remote.Status
import com.example.clinicio.data.remote.availability.Availability
import com.example.clinicio.databinding.FragmentAddSlotsBinding
import com.example.clinicio.utils.LoginHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddSlotsFragment : Fragment() {
    private var _binding: FragmentAddSlotsBinding? = null
    private val binding get() = _binding!!
    lateinit var day: String

    @Inject
    lateinit var loginHelper: LoginHelper
    private lateinit var progressBar: ProgressBar

    val viewModel: AddSlotsViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddSlotsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val interval = binding.intervalATV
        progressBar = binding.progressBar

        binding.calendar.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                // call back
                day = outputDayFormat.format(it).uppercase(Locale.getDefault())
                val text = outputDateFormat.format(it)
                binding.dateInput.setText(text)

            }
            datePicker.show((activity as FragmentActivity).supportFragmentManager,
                AddSlotsFragment::class.simpleName)
        }


        binding.startTimeIV.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Select Start time")
                    .build()
            picker.show((activity as FragmentActivity).supportFragmentManager,
                AddSlotsFragment::class.simpleName)
            picker.addOnPositiveButtonClickListener {
                // call back code
                val h = picker.hour.toString()
                val m = picker.minute.toString()
                val time = "$h:$m:00"
                binding.startTimeInput.setText(time)

            }
        }

        binding.endTimeIV.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Select End time")
                    .build()
            picker.show((activity as FragmentActivity).supportFragmentManager,
                AddSlotsFragment::class.simpleName)
            picker.addOnPositiveButtonClickListener {
                // call back code
                val h = picker.hour.toString()
                val m = picker.minute.toString()
                val time = "$h:$m:00"
                binding.endTimeInput.setText(time)
            }
        }
        val items = listOf(5, 10, 15, 20)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (interval as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.addButton.setOnClickListener {
            slots()
        }
    }

    private fun slots() {
        val date = binding.dateInput.text.toString().trim()
        val day = day
        val startTime = binding.startTimeInput.text.toString().trim()
        val endTime = binding.endTimeInput.text.toString().trim()
        val interval = binding.intervalATV.text.toString().trim().toInt()
        val docID = loginHelper.id
        val availability = Availability(
            date = date,
            day = day,
            start_time = startTime,
            end_time = endTime,
            interval = interval,
            doc_id = docID
        )

        lifecycleScope.launch {
            viewModel.availability(docID, availability).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.success == true) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(),
                                "Slots Added successfully",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    Status.ERROR -> {
                        val error = it.message.toString()
                        Log.e("Error", error)
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        progressBar.isIndeterminate = true
                    }
                    Status.APIERROR -> {
                        if (it.code == 404) {
                            showErrorToast(requireContext(), it.data?.error?.message!!)
                            val action =
                                AddSlotsFragmentDirections.actionAddSlotsFragmentToDoctorInfoFragment()
                            findNavController().navigate(action)
                        } else {
                            showErrorToast(requireContext(), it.data?.error?.message!!)
                        }

                    }
                }
            }
        }
    }

    private fun showErrorToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

@SuppressLint("ConstantLocale")
private val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

@SuppressLint("ConstantLocale")
private val outputDayFormat = SimpleDateFormat("EEE", Locale.getDefault())
