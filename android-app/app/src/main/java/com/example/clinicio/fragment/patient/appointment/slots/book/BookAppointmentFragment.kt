package com.example.clinicio.fragment.patient.appointment.slots.book

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.clinicio.data.remote.Status
import com.example.clinicio.data.remote.entity.APIError
import com.example.clinicio.data.remote.slot.BookAppointment
import com.example.clinicio.databinding.FragmentBookAppointmentBinding
import com.example.clinicio.utils.LoginHelper
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookAppointmentFragment : Fragment() {
    private var _binding: FragmentBookAppointmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookAppointmentViewModel by viewModels()
    lateinit var progressBar: ProgressBar
    val args: BookAppointmentFragmentArgs by navArgs()
    private var allowBooking: Boolean = true;
    private lateinit var fieldMap: Map<String, TextInputLayout>


    @Inject
    lateinit var loginHelper: LoginHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        fieldMap = mapOf(
            Pair("name", binding.reasonTextField)
        )

        binding.bookButton.setOnClickListener {
            resetErrors()
            bookAppointment()
        }

        lifecycleScope.launch {
            viewModel.fetchSlotInfo().collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.data != null) {
                            binding.doctorNameTV.text = it.data.data[0].name
                            binding.appointmentDateTimeTV.text = it.data.data[0].start_time
                            progressBar.visibility = View.GONE
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
                        showErrorToast(requireContext(), it.data?.error?.message!!)

                    }
                }
            }
        }
    }

    private fun bookAppointment() {
        val reason = binding.reasonInputField.text.toString().trim()
        val note = binding.noteInputField.text.toString().trim()

        validateField(binding.reasonTextField, reason, ::isEmptyOrBlank, "Reason")


        if (!allowBooking) {
            return
        }

        val bookAppointment = BookAppointment(
            slotId = args.slotID,
            bookedId = loginHelper.id,
            reason = reason,
            note = note
        )

        lifecycleScope.launch {
            viewModel.bookAppointment(loginHelper.id, bookAppointment).collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.data != null) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(),
                                "Booked Slot successfully",
                                Toast.LENGTH_SHORT).show()
                            val action =
                                BookAppointmentFragmentDirections.actionBookAppointmentFragmentToPatientHomeFragment2()
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
                        when (it.code) {
                            422 -> {
                                handleError(it.data?.error!!)
                            }
                            404 -> {
                                showErrorToast(requireContext(), it.data?.error?.message!!)
                                val action =
                                    BookAppointmentFragmentDirections.actionBookAppointmentFragmentToPatientInfoFragment()
                                findNavController().navigate(action)
                            }
                            else -> {
                                showErrorToast(requireContext(), it.data?.error?.message!!)
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO handle Errors
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
            allowBooking = false

            return
        }


    }

    private fun resetErrors() {
        allowBooking = true
        binding.reasonInputField.error = null

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