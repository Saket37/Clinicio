package com.example.clinicio.fragment.patient.appointmentHistory

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import com.example.clinicio.data.remote.patient.AppointmentHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AppointmentHistoryFragmentViewModel @Inject constructor(val repository: Repository) :
    ViewModel() {
    fun fetchAppointmentHistory(id: Int) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<List<AppointmentHistory>> =
                repository.patientAppointmentHistory(id)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<List<AppointmentHistory>>> =
                e.toResource(AppointmentHistory::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(AppointmentHistoryFragmentViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }
}