package com.example.clinicio.fragment.doctor.slots

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.availability.Availability
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AddSlotsViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    fun availability(id: Int, availability: Availability) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<Availability> =
                repository.availability(id, availability)
            emit(Resource.success(response))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<Availability>> = e.toResource(Availability::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e(AddSlotsViewModel::class.qualifiedName, e.message, e)
            emit(Resource.error(null, e.message.toString()))
        }
    }
}