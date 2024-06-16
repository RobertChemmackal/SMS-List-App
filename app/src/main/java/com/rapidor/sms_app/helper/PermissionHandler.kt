package com.rapidor.sms_app.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionHandler(var context:Context) {
    fun isSmsPermissionsGranted(context: Context): Boolean {
        val sendSmsPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        )
        val readSmsPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        )
        val readContact = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        )
        return sendSmsPermission == PackageManager.PERMISSION_GRANTED &&
                readSmsPermission == PackageManager.PERMISSION_GRANTED&&
                readContact == PackageManager.PERMISSION_GRANTED
    }


}