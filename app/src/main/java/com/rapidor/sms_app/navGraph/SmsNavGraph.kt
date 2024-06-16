package com.rapidor.sms_app.navGraph

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rapidor.sms_app.screens.SmsListScreen
import com.rapidor.sms_app.viewmodel.SmsFetchViewModel

@Composable
fun SmsNavGraph(navController: NavHostController) {
    val mSmsFetchViewModel: SmsFetchViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screen.SMS_LIST.route) {
        composable(route = Screen.SMS_LIST.route) {
            SmsListScreen(navController,mSmsFetchViewModel)
        }
    }

}