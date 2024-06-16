package com.rapidor.sms_app.viewmodel

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Telephony
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rapidor.sms_app.models.SmsList


class SmsFetchViewModel : ViewModel() {
    private val smsList = mutableListOf<SmsList>()
    val mSmsList = MutableLiveData<ArrayList<SmsList>>()

    //method to fetch all sms from Inbox//
    fun fetchSmsFromPhyscialDevice(mContext: Context, searchKey: String) {
        val filter = searchKey
        val contentResolver: ContentResolver = mContext.contentResolver

        val cursor: Cursor? = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            "address LIKE ?",
             arrayOf("%$filter%"),
            Telephony.Sms.DATE + " DESC"
        )

        smsList.clear()
        cursor?.use {
            val idIndex = it.getColumnIndex(Telephony.Sms._ID)
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)
            while (it.moveToNext()) {
                var sms:SmsList
                val id = it.getLong(idIndex)
                val address = it.getString(addressIndex)
                val body = it.getString(bodyIndex)
                val date = it.getLong(dateIndex)
                sms = SmsList(id, address, body, date)
                smsList.add(sms)
            }
            mSmsList.value = smsList as ArrayList<SmsList>
        }
    }
    @SuppressLint("Range")
    fun getContactName(phoneNumber: String, context:Context): String? {
        val lookupUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val cursor = context.contentResolver.query(lookupUri, projection, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            cursor.close()
            return name
        }
        return null
    }}