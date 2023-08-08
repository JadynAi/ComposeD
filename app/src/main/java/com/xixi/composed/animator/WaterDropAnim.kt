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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.w3c.dom.Text
import java.lang.Float.max
import kotlin.math.cos
import kotlin.math.sin


@Composable
@Preview
fun WaterDropAnim(modifier: Modifier = Modifier) {
    var currentState by remember { mutableStateOf<WaterState>(WaterState.Circle) }
    Box(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(500.dp)
            .padding(bottom = 180.dp)
    ) {
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
        var linearProgress by remember { mutableStateOf(0f) }
        with(LocalDensity.current) {
            val pointRadius = 15.dp.toPx()
            val bubbleRadius = 50.dp.toPx()
            val gapDis = 80.dp.toPx()
            val transition = updateTransition(targetState = currentState, label = "Water")

            val durationMillis = 1111
            val progress by transition.animateFloat(label = "Water", transitionSpec = {
                when {
                    WaterState.Circle isTransitioningTo WaterState.Bubble ->
                        TweenSpec(durationMillis = durationMillis, easing = EaseOutBounce)
                    else -> TweenSpec(durationMillis = durationMillis, easing = EaseOutExpo)
                }
            }) {
                when (it) {
                    WaterState.Bubble -> 1f
                    WaterState.Circle -> -0.2f
                }
            }
            Canvas(modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.4f)
                .aspectRatio(1f), onDraw = {
                Log.d("cecece", "WaterDropAnim: $progress $linearProgress")
                Log.d("cecece", "WaterDropAnimState: ${transition.currentState} ${transition.targetState}")
                if (transition.targetState == WaterState.Bubble) {
                    circleTransitionToBubble(
                        pointRadius, linearProgress, progress, Color.Blue,
                        center.x, size.height - pointRadius, bubbleRadius, gapDis, path
                    )
                    if (progress > linearProgress) {
                        linearProgress = progress
                    }
                } else if (transition.targetState == WaterState.Circle) {
                    linearProgress = 0f
                    bubbleTransitionToCircle(
                        pointRadius, 1 - progress, Color.Blue, center.x,
                        size.height - pointRadius, bubbleRadius, gapDis, path
                    )
                }
            })
        }
    }
}

private fun DrawScope.circleTransitionToBubble(
    pointRadius: Float,
    linearProgress: Float,
    progress: Float,
    pointColor: Color,
    centerX: Float,
    centerY: Float,
    bubbleRadius: Float,
    gap: Float,
    path: Path
) {
    val fixCircleCenterX = centerX
    val fixCircleCenterY = centerY
    val currentCircleRadius = pointRadius * (1f - linearProgress)
    drawCircle(
        color = pointColor,
        radius = currentCircleRadius,
        center = Offset(x = fixCircleCenterX, y = fixCircleCenterY)
    )
    val bubbleCenterX = centerX
    val currentDist = gap * linearProgress * 1.25f
    val bubbleCenterY = fixCircleCenterY - currentDist
    val linearChangeBubbleCenterY = fixCircleCenterY - gap * linearProgress * 1.25f

    val iAnchorX = bubbleCenterX
    val iAnchorY = (fixCircleCenterY + linearChangeBubbleCenterY) * 0.5f

    val linearChangedBubbleRadius = pointRadius + (bubbleRadius - pointRadius) * linearProgress
    val currentBubbleRadius = pointRadius + (bubbleRadius - pointRadius) * progress
    Log.d("cecece", "circleTransitionToBubble linearProgress: $linearProgress")

    val angel = 30.0

//    val iBubStartX = bubbleCenterX - linearChangedBubbleRadius 
//    val iBubStartY = bubbleCenterY 
//
//    val iBubEndX = bubbleCenterX + linearChangedBubbleRadius
//    val iBubEndY = bubbleCenterY
    val iBubStartX = bubbleCenterX + currentBubbleRadius * cos(angel * Math.PI / 180).toFloat()
    val iBubStartY = bubbleCenterY + currentBubbleRadius * sin(angel * Math.PI / 180).toFloat()

    val iBubEndX = bubbleCenterX + currentBubbleRadius * cos((180 - angel) * Math.PI / 180).toFloat()
    val iBubEndY = bubbleCenterY + currentBubbleRadius * sin((180 - angel) * Math.PI / 180).toFloat()

    val circleAngel = -angel
    val iFixCircleStartX = fixCircleCenterX + currentCircleRadius * cos(circleAngel * Math.PI / 180).toFloat()
    val iFixCircleStartY = fixCircleCenterY + currentCircleRadius * sin(circleAngel * Math.PI / 180).toFloat()

    val iFixCircleEndX = fixCircleCenterX + currentCircleRadius * cos((180 - circleAngel) * Math.PI / 180).toFloat()
    val iFixCircleEndY = fixCircleCenterY + currentCircleRadius * sin((180 - circleAngel) * Math.PI / 180).toFloat()

//    drawCircle(color = Color.Red, radius = 3.dp.toPx(), center = Offset(iFixCircleStartX, iFixCircleStartY))
//    drawCircle(color = Color.Red, radius = 3.dp.toPx(), center = Offset(iFixCircleEndX, iFixCircleEndY))
//
//    drawCircle(color = Color.Blue, radius = 3.dp.toPx(), center = Offset(iBubStartX, iBubStartY))
//    drawCircle(color = Color.Blue, radius = 3.dp.toPx(), center = Offset(iBubEndX, iBubEndY))
//    val iFixCircleStartX = fixCircleCenterX - currentCircleRadius
//    val iFixCircleStartY = fixCircleCenterY
//
//    val iFixCircleEndX = fixCircleCenterX + currentCircleRadius
//    val iFixCircleEndY = fixCircleCenterY

    path.reset()
    path.moveTo(iBubStartX, iBubStartY)
    path.quadraticBezierTo(iAnchorX, iAnchorY, iFixCircleStartX, iFixCircleStartY)
    path.lineTo(iFixCircleEndX, iFixCircleEndY)
    path.quadraticBezierTo(iAnchorX, iAnchorY, iBubEndX, iBubEndY)
    path.close()
    drawPath(path = path, color = pointColor)

    drawOval(
        color = pointColor, topLeft = Offset(bubbleCenterX - linearChangedBubbleRadius, linearChangeBubbleCenterY - linearChangedBubbleRadius),
        size = Size(linearChangedBubbleRadius * 2, currentBubbleRadius * 2)
    )
//    drawCircle(color = pointColor, center = Offset(bubbleCenterX, bubbleCenterY), radius = currentBubbleRadius)
}

private fun DrawScope.bubbleTransitionToCircle(
    pointRadius: Float,
    animProgress: Float,
    pointColor: Color,
    centerX: Float,
    centerY: Float,
    bubbleRadius: Float,
    gap: Float,
    path: Path
) {
    val fixCircleCenterY = centerY
    val fixCircleCenterX = centerX
    val circleProgress = animProgress.takeIf { it <= 1f } ?: 1f
    val currentCircleRadius = pointRadius * circleProgress
    drawCircle(
        color = pointColor,
        radius = currentCircleRadius,
        center = Offset(x = fixCircleCenterX, y = fixCircleCenterY)
    )
    val bubbleCenterX = centerX
    val bubbleCenterY = fixCircleCenterY - gap * (if (animProgress < 1f) 1f else (1f - (animProgress - 1f) / 0.2f))

    val iAnchorX = bubbleCenterX
    val iAnchorY = (fixCircleCenterY + bubbleCenterY) * 0.5f

    val currentBubbleRadius = bubbleRadius * (1f - circleProgress)

//    val angel = -30.0 + (30.0 * (if (animProgress <= 1f) 0f else ((animProgress - 1f) / 0.2f)))
    val angel = -30.0 + (30.0 * animProgress / 1.2f)
    Log.d("cecece", "bubbleTransitionToCircle: $angel")

    val iBubStartX = bubbleCenterX + currentBubbleRadius * cos(angel * Math.PI / 180)
    val iBubStartY = bubbleCenterY + currentBubbleRadius * sin(angel * Math.PI / 180)

    val iBubEndX = bubbleCenterX + currentBubbleRadius * cos((180 - angel) * Math.PI / 180)
    val iBubEndY = bubbleCenterY + currentBubbleRadius * sin((180 - angel) * Math.PI / 180)

//    val iFixCircleStartX = fixCircleCenterX - currentCircleRadius
//    val iFixCircleStartY = fixCircleCenterY
//
//    val iFixCircleEndX = fixCircleCenterX + currentCircleRadius
//    val iFixCircleEndY = fixCircleCenterY
    val circleAngel = -angel
    val iFixCircleStartX = fixCircleCenterX + currentCircleRadius * cos(circleAngel * Math.PI / 180).toFloat()
    val iFixCircleStartY = fixCircleCenterY + currentCircleRadius * sin(circleAngel * Math.PI / 180).toFloat()

    val iFixCircleEndX = fixCircleCenterX + currentCircleRadius * cos((180 - circleAngel) * Math.PI / 180).toFloat()
    val iFixCircleEndY = fixCircleCenterY + currentCircleRadius * sin((180 - circleAngel) * Math.PI / 180).toFloat()

    path.reset()
    path.moveTo(iBubStartX.toFloat(), iBubStartY.toFloat())
    path.quadraticBezierTo(iAnchorX, iAnchorY, iFixCircleStartX, iFixCircleStartY)
    path.lineTo(iFixCircleEndX, iFixCircleEndY)
    path.quadraticBezierTo(iAnchorX, iAnchorY, iBubEndX.toFloat(), iBubEndY.toFloat())
    path.close()
    drawPath(path = path, color = pointColor)

    drawOval(
        color = pointColor, topLeft = Offset(bubbleCenterX - currentBubbleRadius, bubbleCenterY - currentBubbleRadius),
        size = Size(currentBubbleRadius * 2, currentBubbleRadius * 2)
    )
//    drawCircle(color = pointColor, center = Offset(bubbleCenterX, bubbleCenterY), radius = currentBubbleRadius)
}

private sealed class WaterState {
    object Circle : WaterState()
    object Bubble : WaterState()
}

