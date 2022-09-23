package com.xixi.composed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xixi.composed.ui.theme.ComposeDTheme
import com.xixi.composed.wifi.ui.WifiApp

class BeastChessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WifiApp()
//            Greeting()
        }
    }
}

@Composable
fun Greeting() {
    Image(painter = painterResource(R.drawable.ic_ico_wifi_full),
        modifier = Modifier
            .shadow(2.dp, CircleShape)
            .background(color = Color.White, shape = CircleShape)
            .size(45.dp),
        contentDescription = "close")
}

@Composable
@Preview(showBackground = false)
fun DefaultPreview() {
    ComposeDTheme {
        Greeting()
    }
}