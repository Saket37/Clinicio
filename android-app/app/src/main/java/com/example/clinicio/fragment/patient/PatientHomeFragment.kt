package com.example.clinicio.fragment.patient

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
import androidx.navigation.fragment.findNavController
import com.example.clinicio.R
import com.example.clinicio.auth.LogoutViewModel
import com.example.clinicio.data.remote.Status
import com.example.clinicio.databinding.FragmentPatientHomeBinding
import com.example.clinicio.utils.LoginHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PatientHomeFragment : Fragment() {
    private var _binding: FragmentPatientHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var progressBar: ProgressBar

    private val viewModel: LogoutViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    @Inject
    lateinit var loginHelper: LoginHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPatientHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        binding.infoPatientCV.setOnClickListener {
            val action =
                PatientHomeFragmentDirections.actionPatientHomeFragmentToPatientInfoFragment()
            findNavController().navigate(action)
        }
        binding.bookAppointmentCV.setOnClickListener {
            val action =
                PatientHomeFragmentDirections.actionPatientHomeFragmentToBookAppointmentFragment()
            findNavController().navigate(action)
        }
        binding.appointmentHistoryCV.setOnClickListener {
            val action =
                PatientHomeFragmentDirections.actionPatientHomeFragmentToAppointmentHistoryFragment()
            findNavController().navigate(action)
        }
        binding.upcomingAppointmentCV.setOnClickListener {
            val action =
                PatientHomeFragmentDirections.actionPatientHomeFragmentToUpcomingAppointmentFragment()
            findNavController().navigate(action)
        }
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            viewModel.logoutUser().collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.success == true) {
                            loginHelper.userLoggedOut()
                            Toast.makeText(context, "Logged out Successfully", Toast.LENGTH_SHORT)
                                .show()
                            val action =
                                PatientHomeFragmentDirections.actionPatientHomeFragmentToLoginAsFragment()
                            findNavController().navigate(action)
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
                        // TODO
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}