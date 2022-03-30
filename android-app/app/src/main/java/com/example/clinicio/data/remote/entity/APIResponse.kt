package com.example.clinicio.data.remote.entity

import com.example.clinicio.data.remote.Resource
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.HttpException
import java.lang.reflect.Type

data class APIResponse <out T> (
    val success: Boolean,
    val data: T?,
    var error: APIError?
)

fun <T> HttpException.toResource(dataType: Type): Resource<APIResponse<T>> {
    val error = this.response()?.errorBody()
    val type = Types.newParameterizedType(APIResponse::class.java, dataType)
    val adapter: JsonAdapter<APIResponse<T>> = Moshi.Builder().build().adapter(type)
    return Resource.apiError(adapter.fromJson(error!!.source()), this.code())
}