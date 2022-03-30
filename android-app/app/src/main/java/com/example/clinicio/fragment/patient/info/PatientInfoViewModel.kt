package com.example.clinicio.fragment.patient.info

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import com.example.clinicio.data.remote.patient.Patient
import com.example.clinicio.data.remote.patient.PatientExtras
import com.example.clinicio.data.remote.user.entity.User
import com.example.clinicio.fragment.doctor.info.DoctorInfoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PatientInfoViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun patientData() = flow {

        emit(Resource.loading(null))
        try {
            val response: APIResponse<Patient> = repository.patientData()
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<Patient>> = e.toResource(Patient::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(DoctorInfoViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }

    fun updateUser(user: User) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<User> = repository.updateUser(user)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<User>> = e.toResource(User::class.java)
            emit(resp)
        } catch (e: Exception) {
            emit(Resource.error(null, "Unknown Error"))
        }
    }

    fun upsertExtras(id: Int, extras: PatientExtras) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<PatientExtras> = repository.upsertPatientExtras(id, extras)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<PatientExtras>> = e.toResource(PatientExtras::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(DoctorInfoViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }
}