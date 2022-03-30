package com.example.clinicio.fragment.patient.upcomingAppointment

import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.patient.UpcomingAppointment
import com.example.clinicio.databinding.ItemUpcomingAppointmentBinding

class UpcomingAppointmentHolder(val binding: ItemUpcomingAppointmentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun appointmentHistoryData(upcomingAppointment: UpcomingAppointment) {
        binding.displayDoctorNameTV.text = upcomingAppointment.name
        binding.displaySpecialityTV.text = upcomingAppointment.speciality
        binding.displayProblemTv.text = upcomingAppointment.reason
        binding.appointmentTV.text = upcomingAppointment.start_time

    }
}