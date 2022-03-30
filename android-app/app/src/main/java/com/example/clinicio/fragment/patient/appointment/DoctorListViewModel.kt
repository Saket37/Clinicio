package com.example.clinicio.fragment.patient.appointment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.doctor.DoctorList
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DoctorListViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    fun getDoctorList() = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<List<DoctorList>> = repository.getDoctorList()
            emit(Resource.success(response))

        } catch (e: HttpException) {
            val resp: Resource<APIResponse<List<DoctorList>>> = e.toResource(DoctorList::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(DoctorListViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }
}