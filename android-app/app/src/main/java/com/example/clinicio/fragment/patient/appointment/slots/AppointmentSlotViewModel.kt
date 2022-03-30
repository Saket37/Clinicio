package com.example.clinicio.fragment.patient.appointment.slots

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.availability.AvailabilityDate
import com.example.clinicio.data.remote.availability.AvailabilityTime
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import com.example.clinicio.data.remote.slot.Slot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AppointmentSlotViewModel @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = AppointmentSlotFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val availabilityDateFlow: Flow<Resource<APIResponse<List<AvailabilityDate>>>> =
        fetchAvailableDates()

    private fun fetchAvailableDates() = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<List<AvailabilityDate>> =
                repository.getAvailabilityDate(args.doctorID)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<List<AvailabilityDate>>> =
                e.toResource(AvailabilityDate::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(AppointmentSlotViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }

    fun selectDate(date: String) {
        Log.d("select Date", date)
        viewModelScope.launch {
            fetchAvailableTime(date)
        }
        savedStateHandle.set("date", date)
    }

    private var _availabilityTimeFlow: MutableStateFlow<Resource<APIResponse<List<AvailabilityTime>?>>?> =
        MutableStateFlow(null)
    val availabilityTimeFlow: Flow<Resource<APIResponse<List<AvailabilityTime>?>>?>
        get() = _availabilityTimeFlow

    private suspend fun fetchAvailableTime(date: String) {
        _availabilityTimeFlow.value = Resource.loading(null)
        try {
            val response: APIResponse<List<AvailabilityTime>> =
                repository.getAvailabilityTime(args.doctorID, date)
            _availabilityTimeFlow.value = Resource.success(response)

        } catch (e: HttpException) {
            val resp: Resource<APIResponse<List<AvailabilityTime>>> =
                e.toResource(AvailabilityTime::class.java)
            _availabilityTimeFlow.value = resp

        } catch (e: Exception) {
            Log.e(AppointmentSlotViewModel::class.qualifiedName, e.message, e)
            _availabilityTimeFlow.value = Resource.error(null, e.message ?: "Unknown error")
        }
    }

    fun selectTime(time: String) {
        val date = savedStateHandle.get<String>("date")
        Log.d("date", date.toString())
        Log.d("time", time)
        if (date == null) {
            Log.e(this.javaClass.simpleName, "date is null. can't fetch slots")
            return
        }

        viewModelScope.launch {
            fetchSlots(date, time)
        }
    }

    private var _availabilitySlotFlow: MutableStateFlow<Resource<APIResponse<List<Slot>?>>?> =
        MutableStateFlow(null)

    val availabilitySlotFlow: Flow<Resource<APIResponse<List<Slot>?>>?>
        get() = _availabilitySlotFlow

    private suspend fun fetchSlots(date: String, time: String) {
        _availabilitySlotFlow.value = Resource.loading(null)
        try {
            val response =
                repository.getSlots(args.doctorID, date, time)
            _availabilitySlotFlow.value = Resource.success(response)
            Log.d("Slot Response", response.toString())

        } catch (e: HttpException) {
            val resp: Resource<APIResponse<List<Slot>>> =
                e.toResource(Slot::class.java)
            _availabilitySlotFlow.value = resp
        } catch (e: Exception) {
            Log.e(AppointmentSlotViewModel::class.qualifiedName, e.message, e)
            _availabilitySlotFlow.value = Resource.error(null, e.message ?: "Unknown error")
        }
    }
}