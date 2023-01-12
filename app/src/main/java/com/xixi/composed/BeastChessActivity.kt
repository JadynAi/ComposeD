package com.xixi.composed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.source.WifiRepository
import com.xixi.composed.animator.BottomNavigationTwo
import com.xixi.composed.animator.WaterDropAnim
import com.xixi.composed.custom.TestCustomCompose
import com.xixi.composed.ui.theme.ComposeDTheme
import com.xixi.composed.wifi.ui.TitleBar
import kotlin.math.cos
import kotlin.math.sin

class BeastChessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterDropAnim()
        }
    }
}

@Composable
fun Greeting() {
    Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
        val dp = 50.dp.toPx()
        val center1 = this.center
        drawCircle(color = Color.Blue, radius = dp)
        val d = 30.0
        val x = center1.x + dp * cos(d * Math.PI / 180)
        val y = center1.y + dp * sin(d * Math.PI / 180)
        drawCircle(color = Color.Red, radius = 5.dp.toPx(), center = Offset(x.toFloat(), y.toFloat()))
        val x1 = center1.x + dp * cos((180 - d) * Math.PI / 180)
        val y1 = center1.y + dp * sin((180 - d) * Math.PI / 180)
        drawCircle(color = Color.Yellow, radius = 5.dp.toPx(), center = Offset(x1.toFloat(), y1.toFloat()))
    })
}

@Composable
@Preview(showBackground = false)
fun DefaultPreview() {
    Greeting()
}