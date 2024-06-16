package com.rapidor.sms_app.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rapidor.sms_app.R
import com.rapidor.sms_app.helper.DateTimeConverter.Companion.timestampToDateString
import com.rapidor.sms_app.helper.PrefKeys
import com.rapidor.sms_app.helper.SharedPreferenceManager
import com.rapidor.sms_app.models.AlarmTriggerModel
import com.rapidor.sms_app.models.SmsList
import com.rapidor.sms_app.services.AlarmSchedulerManager
import com.rapidor.sms_app.ui.theme.Common_Bg
import com.rapidor.sms_app.viewmodel.SmsFetchViewModel


var showDialog = mutableStateOf(false)
@Composable
fun SmsListScreen(navController: NavHostController, mSmsFetchViewModel: SmsFetchViewModel) {
    val mContext = LocalContext.current
    val systemUiController = rememberSystemUiController()

    val sharedPreferences= SharedPreferenceManager(mContext)
    systemUiController.setSystemBarsColor(
        color = Common_Bg
    )


    //This method request for getting all SMS from Inbox//
    LaunchedEffect(Unit) {
        mSmsFetchViewModel.fetchSmsFromPhyscialDevice(mContext, "")
    }
    val mSmsList = ArrayList<SmsList>()
    val mSmsListFromDevice by mSmsFetchViewModel.mSmsList.observeAsState(emptyList())
    val searchQuery = remember {
        mutableStateOf("")
    }
    mSmsListFromDevice.forEach { smsList ->
        mSmsList.add(smsList)
    }

    if (showDialog.value) {
        showAddNewSmsAlert(mContext,sharedPreferences)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            SearchBar(
                value = searchQuery.value,
                onValueChange = { newText ->
                    searchQuery.value = newText
                    mSmsFetchViewModel.fetchSmsFromPhyscialDevice(mContext, newText)
                }
            )
           if (mSmsList.size>0){
               LazyColumn(
                   Modifier.padding(
                       top = 24.dp,
                       start = 16.dp,
                       end = 16.dp,
                       bottom = 24.dp
                   )
               ) {
                   items(mSmsList) { sms ->
                       Card(
                           modifier = Modifier
                               .fillMaxWidth()
                               .wrapContentHeight()
                               .padding(8.dp),
                           colors = CardDefaults.cardColors(
                               containerColor =White,
                           ),
                       ) {
                           Column(
                               Modifier
                                   .padding(8.dp)
                                   .fillMaxSize()
                           ) {
                               Text(
                                   // text = sms.address,
                                   text = sms.address,
                                   modifier = Modifier,
                                   textAlign = TextAlign.Start,
                                   color = Color.Black,
                                   fontStyle = FontStyle.Normal,
                                   fontWeight = FontWeight.W700,
                               )
                               Spacer(modifier = Modifier.size(12.dp))
                               Text(
                                   text ="Recevied on : "+ timestampToDateString(sms.date),
                                   modifier = Modifier,
                                   textAlign = TextAlign.Start,
                                   color = Color.Black,
                                   fontStyle = FontStyle.Normal,
                                   fontWeight = FontWeight.W500,
                               )
                               Spacer(modifier = Modifier.size(12.dp))
                               Text(
                                   text = sms.body,
                                   modifier = Modifier,
                                   textAlign = TextAlign.Start,
                                   color = Color.Black,
                                   fontStyle = FontStyle.Normal,
                                   fontWeight = FontWeight.W500,
                               )
                               Spacer(modifier = Modifier.size(12.dp))
                               Box(
                                   modifier = Modifier
                                       .fillMaxWidth()
                                       .height(1.dp)
                                       .background(Color.Black)
                               )
                           }

                       }

                   }
               }

           }
        }

        FloatingActionButton(
            onClick = {
                showDialog.value=true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_textsms),
                contentDescription = "FAB Icon"
            )
        }
    }


}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Search", color = Color.Black) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showAddNewSmsAlert(context: Context, sharedPreferences: SharedPreferenceManager) {
    if (showDialog.value) {
        var textMobile by remember { mutableStateOf("") }
        var textDuration by remember { mutableStateOf("") }
        val maxCharMobile = 10
        val maxCharMinutes = 3

        val schedule = AlarmSchedulerManager(context)
        var alarmItem: AlarmTriggerModel

        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(White)
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
                        text = stringResource(id = R.string.sent_sms_to_number),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.W400,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        value = textMobile,
                        onValueChange = {
                            if (it.length <= maxCharMobile) textMobile = it
                        },
                        label = {
                            Text(stringResource(id = R.string.mobile))
                        },
                        textStyle = TextStyle(color = Color.Black),
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = textDuration,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            if (it.length <= maxCharMinutes) textDuration = it
                        },
                        label = {
                            Text(stringResource(id = R.string.time_duration))
                        },
                        textStyle = TextStyle(color = Color.Black),
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Row {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = {
                                if (textMobile.isEmpty() || textMobile.length!=10){
                                    Toast.makeText(context,context.getString(R.string.validate_mobile),Toast.LENGTH_LONG).show()
                                }else if (textDuration.isEmpty()){
                                    Toast.makeText(context,context.getString(R.string.validate_time),Toast.LENGTH_LONG).show()
                                }else{
                                    sharedPreferences.setStringPref(PrefKeys.KEY_MOBILE,textMobile)
                                    showDialog.value = false
                                    alarmItem = AlarmTriggerModel(
                                        time =textDuration.toLong(), msg = ""
                                    )
                                    schedule.scheduleAlarm(alarmItem)
                                    Toast.makeText(context,context.getString(R.string.job_scheduled),Toast.LENGTH_LONG).show()
                                }

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff091432)),
                            content = {
                                Text(
                                    stringResource(id = R.string.schedule),
                                    color = White
                                )
                            }
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = {
                                showDialog.value = false

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff091432)),
                            content = { Text(stringResource(id = R.string.cancel), color = White) }
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            showDialog.value = false
                            Toast.makeText(context,context.getString(R.string.job_canceled),Toast.LENGTH_LONG).show()
                            schedule.cancelAlarm()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff091432)),
                        content = { Text(stringResource(id = R.string.cancel_job), color = White) }
                    )
                }
            }
        }
    }

}