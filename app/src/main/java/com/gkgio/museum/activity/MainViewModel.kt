package com.gkgio.museum.activity

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.ext.applySchedulers
import com.gkgio.museum.activity.bluetooth.BleManagerContract
import com.gkgio.museum.activity.bluetooth.Resource
import com.gkgio.museum.base.ResourceManager
import com.gkgio.museum.ext.isNonInitialized
import com.gkgio.museum.ext.nonNullValue
import com.gkgio.museum.feature.model.Item
import com.gkgio.museum.utils.AudioDissmisEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.altbeacon.beacon.Beacon
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainViewModel @Inject constructor(
    firebaseAnalytics: FirebaseAnalytics,
    private val bleManager: BleManagerContract,
    private val prefs: SharedPreferences,
    private val resourceManager: ResourceManager,
    private val moshi: Moshi,
    private val audioDissmisEvent: AudioDissmisEvent
) : BaseViewModel() {

    companion object {
        private const val ITEMS_FILENAME = "mock_items.json"
    }

    val showErrorBluetoothNotAvailable = SingleLiveEvent<Unit>()
    val startEnableBluetoothIntent = SingleLiveEvent<Unit>()
    val askLocationPermission = SingleLiveEvent<Unit>()
    val enableLocationServices = SingleLiveEvent<Unit>()

    val state = MutableLiveData<State>()
    var countCheck = 0

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
        findItemByBeacon(beacon)?.let {
            if (it != state.nonNullValue.currentItem) {
                audioDissmisEvent.onComplete(0)
                if (countCheck > 0) {
                    countCheck = 0
                    state.value = state.nonNullValue.copy(
                        currentItem = it
                    )
                } else {
                    countCheck++
                }
            }
        }
    }

    private fun findItemByBeacon(beacon: Beacon): Item? {
        resourceManager.getJsonFromAssets(ITEMS_FILENAME)?.let { json ->
            val type = Types.newParameterizedType(List::class.java, Item::class.java)
            val adapter = moshi.adapter<List<Item>>(type)

            adapter.fromJson(json)?.let { array ->
                array.forEach {
                    if (it.ibeaconUuid == beacon.id1.toString()
                        && it.ibeaconMajorId == beacon.id2.toString()
                    ) {
                        return it
                    }
                }
            }
        }

        return null
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
        val currentItem: Item? = null
    )
}