package com.example.clinicio.fragment.patient.appointment.slots

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clinicio.R
import com.example.clinicio.data.remote.Status
import com.example.clinicio.databinding.FragmentAppointmentSlotBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentSlotFragment : Fragment() {

    private var _binding: FragmentAppointmentSlotBinding? = null
    private val binding get() = _binding!!

    private val args: AppointmentSlotFragmentArgs by navArgs()

    private val viewModel: AppointmentSlotViewModel by viewModels()
    lateinit var progressBar: ProgressBar

    @Inject
    lateinit var appointmentSlotAdapter: AppointmentSlotAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAppointmentSlotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        binding.appointmentSlotRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = appointmentSlotAdapter
        }
        appointmentSlotAdapter.timeSlotCard = fun(id: Int) {
            val action =
                AppointmentSlotFragmentDirections.actionAppointmentSlotFragmentToBookAppointmentFragment(
                    id)
            findNavController().navigate(action)
        }

        lifecycleScope.launch {
            viewModel.availabilityDateFlow.collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.data.isNullOrEmpty()) {
                            progressBar.visibility = View.GONE
                            binding.slotsCV.visibility = View.GONE
                            binding.noDataFoundConstraint.visibility = View.VISIBLE
                        }
                        if (it.data?.data != null) {

                            val list = it.data.data.map { ad -> ad.date }

                            val adapter =
                                ArrayAdapter(requireContext(), R.layout.list_item, list)
                            binding.dateSpinner.adapter = adapter
                            binding.dateSpinner.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        p0: AdapterView<*>?,
                                        p1: View?,
                                        p2: Int,
                                        p3: Long,
                                    ) {
                                        Log.d(AppointmentSlotFragment::class.qualifiedName,
                                            list[p2])
                                        viewModel.selectDate(list[p2])
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {

                                    }

                                }

                            progressBar.visibility = View.GONE
                            binding.slotsCV.visibility = View.VISIBLE
                            binding.noDataFoundConstraint.visibility = View.GONE
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
                            progressBar.visibility = View.GONE
                            binding.slotsCV.visibility = View.GONE
                            binding.noDataFoundConstraint.visibility = View.VISIBLE
                        } else {
                            showErrorToast(requireContext(), it.data?.error?.message!!)
                        }
                    }
                }
            }
            viewModel.availabilityTimeFlow.collectLatest {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            if (it.data?.data.isNullOrEmpty()) {
                                progressBar.visibility = View.GONE
                                binding.slotsCV.visibility = View.GONE
                                binding.noDataFoundConstraint.visibility = View.VISIBLE
                            }
                            if (it.data?.data != null) {
                                val list =
                                    it.data.data.map { at -> at.start_time/*"${at.start_time}-${at.end_time}"*/ }
                                val adapter =
                                    ArrayAdapter(requireContext(), R.layout.list_item, list)
                                binding.timeSpinner.adapter = adapter
                                binding.timeSpinner.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            p0: AdapterView<*>?,
                                            p1: View?,
                                            p2: Int,
                                            p3: Long,
                                        ) {
                                            viewModel.selectTime(list[p2])
                                        }

                                        override fun onNothingSelected(p0: AdapterView<*>?) {

                                        }

                                    }
                                progressBar.visibility = View.GONE
                                binding.slotsCV.visibility = View.VISIBLE
                                binding.noDataFoundConstraint.visibility = View.GONE
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
                                progressBar.visibility = View.GONE
                                binding.slotsCV.visibility = View.GONE
                                binding.noDataFoundConstraint.visibility = View.VISIBLE
                            } else {
                                showErrorToast(requireContext(), it.data?.error?.message!!)
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.availabilitySlotFlow.collectLatest {
                Log.d("view", it.toString())
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            if (it.data?.data != null) {
                                appointmentSlotAdapter.slotListResult = it.data.data
                                Log.d("slot ada", appointmentSlotAdapter.slotListResult.toString())
                                progressBar.visibility = View.GONE
                                binding.slotsCV.visibility = View.VISIBLE
                                binding.noDataFoundConstraint.visibility = View.GONE
                            }
                            if (it.data?.data.isNullOrEmpty()) {
                                progressBar.visibility = View.GONE
                                binding.slotsCV.visibility = View.GONE
                                binding.noDataFoundConstraint.visibility = View.VISIBLE
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
                                progressBar.visibility = View.GONE
                                binding.slotsCV.visibility = View.GONE
                                binding.noDataFoundConstraint.visibility = View.VISIBLE
                            } else {
                                showErrorToast(requireContext(), it.data?.error?.message!!)
                            }
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