package com.xixi.composed.wifi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiData
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiType
import com.xixi.composed.R

/**
 *Jairett since 2022/7/22
 */

@Composable
fun WifiList(
    list: List<WifiData>,
    onItemClick: (pos: Int, wifiData: WifiData) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier) {
        itemsIndexed(items = list, key = { _, task -> task.SSID }) { index, task ->
            WifiItem(task, index, onItemClick, modifier)
        }
    }
}

@Composable
fun WifiItem(wifiData: WifiData, index: Int, onItemClick: (pos: Int, wifiData: WifiData) -> Unit, modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = Modifier
        .clickable(onClick = {
            onItemClick.invoke(index, wifiData)
        })
        .height(77.dp)) {
        val (signal, title, stats, divide) = createRefs()
        Image(painter = painterResource(wifiData.getSignalLevelDrawable()),
            colorFilter = ColorFilter.tint(Color.Black),
            contentDescription = "", modifier = Modifier.constrainAs(signal) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            })
        Text(text = wifiData.SSID, fontSize = 18f.sp, fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(signal.end)
                    top.linkTo(parent.top)
                }
                .padding(start = 10f.dp))
        Text(text = wifiData.getDetail(), fontSize = 13f.sp,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(stats) {
                    start.linkTo(title.start)
                    top.linkTo(title.bottom)
                }
                .padding(start = 10f.dp))
        createVerticalChain(title, stats, chainStyle = ChainStyle.Packed)
        Image(painter = painterResource(id = R.drawable.icon_setting_item_divider),
            contentDescription = "", modifier = Modifier.constrainAs(divide) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    }
}

@Composable
private fun WifiData.getDetail(): String {
    if (type is WifiType.Connect) {
        return when (type) {
            WifiType.Connect.Connecting -> LocalContext.current.resources.getString(R.string.setting_wifi_connecting)
            WifiType.Connect.Connected -> LocalContext.current.resources.getString(R.string.connected)
        }
    } else if (type is WifiType.Saved) {
        return LocalContext.current.resources.getString(R.string.wifi_setting_item_saved)
    }
    return ""
}
