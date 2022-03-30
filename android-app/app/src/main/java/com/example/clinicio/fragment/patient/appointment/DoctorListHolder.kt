package com.example.clinicio.fragment.patient.appointment

import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.doctor.DoctorList
import com.example.clinicio.databinding.ItemDoctorListBinding

class DoctorListHolder(val binding: ItemDoctorListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun doctorListData(doctorList: DoctorList) {

        binding.doctorNameTV.text = doctorList.name
        binding.doctorDegreeTV.text = doctorList.degree
        binding.doctorSpecialityTV.text = doctorList.speciality

    }
}