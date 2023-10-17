package com.xixi.composed

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class BeastChessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            FlickerAnimate(
//                Modifier
//                    .width(200.dp)
//                    .height(50.dp)
//                    .background(Color.Yellow)
//            )
//
//            AsyncImage(model = "", contentDescription = "")
            
//            Greeting()
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TestMutableState() {
    Column {
        Log.d("cecece", "TestMutableState: refresh top")
        Text(
            text = "dasdasdasd", modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
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
    var s by remember {
        mutableStateOf(1)
    }
    val transitionAnimation = remember() { Animatable(initialValue = 0f) }

    LaunchedEffect(Unit) {
        transitionAnimation.animateTo(1f, animationSpec = TweenSpec<Float>(durationMillis = 300, easing = LinearEasing))
    }
    Canvas(modifier = Modifier.fillMaxSize()
        .clickable { s++ }
    ) {
        drawIntoCanvas {
            drawRect(
                color = Color(0xffC9ADFF),
                topLeft = Offset(0f, 0f),
                size = Size(100f, 100f)
            )
            drawRect(
                color = Color(0xffC9ADFF).copy(alpha = 0.48f),
                topLeft = Offset(0f, 100f),
                size = Size(100f, size.height)
            )

            drawRect(
                color = Color(0xffC9ADFF),
                topLeft = Offset(200f, 0f),
                size = Size(100f, 100f)
            )
            drawRect(
                color = Color(0xffC9ADFF).copy(alpha = 0.48f),
                topLeft = Offset(200f, 100f),
                size = Size(100f, size.height * 0.5f)
            )

            drawRect(
                color = Color(0xffC9ADFF),
                topLeft = Offset(400f, 100f),
                size = Size(100f, 100f)
            )
            drawRect(
                color = Color(0xffC9ADFF).copy(alpha = 0.48f),
                topLeft = Offset(400f, 100f),
                size = Size(100f, size.height * 0.2f)
            )
        }
    }
}

@Composable
@Preview(showBackground = false)
fun DefaultPreview() {
    Greeting()
}