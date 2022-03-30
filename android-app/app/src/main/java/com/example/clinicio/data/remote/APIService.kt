package com.example.clinicio.data.remote

import com.example.clinicio.data.remote.availability.Availability
import com.example.clinicio.data.remote.availability.AvailabilityDate
import com.example.clinicio.data.remote.availability.AvailabilityTime
import com.example.clinicio.data.remote.doctor.Appointment
import com.example.clinicio.data.remote.doctor.Doctor
import com.example.clinicio.data.remote.doctor.DoctorExtras
import com.example.clinicio.data.remote.doctor.DoctorList
import com.example.clinicio.data.remote.entity.APIResponse
import com.example.clinicio.data.remote.patient.AppointmentHistory
import com.example.clinicio.data.remote.patient.Patient
import com.example.clinicio.data.remote.patient.PatientExtras
import com.example.clinicio.data.remote.patient.UpcomingAppointment
import com.example.clinicio.data.remote.slot.BookAppointment
import com.example.clinicio.data.remote.slot.Slot
import com.example.clinicio.data.remote.slot.SlotInfo
import com.example.clinicio.data.remote.user.entity.Login
import com.example.clinicio.data.remote.user.entity.LoginResponse
import com.example.clinicio.data.remote.user.entity.User
import retrofit2.http.*


interface APIService {

    @POST("users")
    suspend fun createUser(
        @Body user: User,
    ): APIResponse<User>

    @POST("users/login")
    suspend fun login(
        @Body login: Login,
    ): APIResponse<LoginResponse>

    @POST("users/logout")
    suspend fun logout(): APIResponse<Map<String, String>>

    @GET("doctors/me")
    suspend fun doctorData(): APIResponse<Doctor>

    @GET("patients/me")
    suspend fun patientData(): APIResponse<Patient>


    @POST("doctors/{id}/extras")
    suspend fun upsertDoctorExtras(
        @Path("id") id: Int,
        @Body extras: DoctorExtras,
    ): APIResponse<DoctorExtras>

    @POST("patients/{id}/extras")
    suspend fun upsertPatientExtras(
        @Path("id") id: Int,
        @Body extras: PatientExtras,
    ): APIResponse<PatientExtras>

    @POST("users/{id}")
    suspend fun update(
        @Path("id") id: Int,
        @Body user: User,
    ): APIResponse<User>

    @POST("doctors/{id}/availabilities")
    suspend fun availability(
        @Path("id") id: Int,
        @Body availability: Availability,
    ): APIResponse<Availability>

    @GET("doctors")
    suspend fun getDoctorsList(): APIResponse<List<DoctorList>>

    @GET("doctors/{id}/availabilities/date")
    suspend fun getAvailabilityDate(
        @Path("id") id: Int,
    ): APIResponse<List<AvailabilityDate>>

    @GET("doctors/{id}/availabilities/time")
    suspend fun getAvailabilityTime(
        @Path("id") id: Int,
        @Query("date") date: String,
    ): APIResponse<List<AvailabilityTime>>

    @GET("doctors/{id}/slots")
    suspend fun getSlots(
        @Path("id") id: Int,
        @Query("date") date: String,
        @Query("time") time: String,
    ): APIResponse<List<Slot>>

    @GET("slots/{id}/info")
    suspend fun fetchSlotInfo(
        @Path("id") id: Int,
    ): APIResponse<List<SlotInfo>>

    @POST("patients/{id}/appointment")
    suspend fun bookAppointment(
        @Path("id") id: Int,
        @Body bookAppointment: BookAppointment,
    ): APIResponse<BookAppointment>

    @GET("doctor/{id}/appointments")
    suspend fun appointmentList(
        @Path("id") id: Int,
    ): APIResponse<List<Appointment>>

    @GET("patients/{id}/history")
    suspend fun patientAppointmentHistory(
        @Path("id") id: Int,
    ): APIResponse<List<AppointmentHistory>>

    @GET("patients/{id}/slot")
    suspend fun upcomingAppointment(
        @Path("id") id: Int,
    ): APIResponse<List<UpcomingAppointment>>

    @POST("patients/{id}/slot/{slot_id}")
    suspend fun cancelAppointment(
        @Path("id") id: Int,
        @Path("slot_id") slot_id: Int,
    ): APIResponse<Map<String, String>>
}