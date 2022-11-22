package com.xixi.composed.wifi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiData
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.WifiType
import com.vivid.launcher.home.v2.dashboard.setting.detail.wifi.data.source.WifiRepository
import com.xixi.composed.wifi.data.source.hardware.ErrorPwdException
import com.xixi.composed.wifi.data.source.hardware.WifiCloseException
import com.xixi.composed.wifi.data.Async
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 *Jairett since 2022/7/22
 */
class WifiViewModel : ViewModel() {

    companion object {
        private const val AUTO_REFRESH_PERIOD = 5 * 1000L
    }

    private var connectJob: Job? = null
    private val TAG = "WifiViewModel"
    private val repository = WifiRepository
    private val uiStateMutableFlow by lazy {
        MutableStateFlow(run {
            val isWifiEnable = repository.isWifiOpen
            WifiUIState(isWifiEnable = isWifiEnable,
                list = if (isWifiEnable) Async.Uninitialized else Async.Error(WifiCloseException()))
        })
    }

    val uiStateFlow: StateFlow<WifiUIState>
        get() = uiStateMutableFlow

    private var autoJob: Job = newAutoJob

    private val newAutoJob: Job
        get() = viewModelScope.launch(start = CoroutineStart.LAZY) {
            loadWifiList()
            while (true) {
                delay(AUTO_REFRESH_PERIOD)
                loadWifiList()
            }
        }

    val curState: WifiUIState
        get() = uiStateMutableFlow.value

    private var curLoadTask: Deferred<List<WifiData>>? = null

    init {
        if (curState.isWifiEnable) {
            startAutoRefresh()
        }
        repository.onWifiEnableChanged { open ->
            if (open) {
                startAutoRefresh()
            } else {
                cancelAutoRefreshJob("cancel by wifi close")
                updateState { copy(isWifiEnable = false, list = Async.Error(WifiCloseException())) }
            }
        }
    }

    fun toggleWifiSwitch() {
        repository.isWifiOpen = !repository.isWifiOpen
    }

    fun refreshWifiList(): Boolean {
        if (curLoadTask?.isCompleted == false) {
            return false
        }
        if (connectJob?.isActive == true) {
            return false
        }
        cancelAutoRefreshJob("cause by refresh")
        viewModelScope.launch {
            loadWifiList()
            delay(AUTO_REFRESH_PERIOD)
            startAutoRefresh()
        }
        return true
    }

    fun connectToWifi(wifiData: WifiData, pwdJudgeContinuation: CancellableContinuation<String>.() -> Unit) {
        val connectWifi = curState.connectWifi
        if (connectWifi is Async.Loading && connectWifi.value?.SSID == wifiData.SSID) {
            return
        }
        connectJob?.cancel("other wifi start connect")
        connectJob = viewModelScope.launch {
            cancelAutoRefreshJob("cause by connect wifi")
            autoJob.join()
            updateState { copy(connectWifi = Async.Init) }
            val awaitPwdReady = suspendCancellableCoroutine<String> {
                it.invokeOnCancellation {
                    Log.d(TAG, "connectToWifi on cancel")
                    updateState { copy(connectWifi = Async.Uninitialized) }
                    startAutoRefresh()
                }
                it.pwdJudgeContinuation()
            }
            Log.d(TAG, "connect start loading")
            updateState { copy(connectWifi = Async.Loading(wifiData)) }
            try {
                val wD = repository.connectToWifi(wifiData, awaitPwdReady)
                updateState { copy(connectWifi = Async.Success(wD)) }
                startAutoRefresh()
            } catch (e: ErrorPwdException) {
                Log.d(TAG, "connectToWifi failed pwd error")
                loadWifiList()
                updateState { copy(connectWifi = Async.Error(ErrorPwdException(awaitPwdReady), wifiData)) }
            } catch (e: CancellationException) {
                Log.d(TAG, "connectToWifi failed cancel")
                // do nothing
            }
            Log.d(TAG, "connect coroutine end ")
        }
    }

    fun startAutoRefresh() {
        if (autoJob.isActive) {
            return
        }
        if (autoJob.isCancelled || autoJob.isCompleted) {
            autoJob = newAutoJob
            autoJob.start()
            return
        }
        autoJob.start()
    }

    fun cancelAutoRefreshJob(message: String) {
        autoJob.cancel(CancellationException(message))
    }

    private suspend fun loadWifiList() {
        supervisorScope {
            updateState { copy(isWifiEnable = true, list = Async.Loading((list as? Async.Success)?.value)) }
            val loadTask = async(start = CoroutineStart.LAZY, context = Dispatchers.IO) { repository.getWifiList() }
            curLoadTask = loadTask
            try {
                val list = loadTask.await()
                val curConnect = list.find { it.type == WifiType.Connect.Connected }
                updateState { copy(list = Async.Success(list), connectWifi = curConnect?.let { Async.Success(it) } ?: Async.Uninitialized) }
            } catch (e: Exception) {
                Log.d(TAG, "task await catch exception $e")
                handleWhenLoadException(e)
            }
            Log.d(TAG, "loadWifiList await end")
        }
    }

    private fun handleWhenLoadException(exception: Exception) {
        if (exception is WifiCloseException) {
            updateState { copy(isWifiEnable = false, list = Async.Error(exception)) }
        } else {
            updateState { copy(list = Async.Error(exception)) }
        }
    }

    private fun updateState(reducer: WifiUIState.() -> WifiUIState) {
        val localCurState = curState
        val newState = localCurState.reducer()
        if (newState == localCurState) {
            return
        }
        viewModelScope.launch { uiStateMutableFlow.emit(newState) }
    }
}