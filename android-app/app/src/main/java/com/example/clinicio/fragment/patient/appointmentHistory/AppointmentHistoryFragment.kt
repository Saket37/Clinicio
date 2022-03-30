package com.example.clinicio.fragment.patient.appointmentHistory

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.example.clinicio.R
import com.example.clinicio.data.remote.Status
import com.example.clinicio.databinding.FragmentAppointmentHistoryBinding
import com.example.clinicio.utils.LoginHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentHistoryFragment : Fragment() {
    private var _binding: FragmentAppointmentHistoryBinding? = null
    private val binding get() = _binding!!
    lateinit var progressBar: ProgressBar

    val viewModel: AppointmentHistoryFragmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    @Inject
    lateinit var loginHelper: LoginHelper

    @Inject
    lateinit var appointmentHistoryAdapter: AppointmentHistoryFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAppointmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        binding.appointmentHistoryRV.apply {
            adapter = appointmentHistoryAdapter
        }
        lifecycleScope.launch {
            viewModel.fetchAppointmentHistory(loginHelper.id).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {

                        if (it.data?.data != null) {
                            progressBar.visibility = View.GONE
                            binding.noDataFoundConstraint.visibility = View.GONE
                            binding.appointmentHistoryRV.visibility = View.VISIBLE
                            appointmentHistoryAdapter.appointmentHistoryList = it.data.data

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
                            binding.appointmentHistoryRV.visibility = View.GONE
                            binding.noDataFoundConstraint.visibility = View.VISIBLE
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