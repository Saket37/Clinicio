package com.example.clinicio.fragment.patient.upcomingAppointment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import com.example.clinicio.data.remote.patient.UpcomingAppointment
import com.example.clinicio.utils.LoginHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class UpcomingAppointmentViewModel @Inject constructor(
    val repository: Repository,
    val loginHelper: LoginHelper,
) : ViewModel() {

    val upcomingAppointmentFlow: Flow<Resource<APIResponse<List<UpcomingAppointment>>>> =
        upcomingAppointment(loginHelper.id)

    init {
        upcomingAppointmentFlow
    }


    fun upcomingAppointment(id: Int) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<List<UpcomingAppointment>> =
                repository.upcomingAppointment(id)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<List<UpcomingAppointment>>> =
                e.toResource(UpcomingAppointment::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(UpcomingAppointmentViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }

    fun cancelAppointment(id: Int, slotId: Int) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<Map<String, String>> =
                repository.cancelAppointment(id, slotId)
            upcomingAppointmentFlow

            emit(Resource.success(response))
// TODO catch for APIERROR
        } catch (e: Exception) {
            Resource.error(null, "Unknown error")
        }

    }
}