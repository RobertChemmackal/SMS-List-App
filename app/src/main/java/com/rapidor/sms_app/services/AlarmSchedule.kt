package com.rapidor.sms_app.services

import com.rapidor.sms_app.models.AlarmTriggerModel

interface AlarmSchedule {
    fun scheduleAlarm(alarmSchedule: AlarmTriggerModel)
    fun cancelAlarm()
   // fun cancelAlarm(alarmSchedule: AlarmTriggerModel)
}