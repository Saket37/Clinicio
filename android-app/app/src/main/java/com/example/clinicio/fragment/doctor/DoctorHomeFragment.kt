package com.example.clinicio.fragment.doctor

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
import com.example.clinicio.databinding.FragmentDoctorHomeBinding
import com.example.clinicio.utils.LoginHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class DoctorHomeFragment : Fragment() {
    private var _binding: FragmentDoctorHomeBinding? = null
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
        _binding = FragmentDoctorHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        binding.infoDoctorCV.setOnClickListener {
            val action = DoctorHomeFragmentDirections.actionDoctorHomeFragmentToDoctorInfoFragment()
            findNavController().navigate(action)
        }
        binding.appointmentCV.setOnClickListener {
            val action =
                DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAppointmentFragment()
            findNavController().navigate(action)
        }
        binding.availabilityCV.setOnClickListener {
            val action = DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAddSlotsFragment()
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
                                DoctorHomeFragmentDirections.actionDoctorHomeFragmentToLoginAsFragment()
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
                        //TODO
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