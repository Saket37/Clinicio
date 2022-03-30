package com.example.clinicio.fragment.doctor.appointment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.doctor.Appointment
import com.example.clinicio.databinding.ItemAppointmentBinding
import javax.inject.Inject

class AppointmentFragmentAdapter @Inject constructor() :
    RecyclerView.Adapter<AppointmentFragmentHolder>() {

    var appointmentListResult: List<Appointment> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("Slot List", appointmentListResult.toString())
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentFragmentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppointmentBinding.inflate(inflater, parent, false)
        return AppointmentFragmentHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentFragmentHolder, position: Int) {
        with(appointmentListResult[position]) {
            holder.appointmentList(this)
        }
    }

    override fun getItemCount() = appointmentListResult.size
}