package com.gkgio.museum.activity

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.ext.applySchedulers
import com.gkgio.museum.activity.bluetooth.BleManagerContract
import com.gkgio.museum.activity.bluetooth.Resource
import com.gkgio.museum.ext.isNonInitialized
import com.gkgio.museum.ext.nonNullValue
import com.google.firebase.analytics.FirebaseAnalytics
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.altbeacon.beacon.Beacon
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainViewModel @Inject constructor(
    firebaseAnalytics: FirebaseAnalytics,
    private val bleManager: BleManagerContract,
    private val prefs: SharedPreferences
) : BaseViewModel() {

    val showErrorBluetoothNotAvailable = SingleLiveEvent<Unit>()
    val startEnableBluetoothIntent = SingleLiveEvent<Unit>()
    val askLocationPermission = SingleLiveEvent<Unit>()
    val enableLocationServices = SingleLiveEvent<Unit>()

    val state = MutableLiveData<State>()

    private var bluetoothScannerDisposable: Disposable? = null

    init {
        if (state.isNonInitialized()) {
            state.value = State()

            bluetoothScannerDisposable = Observable.interval(3, TimeUnit.SECONDS)
                .retry()
                .take(20)
                .applySchedulers()
                .subscribe {
                    if (bleManager.isScanning()) {
                        bleManager.stopScan()
                    } else {
                        bleManager.startScan()
                    }
                }

            bleManager.getResource()
                .applySchedulers()
                .subscribe(this::updateSearchedUser)
                .addDisposable()
        }
    }

    private fun updateSearchedUser(resource: Resource<ScanResult>) {
        when (resource) {
            is Resource.ErrorCodeId -> {
                when (resource.errorCode) {
                    BleScanException.BLUETOOTH_NOT_AVAILABLE -> {
                        bluetoothScannerDisposable?.dispose()
                        showErrorBluetoothNotAvailable.call()
                    }
                    BleScanException.BLUETOOTH_DISABLED -> {
                        startEnableBluetoothIntent.call()
                    }
                    BleScanException.LOCATION_PERMISSION_MISSING -> {
                        askLocationPermission.call()
                    }
                    BleScanException.LOCATION_SERVICES_DISABLED -> {
                        enableLocationServices.call()
                    }
                }
            }
        }
    }

    fun onNewBeaconDetected(beacon: Beacon) {
        state.value = state.nonNullValue.copy(
            currentIBeacon = beacon
        )
    }

    fun onLocationPermissionResultLoaded(result: Boolean) =
        if (result) {
            onLocationPermissionGranted()
        } else {
            onLocationPermissionDenied()
        }

    private fun onLocationPermissionGranted() {

    }

    private fun onLocationPermissionDenied() {

    }

    override fun onCleared() {
        super.onCleared()
        bluetoothScannerDisposable?.dispose()
    }

    data class State(
        val currentIBeacon: Beacon? = null
    )
}