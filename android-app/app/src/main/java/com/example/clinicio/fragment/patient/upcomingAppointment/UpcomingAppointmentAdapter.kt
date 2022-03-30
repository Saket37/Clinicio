package com.example.clinicio.fragment.patient.upcomingAppointment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.patient.UpcomingAppointment
import com.example.clinicio.databinding.ItemUpcomingAppointmentBinding
import javax.inject.Inject

class UpcomingAppointmentAdapter @Inject constructor() :
    RecyclerView.Adapter<UpcomingAppointmentHolder>() {
    var upcomingAppointmentList: ArrayList<UpcomingAppointment> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingAppointmentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUpcomingAppointmentBinding.inflate(inflater, parent, false)
        return UpcomingAppointmentHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingAppointmentHolder, position: Int) {
        with(upcomingAppointmentList[position]) {
            holder.appointmentHistoryData(this)
        }
    }

    override fun getItemCount() = upcomingAppointmentList.size
}