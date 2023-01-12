package com.xixi.composed.animator

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun JellyAnim(modifier: Modifier = Modifier) {
    keyframes<Float> {
        durationMillis = 900
        0.0f at 0 with LinearEasing
    }
}