package com.rapidor.sms_app.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.widget.Toast
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

    @SuppressLint("MissingPermission")
     fun showAvailableSIMCards(context: Context) {
        val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        val subscriptionInfoList: List<SubscriptionInfo> = subscriptionManager.activeSubscriptionInfoList

        if (subscriptionInfoList.isNotEmpty()) {
            val simInfo = StringBuilder("Available SIM Cards:\n")
            for (subscriptionInfo in subscriptionInfoList) {
                simInfo.append("Carrier Name: ${subscriptionInfo.carrierName}\n")
                simInfo.append("Phone Number: ${subscriptionInfo.number}\n")
                simInfo.append("Subscription ID: ${subscriptionInfo.subscriptionId}\n\n")
            }
            Toast.makeText(context, simInfo.toString(), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "No SIM cards available", Toast.LENGTH_SHORT).show()
        }
    }
}