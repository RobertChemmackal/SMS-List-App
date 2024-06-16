package com.rapidor.sms_app.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rapidor.sms_app.R
import com.rapidor.sms_app.SmsListActivity
import com.rapidor.sms_app.ui.theme.Common_Bg

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = Common_Bg
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Common_Bg), contentAlignment = Alignment.Center) {
                Column {
                    Image(
                        painterResource(R.drawable.ic_splash_logo),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(id = R.string.lbl_splash),
                        modifier = Modifier,
                        textAlign = TextAlign.Center, color = Color.White, fontStyle = FontStyle.Normal, fontWeight = FontWeight.W900, fontSize = 24.sp,
                    )
                }
            }
            Thread {
                Thread.sleep(2000)
                runOnUiThread {
                    startActivity(Intent(this@SplashActivity, SmsListActivity::class.java))
                    finish()
                }
            }.start()

        }
    }
}