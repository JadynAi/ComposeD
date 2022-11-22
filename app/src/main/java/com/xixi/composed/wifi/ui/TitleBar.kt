package com.xixi.composed.wifi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xixi.composed.R

/**
 *Jairett since 2022/8/24
 */
@Composable
@Preview
fun TitleBar(modifier: Modifier = Modifier, title: String = "test", backClick: () -> Unit = {}) {
    Box(modifier = modifier) {
        Image(painter = painterResource(id = R.drawable.ic_dashboard_top_bar_bg), contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .fillMaxWidth())
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()) {
            Spacer(modifier = Modifier.width(40f.dp))
            Button(onClick = backClick, modifier = Modifier
                .size(44.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC5CDDB)),
                shape = CircleShape,
                elevation = ButtonDefaults.elevation(defaultElevation = 3.dp),
                contentPadding = PaddingValues(0.dp,0.dp,0.dp,0.dp)
            ) {
                Image(painter = painterResource(R.drawable.icon_array_back_48),
                    modifier = Modifier
                        .size(31.dp),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = "close")
            }
            Text(text = title, fontSize = 22f.sp,
                fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 20f.dp))
        }
    }
}