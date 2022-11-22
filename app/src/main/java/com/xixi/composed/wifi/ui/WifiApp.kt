package com.xixi.composed.wifi.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiData
import com.xixi.composed.R
import com.xixi.composed.wifi.WifiUIState
import com.xixi.composed.wifi.WifiViewModel
import com.xixi.composed.wifi.data.Async

/**
 *Jairett since 2022/7/21
 */
var lastState: WifiUIState? = null

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@Preview
fun WifiApp(viewModel: WifiViewModel = viewModel()) {
    val wifiUiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    lastState = wifiUiState
    Column(Modifier
        .background(Color(android.graphics.Color.parseColor("#DDDEEA")))
        .fillMaxWidth(0.8f)) {
        val resources = LocalContext.current.resources
        val titleH = 55f.dp
        TitleBar(title = resources.getString(R.string.setting_wifi_title), modifier = Modifier.height(titleH)
        ) { }
        Column(Modifier
            .padding(start = 30f.dp, end = 30f.dp)
            .fillMaxHeight()
            .fillMaxWidth()
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(54f.dp)) {
                Text(text = "WI-FI", fontSize = 21f.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                    .align(Alignment.CenterStart))
                Image(painter = painterResource(id = if (!wifiUiState.isWifiEnable) R.drawable.ic_switch_off else R.drawable.ic_switch_on),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable(indication = null, interactionSource = MutableInteractionSource(), onClick = { viewModel.toggleWifiSwitch() })
                        .size(42f.dp, 29f.dp))
            }
            Image(painter = painterResource(id = R.drawable.icon_setting_item_divider), contentDescription = "", modifier = Modifier.fillMaxWidth())
            Box(modifier = Modifier
                .height(51f.dp)
                .fillMaxWidth()) {
                Text(text = "Available Networks", fontSize = 15f.sp, modifier = Modifier
                    .align(Alignment.CenterStart),
                    color = Color(android.graphics.Color.parseColor("#323D55")))
                Text(text = "NOT FIND YOUR HOTSPOT?", fontSize = 15f.sp, modifier = Modifier
                    .align(Alignment.CenterEnd),
                    color = Color(android.graphics.Color.parseColor("#00575B")),
                    style = TextStyle(textDecoration = TextDecoration.Underline))
            }
            Image(painter = painterResource(id = R.drawable.icon_setting_item_divider), contentDescription = "", modifier = Modifier.fillMaxWidth())
            var isPullEnd by remember { mutableStateOf(false) }
            if (wifiUiState.list !is Async.Loading) {
                isPullEnd = false
            }
            val isPullRefreshing = rememberSwipeRefreshState(isPullEnd && wifiUiState.list is Async.Loading)
            SwipeRefresh(state = isPullRefreshing, onRefresh = {
                isPullEnd = true
                viewModel.refreshWifiList()
            }) {
                val async = wifiUiState.list
                if (async is Async.Success) {
                    WifiList(list = async.value, { pos: Int, wifiData: WifiData -> })
                } else if (async is Async.Loading && async.value != null) {
                    WifiList(list = async.value, { pos: Int, wifiData: WifiData -> })
                }
            }
        }
    }
}