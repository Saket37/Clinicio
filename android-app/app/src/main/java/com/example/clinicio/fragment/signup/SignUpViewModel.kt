package com.example.clinicio.fragment.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.clinicio.data.Repository
import com.example.clinicio.data.remote.Resource
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.entity.toResource
import com.example.clinicio.data.remote.user.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun createUser(user: User) = flow {
        emit(Resource.loading(null))
        try {
            val response: APIResponse<User> = repository.createUser(user)
            Log.d("view Model", response.toString())
            emit(Resource.success(response, 201))
        } catch (e: HttpException) {
            val resp: Resource<APIResponse<User>> = e.toResource(User::class.java)
            emit(resp)
        } catch (e: Exception) {
            Log.e("View Model Error", e.message, e)
            emit(Resource.error(null, "Unknown error!!"))
        }
    }

}