package com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.source

import android.os.RemoteException
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiData
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiType
import com.xixi.composed.wifi.data.source.hardware.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.jvm.Throws

/**
 *Jairett since 2022/7/22
 */
object WifiRepository {

    private val iWifi: IWifi by lazy { NormalWifiImpl() }

    var isWifiOpen by iWifi::isWifiOpen

    val onWifiEnableChanged = iWifi::onWifiEnableChanged

    @Throws(WifiCloseException::class, RemoteException::class, UnsupportedOperationException::class)
    suspend fun getWifiList() = suspendCancellableCoroutine<List<WifiData>> {
        val callback = object : WifiScanResultCallback() {
            override fun onSucceed(data: List<WifiData>?) {
                iWifi.unregisterWifiListCallback(this)
                it.resume(data ?: emptyList())
            }

            override fun onFailed(e: Exception) {
                iWifi.unregisterWifiListCallback(this)
                it.resumeWithException(e)
            }
        }
        iWifi.registerWifiListCallback(callback)
        it.invokeOnCancellation {
            iWifi.unregisterWifiListCallback(callback)
        }
        val scanWifi = iWifi.startScanWifi()
        if (!scanWifi) {
            iWifi.unregisterWifiListCallback(callback)
            it.resumeWithException(WifiCloseException())
            return@suspendCancellableCoroutine
        }
    }

    @Throws(ErrorPwdException::class)
    suspend fun connectToWifi(wifiData: WifiData, pwd: String) = suspendCancellableCoroutine<WifiData> { con ->
        con.invokeOnCancellation { iWifi.cancelConnecting(wifiData) }
        val success = { con.resume(wifiData) }
        val error: (Exception) -> Unit = { con.resumeWithException(it) }
        when (val type = wifiData.type) {
            WifiType.New -> iWifi.connectNewWifi(wifiData, pwd, success, error)
            is WifiType.Saved -> {
                if (type.hasEverConnected) {
                    iWifi.connectSavedWifi(wifiData, success, error)
                } else {
                    iWifi.connectNewWifi(wifiData, pwd, success, error)
                }
            }
            WifiType.Open -> iWifi.connectOpenWifi(wifiData, success, error)
            is WifiType.Connect -> con.resume(wifiData)
        }
    }
}