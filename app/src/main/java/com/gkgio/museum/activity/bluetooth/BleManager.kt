package com.gkgio.museum.activity.bluetooth

import com.gkgio.museum.ext.applySchedulers
import com.jakewharton.rxrelay2.PublishRelay
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class BleManager @Inject constructor(
    private val rxBleClient: RxBleClient
) : BleManagerContract {

    private lateinit var disposeScan: Disposable
    private var isScan: Boolean = false
    private var rxRelay: PublishRelay<Resource<ScanResult>> = PublishRelay.create()

    override fun startScan() {
        isScan = true
        disposeScan = (rxBleClient.scanBleDevices(ScanSettings.Builder().build())
            .applySchedulers()
            .doOnDispose { onDoOnDispose() }
            .doOnSubscribe { onDoOnSubscribe() }
            .subscribe(this::onScanResult, this::onScanFailure))
    }

    override fun getResource(): Flowable<Resource<ScanResult>> {
        return rxRelay.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun stopScan() {
        isScan = false
        disposeScan.dispose()
    }

    override fun isScanning(): Boolean {
        return isScan
    }

    private fun onDoOnDispose() {
        rxRelay.accept(Resource.Stopped())
    }

    private fun onDoOnSubscribe() {
        rxRelay.accept(Resource.StartingResource())
    }

    private fun onScanResult(result: ScanResult) {
        rxRelay.accept(Resource.SuccessResource(result))
    }

    private fun onScanFailure(reason: Throwable) {
        rxRelay.accept(Resource.ErrorCodeId((reason as BleScanException).reason))
    }
}

