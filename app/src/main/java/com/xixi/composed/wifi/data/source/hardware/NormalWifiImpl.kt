package com.xixi.composed.wifi.data.source.hardware

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.ScanResult
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.util.Log
import androidx.annotation.MainThread
import androidx.collection.ArraySet
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiData
import java.nio.channels.ClosedSelectorException
import java.security.InvalidParameterException
import java.util.*
import kotlin.concurrent.thread

/**
 *Jairett since 2022/7/22
 */
typealias WifiConnectCallback = Triple<WifiData, () -> Unit, (Exception) -> Unit>
typealias ErrorPwdException = InvalidParameterException
typealias WifiCloseException = ClosedSelectorException

class NormalWifiImpl : IWifi {

    private val scanResultCallbacks by lazy { Collections.synchronizedSet(ArraySet<WifiScanResultCallback>(3)) }
    
    override var isWifiOpen: Boolean = false

    override fun startScanWifi(): Boolean {
        thread { 
            Thread.sleep(5000)
            Handler(Looper.getMainLooper()).post { 
                val iterator = scanResultCallbacks.iterator()
                while (iterator.hasNext()) {
                    val callback = iterator.next()
                    callback?.onSucceed(arrayListOf())
                }
            }
        }
        return true
    }

    override fun connectOpenWifi(wifiData: WifiData, success: () -> Unit, error: (Exception) -> Unit) {
    }

    override fun connectSavedWifi(wifiData: WifiData, success: () -> Unit, error: (Exception) -> Unit) {
    }

    override fun connectNewWifi(wifiData: WifiData, pwd: String, success: () -> Unit, error: (Exception) -> Unit) {
    }

    override fun onWifiEnableChanged(callback: (Boolean) -> Unit) {
    }

    @MainThread
    override fun registerWifiListCallback(callback: WifiScanResultCallback) {
        scanResultCallbacks.add(callback)
    }

    @MainThread
    override fun unregisterWifiListCallback(callback: WifiScanResultCallback) {
        val iterator = scanResultCallbacks.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() == callback) {
                iterator.remove()
            }
        }
    }

    override fun cancelConnecting(wifiData: WifiData) {
    }

    override fun release() {
    }
}