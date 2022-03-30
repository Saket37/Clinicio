package com.example.clinicio.fragment.patient.appointment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.doctor.DoctorList
import com.example.clinicio.databinding.ItemDoctorListBinding
import javax.inject.Inject

class DoctorListAdapter @Inject constructor() : RecyclerView.Adapter<DoctorListHolder>() {
    var doctorListResult: List<DoctorList> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var bookButton: ((id: Int) -> Unit) = fun(_: Int) {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDoctorListBinding.inflate(inflater, parent, false)
        return DoctorListHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorListHolder, position: Int) {
        with(doctorListResult[position]) {
            holder.doctorListData(this)
            holder.binding.bookButton.setOnClickListener {
                bookButton(this.id)
            }
        }
    }

    override fun getItemCount() = doctorListResult.size
}