package com.example.clinicio.fragment.login

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.clinicio.R
import com.example.clinicio.auth.LoginViewModel
import com.example.clinicio.data.remote.Status
import com.example.clinicio.data.remote.entity.APIError
import com.example.clinicio.data.remote.user.entity.Login
import com.example.clinicio.data.remote.user.entity.Role
import com.example.clinicio.databinding.FragmentLoginBinding
import com.example.clinicio.utils.LoginHelper
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val args: LoginFragmentArgs by navArgs()
    lateinit var progressBar: ProgressBar

    @Inject
    lateinit var loginHelper: LoginHelper

    private var allowLogin: Boolean = true;
    private lateinit var fieldMap: Map<String, TextInputLayout>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        /*sharedPref =
            requireContext().getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)*/
        /**
         * checks the role passed from the Login As Fragment and set text of loginASTV to its respective role.
         */
        if (args.role == Role.DOCTOR) {
            binding.loginAsTV.setText(R.string.doctor_login)
        } else {
            binding.loginAsTV.setText(R.string.patient_login)
        }
        fieldMap = mapOf(
            Pair("password", binding.userPasswordTextField),
            Pair("email", binding.emailTextField),
        )

        /**
         * Click Listener on register here, when clicked it redirects to Signup Fragment.
         */
        binding.registerTV.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment(args.role)
            findNavController().navigate(action)
        }

        /**
         * Click Listener on login button, it resets errors and calls login function.
         */
        binding.loginButton.setOnClickListener {
            resetError()
            login()
        }
    }

    /**
     * Launches view model in lifecycle scope
     */
    fun login() {
        val email = binding.emailInputText.text.toString().trim()
        val password = binding.passwordInputText.text.toString().trim()
        val uniqueID: String = UUID.randomUUID().toString()

        validateEmail(email)
        validatePassword(password)
        if (!allowLogin) {
            return
        }
        val login = Login(email = email, password = password, device_name = uniqueID)
        lifecycleScope.launch {
            viewModel.login(login).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.data != null) {
                            val token = it.data.data.token
                            val id = it.data.data.user.id
                            val role = it.data.data.user.role
                            loginHelper.saveLoginInfo(token, id, role)

                            if (it.data.data.user.role == Role.DOCTOR) {
                                val action =
                                    LoginFragmentDirections.actionLoginFragmentToDoctorHomeFragment()
                                findNavController().navigate(action)
                            }
                            if (it.data.data.user.role == Role.PATIENT) {
                                val action =
                                    LoginFragmentDirections.actionLoginFragmentToPatientHomeFragment()
                                findNavController().navigate(action)
                            }
                        }
                    }
                    Status.ERROR -> {
                        val error = it.message.toString()
                        Log.e("Error", error)
                        if (it.message != null) {
                            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        progressBar.isIndeterminate = true
                    }
                    Status.APIERROR -> {
                        if (it.code == 422 || it.code == 401) {
                            handleError(it.data?.error!!)
                        } else {
                            showErrorToast(requireContext(), it.data?.error?.message!!)
                        }
                    }
                }
            }
        }
    }


    private fun handleError(error: APIError) {
        if (error.fields == null) {
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            return
        }

        for (e in error.fields) {
            e.forEach { (key, value) ->
                fieldMap[key]?.error = value
            }
        }

    }

    private fun validatePassword(password: String) {
        if (password.isEmpty() || password.isBlank()) {
            binding.userPasswordTextField.error = "Password must be filled"
            allowLogin = false
            return
        }
    }

    private fun validateEmail(password: String) {
        if (password.isEmpty() || password.isBlank()) {
            binding.emailTextField.error = "Email must be filled"
            allowLogin = false
            return
        }
    }


    private fun resetError() {
        allowLogin = true
        binding.emailTextField.error = null
        binding.userPasswordTextField.error = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showErrorToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}