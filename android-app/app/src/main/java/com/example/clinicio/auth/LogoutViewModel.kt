package com.example.clinicio.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.entity.APIResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LogoutViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun logoutUser() = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<Map<String, String>> = repository.logoutUser()
            Log.d("Success Logout View Model", response.toString())
            emit(Resource.success(response))
// TODO catch for APIERROR
        } catch (e: Exception) {
            Log.e("Error Logout View Model", e.message, e)
            Resource.error(null, "Unknown error")
        }

    }
}