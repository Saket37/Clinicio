package com.example.clinicio.fragment.doctor.info

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
import com.example.clinicio.R
import com.example.clinicio.data.remote.Status
import com.example.clinicio.data.remote.doctor.Doctor
import com.example.clinicio.data.remote.doctor.DoctorExtras
import com.example.clinicio.data.remote.entity.APIError
import com.example.clinicio.data.remote.user.entity.Role
import com.example.clinicio.data.remote.user.entity.User
import com.example.clinicio.databinding.FragmentInfoDoctorBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoctorInfoFragment : Fragment() {
    private var _binding: FragmentInfoDoctorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorInfoViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private lateinit var progressBar: ProgressBar
    lateinit var doctor: Doctor
    private var allowUpdate: Boolean = true
    private lateinit var fieldMap: Map<String, TextInputLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        binding.saveInfoButton.setOnClickListener {
            resetErrors()
            update()
            /*val action = DoctorInfoFragmentDirections.actionDoctorInfoFragmentToDoctorHomeFragment()
            findNavController().navigate(action)*/

        }

        lifecycleScope.launch {
            viewModel.doctorData().collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.data != null) {
                            doctor = it.data.data
                            binding.nameInputText.setText(it.data.data.name)
                            binding.emailInputText.setText(it.data.data.email)
                            binding.addressEditText.setText(it.data.data.address)
                            binding.phoneInputText.setText(it.data.data.phone)
                            binding.specialityInputText.setText(it.data.data.doctor_extras?.speciality)
                            binding.degreeInputField.setText(it.data.data.doctor_extras?.degree)
                            progressBar.visibility = View.GONE
                        }
                    }
                    Status.ERROR -> {
                        val error = it.message.toString()
                        Log.e("Error", error)
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        progressBar.isIndeterminate = true

                    }
                    Status.APIERROR -> {

                        showErrorToast(requireContext(), it.data?.error?.message!!)

                    }
                }
            }

        }


    }


    private fun update() {
        val name = binding.nameInputText.text.toString().trim()
        val email = binding.emailInputText.text.toString().trim()
        val phone = binding.phoneInputText.text.toString().trim()
        val address = binding.addressEditText.text.toString().trim()
        val speciality = binding.specialityInputText.text.toString().trim()
        val degree = binding.degreeInputField.text.toString().trim()

        fieldMap = mapOf(
            Pair("name", binding.nameTextField),
            Pair("email", binding.emailTextField),
            Pair("phone", binding.contactField),
            Pair("address", binding.addressTextField),
            Pair("speciality", binding.specialityField),
            Pair("degree", binding.degreeField)
        )

        /*validateField(binding.nameTextField, name, ::isEmptyOrBlank, "Name")*/
        validateField(binding.emailTextField, email, ::isEmptyOrBlank, "Email")
        validateField(binding.contactField, phone, ::isEmptyOrBlank, "Phone Number")
        validateField(binding.addressTextField, address, ::isEmptyOrBlank, "Address")
        validateField(binding.specialityField, speciality, ::isEmptyOrBlank, "Speciality")
        validateField(binding.degreeField, degree, ::isEmptyOrBlank, "Degree")
        if (!allowUpdate) {
            return
        }

        val user = User(id = doctor.id,
            name = name,
            email = email,
            password = "",
            phone = phone,
            address = address,
            role = Role.DOCTOR)

        val extras = DoctorExtras(
            speciality = speciality,
            degree = degree,
            doctor_id = doctor.id,
        )
        /*  var userUpdate: Boolean = false
          var extrasUpdate: Boolean = false*/


        lifecycleScope.launch {
            /*viewModel.update(user, extras).collectLatest {
                Log.i(DoctorInfoFragment::class.qualifiedName, it.toString())
                when (it.status) {
                    Status.SUCCESS ->
                    Status.ERROR ->
                    Status.LOADING ->
                }
            }*/

            viewModel.updateUser(user).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data != null) {
                            progressBar.visibility = View.GONE
                            /*Toast.makeText(requireContext(),
                                "Record updated successfully",
                                Toast.LENGTH_SHORT).show()*/
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
                        if (it.code == 422 || it.code == 401) {
                            handleError(it.data?.error!!)
                        } else {
                            showErrorToast(requireContext(), it.data?.error?.message!!)
                        }
                    }
                }
            }
            viewModel.upsertExtras(doctor.id, extras).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.success!! && it.data.data == null) {
                            /*if (!it.data.success) {
                                // handleError(it.data.error!!)
                            }*/
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(),
                                "Record updated successfully",
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

    // TODO Handle other field errors
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

    private fun <T> validateField(
        field: TextInputLayout,
        data: T,
        inValid: (t: T) -> Boolean,
        fieldName: String,
    ) {
        if (inValid(data)) {
            field.error = "$fieldName must be filled"
            allowUpdate = false

            return
        }


    }


    private fun resetErrors() {
        allowUpdate = true
        binding.nameTextField.error = null
        binding.emailTextField.error = null
        binding.contactField.error = null
        binding.addressTextField.error = null
        binding.specialityField.error = null
        binding.degreeField.error = null
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