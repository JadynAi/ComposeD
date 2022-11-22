package com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.annotation.IntRange
import com.xixi.composed.R

/**
 *Jairett since 2022/3/10
 */
data class WifiData(val scanResult: ScanResult,
                    @IntRange(from = 0, to = 3) val signalLevel: Int,
                    val type: WifiType) {
    companion object {
        private const val MAX_LEVEL = 10
        private const val MEDIUM_LEVEL = 5
    }

    val SSID: String
        get() = scanResult.SSID

    fun getSignalLevelDrawable(): Int {
        return when (signalLevel) {
            0 ->  R.drawable.ic_ico_wifi_low
            1 ->  R.drawable.ic_ico_wifi_mid
            2 ->  R.drawable.ic_ico_wifi_high
            3 ->  R.drawable.ic_ico_wifi_full
            else -> R.drawable.ic_ico_wifi_low
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WifiData

        if (signalLevel != other.signalLevel) return false
        if (type != other.type) return false
        if (SSID != other.SSID) return false

        return true
    }

    override fun hashCode(): Int {
        var result = signalLevel
        result = 31 * result + type.hashCode()
        result = 31 * result + SSID.hashCode()
        return result
    }

    val sortLevel: Int
        get() {
            if (type is WifiType.Connect) {
                return MAX_LEVEL
            } else if (type is WifiType.Saved) {
                return MEDIUM_LEVEL + signalLevel
            }
            return signalLevel
        }
    
    
}

sealed class WifiType {

    val isNeedPwdToConnect: Boolean
        get() = this is New || this is Saved.UseToConnected

    val hasEverConnected: Boolean
        get() = this is Saved.EverConnected

    object Open : WifiType()

    // Ever is connected succeed
    sealed class Saved : WifiType() {
        object EverConnected : Saved()
        object UseToConnected : Saved()
    }

    object New : WifiType()
    
    sealed class Connect : WifiType() {
        object Connecting : Connect()
        object Connected : Connect()
    }
}
