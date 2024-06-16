package com.rapidor.sms_app

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rapidor.sms_app.helper.PermissionHandler
import com.rapidor.sms_app.navGraph.SmsNavGraph
import com.rapidor.sms_app.ui.theme.Common_Bg
import com.rapidor.sms_app.ui.theme.SmsAppTheme


class SmsListActivity : ComponentActivity() {
    private val PERMISSION_SEND_SMS = 100
    private lateinit var navController: NavHostController
    var showDialog = mutableStateOf(false)
    var isPermissionEnabled = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableUserPermissionForReadSms()
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color =Common_Bg
            )
            navController = rememberNavController()
            SmsAppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xff2E8B57)),
                            title = { Text(text = stringResource(id = R.string.sms_List), color = Color.White, fontSize = 20.sp,
                                fontWeight = FontWeight.W500) }
                        )
                    },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .background(Color.White),
                        ) {
                            if (isPermissionEnabled.value && !showDialog.value){
                                SmsNavGraph(navController = navController)
                            }else{
                                showAddNewSmsAlert()
                            }
                        }
                    })
            }
        }

    }

    //@Composable
    private fun enableUserPermissionForReadSms() {
        val permissionHandler = PermissionHandler(this)
        if (!permissionHandler.isSmsPermissionsGranted(this)) {
            requestSmsPermissions()
            isPermissionEnabled.value=false
        }else{
            isPermissionEnabled.value=true
        }
        requestReadContactsPermission()
    }
    private fun requestSmsPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.SEND_SMS,android.Manifest.permission.READ_SMS,android.Manifest.permission.READ_CONTACTS),
            PERMISSION_SEND_SMS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_SEND_SMS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    isPermissionEnabled.value=true
                    showDialog.value=false
                } else {
                    isPermissionEnabled.value=false
                    showDialog.value=true
                }
                return
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun showAddNewSmsAlert() {
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialog.value = true
                }
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W700,
                            fontSize = 20.sp,
                            color = Color.Black,
                            text = stringResource(id = R.string.sent_sms),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = stringResource(id = R.string.permission_alert),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.W400,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.size(12.dp))

                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                showDialog.value = false
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri: Uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivityForResult(intent, PERMISSION_SEND_SMS)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff091432)),
                            content = { Text(stringResource(id = R.string.enable_permission), color = Color.White) }
                        )
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_SEND_SMS) {
           enableUserPermissionForReadSms()
        }
    }

    private fun requestReadContactsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                123
            )
        }
    }

}


