package com.example.clinicio.fragment.doctor.appointment

import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.doctor.Appointment
import com.example.clinicio.databinding.ItemAppointmentBinding

class AppointmentFragmentHolder(val binding: ItemAppointmentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun appointmentList(appointment: Appointment) {
        binding.nameTV.text = appointment.name
        binding.displayAddressTV.text = appointment.address
        binding.displayContactTV.text = appointment.phone
        binding.displayAppointmentTimeTV.text = appointment.start_time
        binding.displayBloodGroupTV.text = appointment.blood_group
        binding.displayWeightTV.text = appointment.weight.toString()
        binding.displayProblemTv.text = appointment.reason
    }
}