package com.rapidor.sms_app.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPreferenceManager(context: Context) {
    companion object {
        private const val PREF_NAME = "sms_pref"
    }

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
    fun setStringPref(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringPref(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: ""
    }

}