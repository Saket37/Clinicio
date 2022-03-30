package com.example.clinicio.fragment.patient.appointment.slots.book

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import com.example.clinicio.data.remote.slot.BookAppointment
import com.example.clinicio.data.remote.slot.SlotInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class BookAppointmentViewModel @Inject constructor(
    val repository: Repository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = BookAppointmentFragmentArgs.fromSavedStateHandle(savedStateHandle)


    fun fetchSlotInfo() = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<List<SlotInfo>> = repository.fetchSlotInfo(args.slotID)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<List<SlotInfo>>> = e.toResource(SlotInfo::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(BookAppointmentViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }

    fun bookAppointment(id: Int, bookAppointment: BookAppointment) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<BookAppointment> =
                repository.bookAppointment(id, bookAppointment)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<BookAppointment>> =
                e.toResource(BookAppointment::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(BookAppointmentViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }
}