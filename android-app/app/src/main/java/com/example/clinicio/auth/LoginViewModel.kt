package com.example.clinicio.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import com.example.clinicio.data.remote.user.entity.Login
import com.example.clinicio.data.remote.user.entity.LoginResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val moshi: Moshi,
) : ViewModel() {


    fun login(login: Login) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<LoginResponse> = repository.loginUser(login)
            Log.d("Success Login View Model", response.toString())
            emit(Resource.success(response))

        } catch (e: HttpException) {
            val resp: Resource<APIResponse<LoginResponse>> = e.toResource(LoginResponse::class.java)
            emit(resp)
        } catch (e: Exception) {

            Log.e("Error Login View Model", e.message, e)
            Resource.error(null, "Unknown error")
        }

    }
}