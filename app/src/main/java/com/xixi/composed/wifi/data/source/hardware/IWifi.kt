package com.xixi.composed.wifi.data.source.hardware

import androidx.annotation.MainThread
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiData

/**
 *Jairett since 2022/7/22
 */
interface IWifi {

    var isWifiOpen: Boolean

    fun startScanWifi(): Boolean

    fun connectOpenWifi(wifiData: WifiData, success: () -> Unit, error: (Exception) -> Unit)

    fun connectSavedWifi(wifiData: WifiData, success: () -> Unit, error: (Exception) -> Unit)

    fun connectNewWifi(wifiData: WifiData, pwd: String, success: () -> Unit, error: (Exception) -> Unit)

    fun onWifiEnableChanged(callback: (Boolean) -> Unit)

    @MainThread
    fun registerWifiListCallback(callback: WifiScanResultCallback)

    @MainThread
    fun unregisterWifiListCallback(callback: WifiScanResultCallback)
    
    fun cancelConnecting(wifiData: WifiData)

    fun release()
}

abstract class WifiScanResultCallback {

    var isComplete = false

    abstract fun onSucceed(data: List<WifiData>?)

    abstract fun onFailed(e: Exception)
}