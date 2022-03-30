package com.example.clinicio.hilt

import com.example.clinicio.data.remote.APIService
import com.example.clinicio.data.remote.Constants
import com.example.clinicio.utils.LoginHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
    }


    @Provides
    fun provideAPIService(client: OkHttpClient, moshi: Moshi): APIService {
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(APIService::class.java)
    }

    @Provides
    fun provideHTTPClient(loginHelper: LoginHelper): OkHttpClient {
        // Interceptor to enable logging in Debug
        val logInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (com.example.clinicio.BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val tokenInterceptor = Interceptor {
            val request = it.request().newBuilder()
                .addHeader("Authorization", "Bearer ${loginHelper.token}")
                .build()
            it.proceed(request)
        }


        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(logInterceptor)
            addInterceptor(tokenInterceptor)
            connectTimeout(2, TimeUnit.MINUTES)
            readTimeout(2, TimeUnit.MINUTES)
            writeTimeout(4, TimeUnit.MINUTES)
        }

        return httpClient.build()
    }
}