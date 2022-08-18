package com.xixi.composed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.xixi.composed.ui.theme.ComposeDTheme

/**
 *Jairett since 2022/7/21
 */
@Composable
@Preview
fun WifiApp() {
    ComposeDTheme {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (titleBar, switch, line1, line2, tips, list) = createRefs()
            TopBackBar("Wi-Fi", Modifier.constrainAs(titleBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            })
            Row(modifier = Modifier.constrainAs(switch){
                top.linkTo(titleBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                Text(text = "Wi-Fi")
            }
        }
    }
}

@Composable
private fun TopBackBar(title: String, modifier: Modifier) {
    Row(
        modifier = Modifier
            .height(34.dp)
            .padding(start = 15.dp)
            .then(modifier)
    ) {
        OutlinedButton(
            onClick = {},
            shape = CircleShape,
            modifier = Modifier.size(34.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Gray),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_array_back_48),
                contentDescription = null,
                contentScale = ContentScale.Inside
            )
        }
        Text(
            text = title, modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 12.dp)
        )
    }
}