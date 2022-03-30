package com.example.clinicio.fragment.doctor.appointment

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
import com.example.clinicio.databinding.FragmentAppointmentBinding
import com.example.clinicio.utils.LoginHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!
    lateinit var progressBar: ProgressBar

    private val viewModel: AppointmentFragmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    @Inject
    lateinit var appointmentFragmentAdapter: AppointmentFragmentAdapter

    @Inject
    lateinit var loginHelper: LoginHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar



        binding.appointmentRV.apply {
            adapter = appointmentFragmentAdapter
        }

        lifecycleScope.launch {
            viewModel.appointmentList(loginHelper.id).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {

                        if (it.data?.data != null) {
                            progressBar.visibility = View.GONE
                            binding.appointmentRV.visibility = View.VISIBLE
                            binding.noDataFoundConstraint.visibility = View.GONE
                            appointmentFragmentAdapter.appointmentListResult = it.data.data

                        }
                    }
                    Status.ERROR -> {
                        val error = it.message.toString()
                        Log.e("Error", error)
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        binding.appointmentRV.visibility = View.GONE
                        binding.noDataFoundConstraint.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        progressBar.isIndeterminate = true
                    }
                    Status.APIERROR -> {
                        if (it.code == 404) {
                            progressBar.visibility = View.GONE
                            binding.appointmentRV.visibility = View.GONE
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