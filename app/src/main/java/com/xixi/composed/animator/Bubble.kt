package com.xixi.composed.animator

import android.annotation.SuppressLint
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xixi.composed.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    //首页选中项的索引
    private val _position = MutableLiveData(-1)

    //动画状态
    val animalBoolean = mutableStateOf(true)
    var position: LiveData<Int> = _position

    //选中索引数据刷新
    var bootomType = true
    fun positionChanged(selectedIndex: Int) {
        _position.value = selectedIndex
    }
}

@OptIn(InternalComposeApi::class)
@Composable
fun BottomNavigationTwo(homeViewModel: HomeViewModel = viewModel()) {
    val applyContext = currentComposer.applyCoroutineContext
    val clickTrue = remember { mutableStateOf(false) }
    val mCurAnimValueColor = remember { Animatable(1f) }
//    val animalBooleanState: Float by animateFloatAsState(
//        if (homeViewModel.animalBoolean.value) {
//            0f
//        } else {
//            1f
//        }, animationSpec = TweenSpec(durationMillis = 600),
//        finishedListener = {
//            if (it >= 0.9f && clickTrue.value) {
//                homeViewModel.animalBoolean.value = !homeViewModel.animalBoolean.value
//            }
//        }
//    )
    val stiffness = 100f
    val animalScaleCanvasWidthValue: Float by animateFloatAsState(
        if (!clickTrue.value) {
            0f
        } else {
            30f
        },
        animationSpec = SpringSpec(stiffness = stiffness),
    )
    val animalScaleCanvasHeightValue: Float by animateFloatAsState(
        if (!clickTrue.value) {
            0f
        } else {
            30f
        },
        animationSpec = SpringSpec(stiffness = stiffness),
    )
    val mCurAnimalHeight: Float by animateFloatAsState(
        if (!clickTrue.value) {
            -30f
        } else {
            30f
        },
        animationSpec = SpringSpec(stiffness = stiffness),
    )

    val mCurAnimValueY: Float by animateFloatAsState(
        if (!clickTrue.value) {
            0f
        } else {
            1f
        }, animationSpec = SpringSpec(stiffness = stiffness),
        finishedListener = {
            if (it >= 0.9f && clickTrue.value) {
                CoroutineScope(applyContext).launch {
                    mCurAnimValueColor.animateTo(
                        0f,
                        animationSpec = SpringSpec(stiffness = stiffness)
                    )
                }
            }
            if (it <= 0.01f && !clickTrue.value) {
                CoroutineScope(applyContext).launch {
                    mCurAnimValueColor.animateTo(
                        1f,
                        animationSpec = SpringSpec(stiffness = stiffness)
                    )
                }
            }
            //动画结束->回归原来位置
            if (it > 0.9f && clickTrue.value) {
                clickTrue.value = !clickTrue.value
            }
        }
        //TweenSpec(durationMillis = 1600)
        // DurationBasedAnimationSpec, FloatSpringSpec, FloatTweenSpec, KeyframesSpec, RepeatableSpec, SnapSpec, SpringSpec, TweenSpec
    )

    //半径的决定动画
    val mCurAnimValue: Float by animateFloatAsState(
        if (clickTrue.value) {
            0f
        } else {
            1f
        }, animationSpec = SpringSpec(dampingRatio = 1f, stiffness = 30f)
    )
    LaunchedEffect(key1 = mCurAnimValue, block = {
        delay(3000)
        homeViewModel.positionChanged(0)
        clickTrue.value = !clickTrue.value
        homeViewModel.animalBoolean.value = !homeViewModel.animalBoolean.value
    })
    Box(contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                drawIntoCanvas { canvas ->
                    //绘制底部曲线到底部
                    canvas.translate(0f, size.height)
                    canvas.scale(1f, -1f)
                    val paint = androidx.compose.ui.graphics.Paint()
                    paint.strokeWidth = 2f
                    paint.style = PaintingStyle.Fill
                    paint.color = Color(245, 215, 254, 255)

                    val height = 276f
                    val cicleHeight = height / 3
                    val ScaleHeight = animalScaleCanvasHeightValue
                    val ScaleWidth = animalScaleCanvasWidthValue
                    //控制脖子左边,一直在变化
                    val path = Path()
                    path.moveTo(0f + ScaleWidth, 0f)
                    path.lineTo(0f + ScaleWidth, height - cicleHeight + ScaleHeight)
                    path.quadraticBezierTo(
                        0f + ScaleWidth,
                        height + ScaleHeight,
                        cicleHeight,
                        height + ScaleHeight
                    )

                    //第一个左弧度
                    path.lineTo(size.width - cicleHeight - ScaleWidth, height + ScaleHeight)
                    path.quadraticBezierTo(
                        size.width - ScaleWidth,
                        height + ScaleHeight,
                        size.width - ScaleWidth,
                        height - cicleHeight + ScaleHeight
                    )
                    path.lineTo(size.width - ScaleWidth, 0f)
                    path.close()
//                    canvas.drawPath(path, paint)
//--------------------------------------------------------------------------
                    canvas.save()
                    //中间凸起部分
                    val centerHdX =
                        size.width / 3 / 2 + size.width / 3 * homeViewModel.position.value!!
                    //这里坐标系位置圆点就为上边线中点
                    canvas.translate(centerHdX, height)
                    val R = 30f
                    //0-50是变大部分
                    val RH = mCurAnimalHeight
                    //50到-50是变为平
                    val p0 = Offset(-R, 0f + RH + animalScaleCanvasHeightValue)
                    val p1 = Offset(-R, R + RH + animalScaleCanvasHeightValue)
                    val p3 = Offset(0f, 2 * R - 30f + RH + animalScaleCanvasHeightValue)
                    val p5 = Offset(R, R + RH + animalScaleCanvasHeightValue)
                    val p6 = Offset(R, 0f + RH + animalScaleCanvasHeightValue)
                    val p7 = Offset(100f, -10f + animalScaleCanvasHeightValue)

                    val pathCub = Path()
                    pathCub.moveTo(-100f, 0f + animalScaleCanvasHeightValue)
                    pathCub.cubicTo(p0.x, p0.y, p1.x, p1.y, p3.x, p3.y)
                    pathCub.cubicTo(p5.x, p5.y, p6.x, p6.y, p7.x, p7.y)


                    canvas.drawPath(pathCub, paint)

                    //中间凸起部分落下
                    canvas.restore()

//--------------------------------------------------------------------------

                    //绘制弹性圆球
                    //假设点击的是index=0一共三个底部按钮
                    canvas.save()
                    //1,2,3
                    //将坐标系移动到点击部位()这样写起来比较爽好理解。将点击部位作为我们的坐标系园点
                    val centerX =
                        size.width / 3 / 2 + size.width / 3 * homeViewModel.position.value!!
                    Log.e("圆点", "LoginPage: $centerX")
                    canvas.translate(centerX, height * 2 / 3.2f)
                    canvas.drawCircle(Offset(0f, 0f), 100f, paint)
                    //这里我们清楚坐标圆点之后我们进行绘制我们的圆
                    val r = 100f - 50 * (1 - mCurAnimValue)
                    //圆的坐标和中心点的坐标计算
                    //1.首先 原点为(0f,0f)且半径r=100f--->那么p6(0f,r),p5=(r/2,r),p4(r,r/2),p3(r,0f)
                    //2.第二象限里面 p2(r,-r/2),p1(r/2,-r),p0(0f,r)
                    //3.第三象限里面 p11(-r/2,-r),p10(-r,-r/2),p9(-r,0f)
                    //4.第四象限里面 p8(-r,r/2),p7(r/2,r),p6(0f,r)
                    Log.e("mCurAnimValueY", "LoginPage=: $mCurAnimValueY")
                    val moveTopHeight = mCurAnimValueY * 250f
                    val P0 = Offset(0f, -r + moveTopHeight + animalScaleCanvasHeightValue)
                    val P1 = Offset(r / 2, -r + moveTopHeight + animalScaleCanvasHeightValue)
                    val P2 = Offset(r, -r / 2 + moveTopHeight + animalScaleCanvasHeightValue)
                    val P3 = Offset(r, 0f + moveTopHeight + animalScaleCanvasHeightValue)
                    val P4 = Offset(r, r / 2 + moveTopHeight + animalScaleCanvasHeightValue)
                    val P5 = Offset(r / 2, r + moveTopHeight + animalScaleCanvasHeightValue)
                    val P6 = Offset(0f, r + moveTopHeight + animalScaleCanvasHeightValue)
                    val P7 = Offset(-r / 2, r + moveTopHeight + animalScaleCanvasHeightValue)
                    val P8 = Offset(-r, r / 2 + moveTopHeight + animalScaleCanvasHeightValue)
                    val P9 = Offset(-r, 0f + moveTopHeight + animalScaleCanvasHeightValue)
                    val P10 = Offset(-r, -r / 2 + moveTopHeight + animalScaleCanvasHeightValue)
                    val P11 = Offset(-r / 2, -r + moveTopHeight + animalScaleCanvasHeightValue)

                    val heightController = 180f
                    val pathReult = Path()
                    pathReult.moveTo(P0.x, P0.y - heightController * mCurAnimValue)
                    //p1->p2->p3
                    pathReult.cubicTo(
                        P1.x,
                        P1.y - 30 * mCurAnimValue,
                        P2.x,
                        P2.y - 30 * mCurAnimValue,
                        P3.x,
                        P3.y
                    )
                    //p4->p5->p6
                    pathReult.cubicTo(P4.x, P4.y, P5.x, P5.y, P6.x, P6.y)
                    //p7->p8->p9
                    pathReult.cubicTo(P7.x, P7.y, P8.x, P8.y, P9.x, P9.y)
                    //p10->p11->p0
                    pathReult.cubicTo(
                        P10.x,
                        P10.y - 30 * mCurAnimValue,
                        P11.x,
                        P11.y - 30 * mCurAnimValue,
                        P0.x,
                        P0.y - heightController * mCurAnimValue
                    )
                    pathReult.close()
                    //
                    paint.color = Color(245, 215, 254, mCurAnimValueColor.value.toInt() * 255)
                    canvas.drawPath(pathReult, paint)
                }

            }
        }
//        Text(text = "Start", modifier = Modifier.padding(80.dp)
//            .clickable {
//                homeViewModel.positionChanged(0)
//                clickTrue.value = !clickTrue.value
//                homeViewModel.animalBoolean.value = !homeViewModel.animalBoolean.value
//            }.align(alignment = Alignment.BottomStart)
//        )
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Bottom
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher),
//                contentDescription = "1",
//                modifier = Modifier
//                    .modifiers(homeViewModel.position.value, 0, animalBooleanState)
//                    .clickable {
//                        homeViewModel.positionChanged(0)
//                        clickTrue.value = !clickTrue.value
//                        homeViewModel.animalBoolean.value = !homeViewModel.animalBoolean.value
//                    }
//            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher),
//                contentDescription = "1",
//                modifier = Modifier
//                    .modifiers(homeViewModel.position.value, 1, animalBooleanState)
//                    .clickable {
//                        homeViewModel.positionChanged(1)
//                        clickTrue.value = !clickTrue.value
//                        homeViewModel.animalBoolean.value = !homeViewModel.animalBoolean.value
//
//                    }
//            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher),
//                contentDescription = "1",
//                modifier = Modifier
//                    .modifiers(homeViewModel.position.value, 2, animalBooleanState)
//                    .clickable {
//                        homeViewModel.positionChanged(2)
//                        clickTrue.value = !clickTrue.value
//                        homeViewModel.animalBoolean.value = !homeViewModel.animalBoolean.value
//                    }
//            )
//
//        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.modifiers(
    animalCenterIndex: Int?,
    i: Int,
    animalBooleanState: Float
): Modifier {
    Log.e("currentValue=", "modifiers: " + animalCenterIndex.toString() + "==" + i)
    return if (animalCenterIndex == i) {
        Modifier
            .padding(bottom = 35.dp + (animalBooleanState * 100).dp)
            .width(25.dp)
            .height(25.dp)
    } else {
        return Modifier
            .padding(bottom = 35.dp - (animalBooleanState * 10).dp)
            .width(25.dp)
            .height(25.dp)
    }
}
