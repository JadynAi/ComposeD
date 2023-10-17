package com.xixi.composed.animator

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.xixi.composed.R
import kotlin.math.roundToInt


@Composable
fun FlickerAnimate(modifier: Modifier) {
    val width = with(LocalDensity.current) { 200.dp.toPx() } 
    Box(modifier = modifier){
        val offsetX = remember { Animatable(0f) }
        LaunchedEffect(key1 = true) {
            offsetX.animateTo(
                targetValue = width,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = LinearEasing)
                )
            )
        }

        Image(
            modifier = Modifier
                .wrapContentSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                , painter = painterResource(id = R.drawable.gradient_walker_1), contentDescription = ""
        )  
    }
}