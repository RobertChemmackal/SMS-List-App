package com.rapidor.sms_app.services

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rapidor.sms_app.helper.PrefKeys
import com.rapidor.sms_app.helper.SharedPreferenceManager
import com.rapidor.sms_app.models.SmsList

class SmsBroadCastService  : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BackGround","Running")
        getLatestSmsFromDevice(context)
    }
    fun getLatestSmsFromDevice(mContext: Context){
        val sharedPreferences=SharedPreferenceManager(mContext)
        val mobile=sharedPreferences.getStringPref(PrefKeys.KEY_MOBILE)
        val contentResolver: ContentResolver = mContext.contentResolver
        val smsList = mutableListOf<SmsList>()
        val mSmsList = MutableLiveData<ArrayList<SmsList>>()
        val cursor: Cursor? = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            Telephony.Sms.DATE + " DESC"
        )
        smsList.clear()
        cursor?.use {
            val idIndex = it.getColumnIndex(Telephony.Sms._ID)
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val address = it.getString(addressIndex)
                val body = it.getString(bodyIndex)
                val date = it.getLong(dateIndex)
                val sms = SmsList(id, address, body, date)
                smsList.add(sms)
                mSmsList.value=smsList as ArrayList<SmsList>
            }
        }
        try {
            val smsManager: SmsManager
            smsManager = mContext.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage("+91"+mobile, null, mSmsList.value?.get(0)?.body, null, null)
            Log.d("MobileNumber"+" ---> ", mobile)
        } catch (e: Exception) {
         Log.d("SmsFailedError"+" ---> ", e.message.toString())
        }

    }

}