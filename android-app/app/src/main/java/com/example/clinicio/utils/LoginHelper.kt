package com.example.clinicio.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.clinicio.data.SHARED_PREF_FILE
import com.example.clinicio.data.SHARED_PREF_TOKEN_KEY
import com.example.clinicio.data.SHARED_PREF_USER_ID
import com.example.clinicio.data.SHARED_PREF_USER_ROLE
import com.example.clinicio.data.remote.user.entity.Role
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LoginHelper @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
    val token: String?
        get() = sharedPref.getString(SHARED_PREF_TOKEN_KEY, "")
    val id: Int
        get() = sharedPref.getInt(SHARED_PREF_USER_ID, 0)
    val role: String?
        get() = sharedPref.getString(
            SHARED_PREF_USER_ROLE, "")

    /**
     * checks whether token, role and id is present in Shared Preference.
     */
    fun isUserLoggedIn(): Boolean {
        if (token.isNullOrEmpty() || role.isNullOrEmpty() || id == 0) {
            return false
        }
        return true
    }

    /**
     * checks whether role is DOCTOR
     */
    fun isDoctor(): Boolean {
        if (role == Role.DOCTOR.toString()) {
            return true
        }
        return false
    }

    /**
     * checks whether role is PATIENT
     */
    fun isPatient(): Boolean {
        if (role == Role.PATIENT.toString()) {
            return true
        }
        return false
    }

    /**
     * saves the Login info(token, id, role) of user in Shared Preference when user Logs In
     */
    fun saveLoginInfo(token: String, id: Int, role: Role) {
        with(sharedPref.edit()) {
            putString(SHARED_PREF_TOKEN_KEY, token)
            putInt(SHARED_PREF_USER_ID, id)
            putString(SHARED_PREF_USER_ROLE, role.toString())
            apply()
        }
    }

    /**
     * deletes token, id, role of user in Shared Preference when user Logs out
     */
    fun userLoggedOut() {
        with(sharedPref.edit()) {
            remove(SHARED_PREF_TOKEN_KEY)
            remove(SHARED_PREF_USER_ID)
            remove(SHARED_PREF_USER_ROLE)
            apply()
        }
    }
}