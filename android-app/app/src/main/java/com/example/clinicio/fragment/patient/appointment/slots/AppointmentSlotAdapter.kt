package com.example.clinicio.fragment.patient.appointment.slots

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicio.data.remote.slot.Slot
import com.example.clinicio.data.remote.slot.Status
import com.example.clinicio.databinding.ItemAppointmentSlotBinding
import javax.inject.Inject

class AppointmentSlotAdapter @Inject constructor() : RecyclerView.Adapter<BookSlotHolder>() {
    var slotListResult: List<Slot> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("Slot List", slotListResult.toString())
        }

    var timeSlotCard: ((id: Int) -> Unit) = fun(_: Int) {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSlotHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppointmentSlotBinding.inflate(inflater, parent, false)
        return BookSlotHolder(binding)
    }

    override fun onBindViewHolder(holder: BookSlotHolder, position: Int) {
        with(slotListResult[position]) {
            holder.slotList(this)
            if (this.status == Status.AVAILABLE) {
                holder.binding.slotCard.setOnClickListener {
                    Log.d("SlotId", this.id.toString())
                    timeSlotCard(this.id)

                }

            }
            if (this.status == Status.BOOKED) {
                holder.binding.slotCard.setOnClickListener {
                    return@setOnClickListener
                }
            }
        }
    }

    override fun getItemCount() = slotListResult.size
}