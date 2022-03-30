package com.example.clinicio.fragment.patient.appointmentHistory

import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.patient.AppointmentHistory
import com.example.clinicio.databinding.ItemAppointmentHistoryBinding

class AppointmentHistoryFragmentHolder(val binding: ItemAppointmentHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun appointmentHistoryData(appointmentHistory: AppointmentHistory) {
        binding.displayDoctorNameTV.text = appointmentHistory.name
        binding.displaySpecialityTV.text = appointmentHistory.speciality
        binding.displayProblemTv.text = appointmentHistory.reason
        binding.appointmentTV.text = appointmentHistory.start_time

    }
}