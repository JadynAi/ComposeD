package com.xixi.composed.wifi

import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiData
import com.xixi.composed.wifi.data.Async

/**
 *Jairett since 2022/7/22
 */
data class WifiUIState(val list: Async<List<WifiData>> = Async.Uninitialized,
                       val isWifiEnable: Boolean,
                       val connectWifi: Async<WifiData> = Async.Uninitialized) {}

data class Cece(val isE:Boolean)