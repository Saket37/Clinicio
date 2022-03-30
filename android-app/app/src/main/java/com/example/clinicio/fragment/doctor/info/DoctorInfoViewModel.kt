package com.example.clinicio.fragment.doctor.info

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.doctor.Doctor
import com.example.clinicio.data.remote.doctor.DoctorExtras
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import com.example.clinicio.data.remote.user.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DoctorInfoViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun doctorData() = flow {

        emit(Resource.loading(null))
        try {
            val response: APIResponse<Doctor> = repository.doctorData()
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<Doctor>> = e.toResource(Doctor::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(DoctorInfoViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }

//    val mf: MutableStateFlow<Pair<Resource<APIResponse<User>>, Resource<APIResponse<DoctorExtras>>>?> =
//        MutableStateFlow(null)

    /*  @FlowPreview
       fun update(user: User, extras: DoctorExtras) = flowOf(
           updateUser(user),
           upsertExtras(user.id, extras)
       ).flattenMerge()*/

    /*  fun update(user: User, extras: DoctorExtras) =
          updateUser(user).combine(upsertExtras(user.id, extras)) { r1, r2 ->
              Pair(r1, r2)
          }*/


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

    fun upsertExtras(id: Int, extras: DoctorExtras) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<DoctorExtras> = repository.upsertDoctorExtras(id, extras)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<DoctorExtras>> = e.toResource(DoctorExtras::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(DoctorInfoViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, "Unknown Error"))
        }
    }
}