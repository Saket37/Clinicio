package com.example.clinicio.data

import com.example.clinicio.data.remote.APIService
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
import java.util.*
import javax.inject.Inject

const val SHARED_PREF_TOKEN_KEY = "TOKEN"
const val SHARED_PREF_USER_ID = "USER ID"
const val SHARED_PREF_USER_ROLE = "Role"
const val SHARED_PREF_FILE = "com.example.clinicio.data.xml"

class Repository @Inject constructor(private val apiService: APIService) {

    suspend fun createUser(user: User): APIResponse<User> {
        return apiService.createUser(user)
    }

    suspend fun loginUser(login: Login): APIResponse<LoginResponse> {
        val uniqueID: String = UUID.randomUUID().toString()
        login.device_name = uniqueID
        return apiService.login(login)
    }

    suspend fun logoutUser(): APIResponse<Map<String, String>> {
        return apiService.logout()
    }

    suspend fun doctorData(): APIResponse<Doctor> {
        return apiService.doctorData()
    }

    suspend fun patientData(): APIResponse<Patient> {
        return apiService.patientData()
    }

    suspend fun updateUser(user: User): APIResponse<User> {
        return apiService.update(user.id, user)
    }

    suspend fun upsertDoctorExtras(id: Int, extras: DoctorExtras): APIResponse<DoctorExtras> {
        return apiService.upsertDoctorExtras(id, extras)
    }

    suspend fun upsertPatientExtras(id: Int, extras: PatientExtras): APIResponse<PatientExtras> {
        return apiService.upsertPatientExtras(id, extras)
    }

    suspend fun availability(id: Int, availability: Availability): APIResponse<Availability> {
        return apiService.availability(id, availability)
    }

    suspend fun getDoctorList(): APIResponse<List<DoctorList>> {
        return apiService.getDoctorsList()
    }

    suspend fun getAvailabilityDate(id: Int): APIResponse<List<AvailabilityDate>> {
        return apiService.getAvailabilityDate(id)
    }

    suspend fun getAvailabilityTime(id: Int, date: String): APIResponse<List<AvailabilityTime>> {
        return apiService.getAvailabilityTime(id, date)
    }

    suspend fun getSlots(id: Int, date: String, time: String): APIResponse<List<Slot>> {
        return apiService.getSlots(id, date, time)
    }

    suspend fun fetchSlotInfo(id: Int): APIResponse<List<SlotInfo>> {
        return apiService.fetchSlotInfo(id)
    }

    suspend fun bookAppointment(
        id: Int,
        bookAppointment: BookAppointment,
    ): APIResponse<BookAppointment> {
        return apiService.bookAppointment(id, bookAppointment)
    }

    suspend fun appointmentList(id: Int): APIResponse<List<Appointment>> {
        return apiService.appointmentList(id)
    }

    suspend fun patientAppointmentHistory(id: Int): APIResponse<List<AppointmentHistory>> {
        return apiService.patientAppointmentHistory(id)
    }

    suspend fun upcomingAppointment(id: Int): APIResponse<List<UpcomingAppointment>> {
        return apiService.upcomingAppointment(id)
    }

    suspend fun cancelAppointment(id: Int, slotId: Int): APIResponse<Map<String, String>> {
        return apiService.cancelAppointment(id, slotId)
    }

}