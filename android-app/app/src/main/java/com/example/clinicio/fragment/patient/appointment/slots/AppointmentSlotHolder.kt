package com.example.clinicio.fragment.patient.appointment.slots

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.slot.Slot
import com.example.clinicio.data.remote.slot.Status
import com.example.clinicio.databinding.ItemAppointmentSlotBinding
import java.util.*

class BookSlotHolder(val binding: ItemAppointmentSlotBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun slotList(slot: Slot) {
        /*val time = outputDateFormat.format(slot.start_time).toString()
        Log.d("Slot Time", time)*/
        val value = slot.start_time.trim().split("\\s+".toRegex())
        val s = slot.start_time.substringAfter(" ");
        binding.timeSlots.text = s
        if (slot.status == Status.AVAILABLE) {
            binding.slotCard.setCardBackgroundColor(Color.GREEN)
        }
        if (slot.status == Status.BOOKED) {
            binding.slotCard.setCardBackgroundColor(Color.RED)
        }
    }
}

