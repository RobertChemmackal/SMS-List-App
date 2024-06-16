package com.rapidor.sms_app.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import com.rapidor.sms_app.helper.PrefKeys.Companion.KEY_REQ_CODE
import com.rapidor.sms_app.helper.SharedPreferenceManager
import com.rapidor.sms_app.models.AlarmTriggerModel
import java.util.Random


class AlarmSchedulerManager(private val context: Context):AlarmSchedule {
    private val alarmManager=context.getSystemService(AlarmManager::class.java)

    val intent=Intent(context,SmsBroadCastService::class.java).apply {
        putExtra("msg","")
        putExtra("mobile","")
    }
    override fun scheduleAlarm(alarmSchedule: AlarmTriggerModel) {
        val manager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val minutes = alarmSchedule.time
        val millisecondsForSms = (minutes * 60000)

        val randomCode: Int = Random().nextInt(61) + 20

        val pendingIntent = PendingIntent.getBroadcast(context,randomCode,intent,
                PendingIntent.FLAG_IMMUTABLE)

        manager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            millisecondsForSms,
            //alarmSchedule.time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            pendingIntent
        )
        val sharedPreferences= SharedPreferenceManager(context)
        sharedPreferences.setStringPref(KEY_REQ_CODE,randomCode.toString())

        Log.d("AlarmId Request",sharedPreferences.getStringPref(
            KEY_REQ_CODE))
    }

    //override fun cancelAlarm(alarmSchedule: AlarmTriggerModel) {
    override fun cancelAlarm() {
        val sharedPreferences= SharedPreferenceManager(context)
       /* alarmManager.cancel(PendingIntent.getBroadcast(context,sharedPreferences.getStringPref(
            KEY_REQ_CODE).toInt(),
            Intent(context,SmsBroadCastService::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_MUTABLE
        ))*/

        try {
            val i: Intent = Intent(context, SmsBroadCastService::class.java)
            val pi = PendingIntent.getService(context, sharedPreferences.getStringPref(
                KEY_REQ_CODE).toInt(), i, PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_MUTABLE
            )
            val am = context.getSystemService(ALARM_SERVICE) as AlarmManager?
            am!!.cancel(pi)
        }catch (e:Exception){
            Log.d("AlarmId",e.message.toString())
        }
    }
}