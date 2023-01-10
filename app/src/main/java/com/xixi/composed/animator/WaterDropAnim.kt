package com.xixi.composed.animator

import android.util.Log
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.xixi.composed.R
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun WaterDropAnim(modifier: Modifier = Modifier) {
    var currentState by remember { mutableStateOf<WaterState>(WaterState.Circle) }
    Box(modifier = modifier
        .fillMaxWidth(0.8f)
        .height(500.dp)
        .padding(bottom = 180.dp)) {
        Text(text = "Start", modifier = Modifier
            .align(alignment = Alignment.BottomStart)
            .padding(10.dp)
            .background(color = Color.Blue)
            .clickable {
                currentState = WaterState.Bubble
            }
        )
        Text(text = "Dismiss", modifier = Modifier
            .align(alignment = Alignment.BottomEnd)
            .padding(10.dp)
            .background(color = Color.Blue)
            .clickable {
                currentState = WaterState.Circle
            }
        )
        val path by remember { mutableStateOf(Path()) }
        with(LocalDensity.current) {
            val pointRadius = 15.dp.toPx()
            val bubbleRadius = 50.dp.toPx()
            val gapDis = 80.dp.toPx()
            val transition = updateTransition(targetState = currentState, label = "Water")
            val progress by transition.animateFloat(label = "Water", transitionSpec = {
                when {
                    WaterState.Circle isTransitioningTo WaterState.Bubble ->
                        TweenSpec(durationMillis = 1250, easing = EaseOutBounce)
                    else -> TweenSpec(durationMillis = 1250, easing = EaseOutExpo)
                }
            }) {
                when (it) {
                    WaterState.Bubble -> 1f
                    WaterState.Circle -> 0f
                }
            }
            Log.d("cecece", "WaterDropAnim: $progress ${transition.currentState}")
            Canvas(modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.4f)
                .aspectRatio(1f), onDraw = {
                if (transition.targetState == WaterState.Bubble) {
                    circleTransitionToBubble(pointRadius, progress, Color.Blue,
                        center.x, size.height - pointRadius, bubbleRadius, gapDis, path)
                } else if (transition.targetState == WaterState.Circle) {
                    bubbleTransitionToCircle(pointRadius, 1 - progress, Color.Blue, center.x,
                        size.height - pointRadius, bubbleRadius, gapDis, path)
                }
            })
        }
    }
}

private fun DrawScope.circleTransitionToBubble(pointRadius: Float,
                                               animProgress: Float,
                                               pointColor: Color,
                                               centerX: Float,
                                               centerY: Float,
                                               bubbleRadius: Float,
                                               gap: Float,
                                               path: Path) {

    val fixCircleCenterY = centerY
    val fixCircleCenterX = centerX
    val currentCircleRadius = if (animProgress <= 0.5f || animProgress > 0.8f) pointRadius * (1f - animProgress)
    else 0.5f * pointRadius
    drawCircle(
        color = pointColor,
        radius = currentCircleRadius,
        center = Offset(x = fixCircleCenterX, y = fixCircleCenterY)
    )
    val bubbleCenterX = centerX
    val currentDist = gap * animProgress
    val bubbleCenterY = fixCircleCenterY - currentDist

    val iAnchorX = bubbleCenterX
    val iAnchorY = (fixCircleCenterY + bubbleCenterY) * 0.5f

    val currentBubbleRadius = pointRadius + (bubbleRadius - pointRadius) * animProgress

    val angel = -30.0

    val iBubStartX = bubbleCenterX + currentBubbleRadius * cos(angel * Math.PI / 180)
    val iBubStartY = bubbleCenterY + currentBubbleRadius * sin(angel * Math.PI / 180)

    val iBubEndX = bubbleCenterX + currentBubbleRadius * cos((180 - angel) * Math.PI / 180)
    val iBubEndY = bubbleCenterY + currentBubbleRadius * sin((180 - angel) * Math.PI / 180)

    val iFixCircleStartX = fixCircleCenterX - currentCircleRadius
    val iFixCircleStartY = fixCircleCenterY

    val iFixCircleEndX = fixCircleCenterX + currentCircleRadius
    val iFixCircleEndY = fixCircleCenterY

    path.reset()
    path.moveTo(iBubStartX.toFloat(), iBubStartY.toFloat())
    path.quadraticBezierTo(iAnchorX, iAnchorY, iFixCircleStartX, iFixCircleStartY)
    path.lineTo(iFixCircleEndX, iFixCircleEndY)
    path.quadraticBezierTo(iAnchorX, iAnchorY, iBubEndX.toFloat(), iBubEndY.toFloat())
    path.close()
    drawPath(path = path, color = pointColor)

    drawCircle(color = pointColor, center = Offset(bubbleCenterX, bubbleCenterY), radius = currentBubbleRadius)
}

private fun DrawScope.bubbleTransitionToCircle(pointRadius: Float,
                                               animProgress: Float,
                                               pointColor: Color,
                                               centerX: Float,
                                               centerY: Float,
                                               bubbleRadius: Float,
                                               gap: Float,
                                               path: Path) {
    val fixCircleCenterY = centerY
    val fixCircleCenterX = centerX
    val currentCircleRadius = pointRadius * animProgress
    drawCircle(
        color = pointColor,
        radius = currentCircleRadius,
        center = Offset(x = fixCircleCenterX, y = fixCircleCenterY)
    )
    val bubbleCenterX = centerX
    val ratio = if (animProgress <= 0.9f) 1f else (1 - animProgress)
    val bubbleCenterY = fixCircleCenterY - gap * ratio

    val iAnchorX = bubbleCenterX
    val iAnchorY = (fixCircleCenterY + bubbleCenterY) * 0.5f

    val currentBubbleRadius = bubbleRadius * (1f - animProgress)

    val angel = -30.0

    val iBubStartX = bubbleCenterX + currentBubbleRadius * cos(angel * Math.PI / 180)
    val iBubStartY = bubbleCenterY + currentBubbleRadius * sin(angel * Math.PI / 180)

    val iBubEndX = bubbleCenterX + currentBubbleRadius * cos((180 - angel) * Math.PI / 180)
    val iBubEndY = bubbleCenterY + currentBubbleRadius * sin((180 - angel) * Math.PI / 180)

    val iFixCircleStartX = fixCircleCenterX - currentCircleRadius
    val iFixCircleStartY = fixCircleCenterY

    val iFixCircleEndX = fixCircleCenterX + currentCircleRadius
    val iFixCircleEndY = fixCircleCenterY

    path.reset()
    path.moveTo(iBubStartX.toFloat(), iBubStartY.toFloat())
    path.quadraticBezierTo(iAnchorX, iAnchorY, iFixCircleStartX, iFixCircleStartY)
    path.lineTo(iFixCircleEndX, iFixCircleEndY)
    path.quadraticBezierTo(iAnchorX, iAnchorY, iBubEndX.toFloat(), iBubEndY.toFloat())
    path.close()
    drawPath(path = path, color = pointColor)

    drawCircle(color = pointColor, center = Offset(bubbleCenterX, bubbleCenterY), radius = currentBubbleRadius)
}

private sealed class WaterState {
    object Circle : WaterState()
    object Bubble : WaterState()
}

