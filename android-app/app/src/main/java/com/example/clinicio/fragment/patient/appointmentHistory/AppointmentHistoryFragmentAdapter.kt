package com.example.clinicio.fragment.patient.appointmentHistory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.patient.AppointmentHistory
import com.example.clinicio.databinding.ItemAppointmentHistoryBinding
import javax.inject.Inject

class AppointmentHistoryFragmentAdapter @Inject constructor() :
    RecyclerView.Adapter<AppointmentHistoryFragmentHolder>() {
    var appointmentHistoryList: List<AppointmentHistory> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AppointmentHistoryFragmentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppointmentHistoryBinding.inflate(inflater, parent, false)
        return AppointmentHistoryFragmentHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentHistoryFragmentHolder, position: Int) {
        with(appointmentHistoryList[position]) {
            holder.appointmentHistoryData(this)
        }
    }

    override fun getItemCount() = appointmentHistoryList.size
}