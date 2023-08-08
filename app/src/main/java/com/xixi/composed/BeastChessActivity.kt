package com.xixi.composed

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.xixi.composed.animator.WaterDropAnim
import org.w3c.dom.Text
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin

class BeastChessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestMutableState()
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TestMutableState() {
    Column {
        Log.d("cecece", "TestMutableState: refresh top")
        Text(text = "dasdasdasd", modifier = Modifier
            .width(100.dp)
            .height(100.dp))
        Log.d("cecece", "TestMutableState: refresh after das")
        var count by mutableStateOf(1)
        Log.d("cecece", "TestMutableState: refresh state")
        Text(text = "haha$count", modifier = Modifier.clickable { 
            count++
        })
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