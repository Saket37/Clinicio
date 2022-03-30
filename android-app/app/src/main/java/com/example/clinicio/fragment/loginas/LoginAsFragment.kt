package com.example.clinicio.fragment.loginas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinicio.data.remote.user.entity.Role
import com.example.clinicio.databinding.FragmentLoginAsBinding
import com.example.clinicio.utils.LoginHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginAsFragment : Fragment() {
    private var _binding: FragmentLoginAsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var loginHelper: LoginHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginAsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         *This if statement checks whether the user is logged in, else it returns to Login As Fragment.
         * if user is logged in, it checks role of the user and redirects it to the respective Home Screen.
         */
        if (loginHelper.isUserLoggedIn()) {
            if (loginHelper.isDoctor()) {
                val action = LoginAsFragmentDirections.actionLoginAsFragmentToDoctorHomeFragment()
                findNavController().navigate(action)
            }
            if (loginHelper.isPatient()) {
                val action = LoginAsFragmentDirections.actionLoginAsFragmentToPatientHomeFragment()
                findNavController().navigate(action)
            }
        }
        /**
         * when clicked on Continue as Doctor button is clicked DOCTOR is passed as role to the Login Fragment
         */
        binding.loginAsDoctorButton.setOnClickListener {
            val action = LoginAsFragmentDirections.actionLoginAsFragmentToLoginFragment(Role.DOCTOR)
            findNavController().navigate(action)
        }
        /**
         * when clicked on Continue as Patient button is clicked PATIENT is passed as role to the Login Fragment
         */
        binding.loginAsPatientButton.setOnClickListener {
            val action =
                LoginAsFragmentDirections.actionLoginAsFragmentToLoginFragment(Role.PATIENT)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}