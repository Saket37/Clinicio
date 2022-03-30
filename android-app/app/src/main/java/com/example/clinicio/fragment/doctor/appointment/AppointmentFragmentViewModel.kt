package com.example.clinicio.fragment.doctor.appointment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.doctor.Appointment
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AppointmentFragmentViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    fun appointmentList(id: Int) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<List<Appointment>> = repository.appointmentList(id)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<List<Appointment>>> =
                e.toResource(Appointment::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(AppointmentFragmentViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }
}