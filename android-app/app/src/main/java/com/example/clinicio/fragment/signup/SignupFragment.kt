package com.example.clinicio.fragment.signup

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
import com.example.clinicio.data.remote.Status
import com.example.clinicio.data.remote.entity.APIError
import com.example.clinicio.data.remote.user.entity.User
import com.example.clinicio.databinding.FragmentSignupBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    /*private val passwordRegex: Regex =
        Regex("/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$/")*/
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private val args: SignupFragmentArgs by navArgs()

    private var allowSignup: Boolean = true;
    lateinit var progressBar: ProgressBar

    private lateinit var fieldMap: Map<String, TextInputLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        fieldMap = mapOf(
            Pair("name", binding.nameTextField),
            Pair("password", binding.userPasswordTextField),
            Pair("email", binding.emailTextField),
            Pair("phone", binding.userPhoneNumberField),
            Pair("address", binding.userAddressField)
        )

        binding.signUpButton.setOnClickListener {
            resetErrors()
            signUpData()
        }
    }

    private fun signUpData() {
        val name = binding.nameInputText.text.toString().trim()
        val email = binding.emailInputText.text.toString().trim()
        val password = binding.passwordInputText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordInputText.text.toString().trim()
        val phone = binding.phoneInputText.text.toString().trim()
        val address = binding.addressEditText.text.toString().trim()


        validateField(binding.nameTextField, name, ::isEmptyOrBlank, "Name")
        validateField(binding.emailTextField, email, ::isEmptyOrBlank, "Email")
        validateField(binding.userPhoneNumberField, phone, ::isEmptyOrBlank, "Phone Number")
        validateField(binding.userAddressField, address, ::isEmptyOrBlank, "Address")

        validatePassword(password, confirmPassword)


        if (!allowSignup) {
            return
        }

        val user = User(
            name = name,
            email = email,
            password = password,
            phone = phone,
            address = address,
            role = args.role
        );


        lifecycleScope.launch {
            viewModel.createUser(user).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data != null) {
                            Log.d("Post", it.toString())

                            Toast.makeText(requireContext(),
                                "Account created successfully, continue to Login",
                                Toast.LENGTH_SHORT).show()
                            val action =
                                SignupFragmentDirections.actionSignupFragmentToLoginFragment(args.role)
                            findNavController().navigate(action)

                        }
                    }
                    Status.ERROR -> {
                        val error = it.message.toString()
                        Log.e("Error", error)
                        Toast.makeText(requireContext(),
                            " Some Error occurred, Please try again",
                            Toast.LENGTH_SHORT).show()

                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        progressBar.isIndeterminate = true

                    }
                    Status.APIERROR -> {
                        if (it.code == 422) {
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


    private fun validatePassword(password: String, confirmPassword: String) {
        if (password.isEmpty()) {
            binding.userPasswordTextField.error = "Password must be filled"
            allowSignup = false
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.userConfirmPasswordTextField.error = "Confirm Password must be filled"
            allowSignup = false
            return
        }

        /*if (!password.contains(passwordRegex)) {
            binding.userPasswordTextField.error =
                "Password must contain min 8 character with uppercase, lowercase, symbol and digit"
            allowSignup = false
            return
        }*/

        if (password != confirmPassword) {
            binding.userPasswordTextField.error = "Passwords should match"
            binding.userConfirmPasswordTextField.error = "Passwords should match"
            allowSignup = false
            return
        }


    }

    private fun <T> validateField(
        field: TextInputLayout,
        data: T,
        inValid: (t: T) -> Boolean,
        fieldName: String,
    ) {
        if (inValid(data)) {
            field.error = "$fieldName must be filled"
            allowSignup = false

            return
        }


    }

    private fun resetErrors() {
        allowSignup = true
        binding.nameTextField.error = null
        binding.emailTextField.error = null
        binding.userPasswordTextField.error = null
        binding.userConfirmPasswordTextField.error = null
        binding.userPhoneNumberField.error = null
        binding.userAddressField.error = null
    }

    private fun isEmptyOrBlank(string: String): Boolean {
        return string.isBlank() || string.isEmpty()
    }

    private fun showErrorToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}