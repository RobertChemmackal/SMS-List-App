package com.rapidor.sms_app.navGraph

sealed class Screen(val route:String){
    object SMS_LIST: Screen(route = "sms_list")
}
