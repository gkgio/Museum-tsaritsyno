package com.gkgio.museum.di

import android.content.Context
import com.gkgio.museum.activity.bluetooth.BleManager
import com.gkgio.museum.activity.bluetooth.BleManagerContract
import com.polidea.rxandroidble2.RxBleClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import javax.inject.Singleton

@Module(includes = [BluetoothModule.BindsModule::class])
class BluetoothModule {
    companion object {
        const val RUUVI_LAYOUT = "m:0-2=0499,i:4-19,i:20-21,i:22-23,p:24-24" // TBD
        const val IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"
        const val ALTBEACON_LAYOUT = BeaconParser.ALTBEACON_LAYOUT
        const val EDDYSTONE_UID_LAYOUT = BeaconParser.EDDYSTONE_UID_LAYOUT
        const val EDDYSTONE_URL_LAYOUT = BeaconParser.EDDYSTONE_URL_LAYOUT
        const val EDDYSTONE_TLM_LAYOUT = BeaconParser.EDDYSTONE_TLM_LAYOUT
    }

    @Singleton
    @Provides
    fun provideRxBleClient(context: Context): RxBleClient {
        return RxBleClient.create(context)
    }

    @Provides
    @Singleton
    fun provideBeaconManager(context: Context): BeaconManager {
        val instance = BeaconManager.getInstanceForApplication(context)

        // Sets the delay between each scans according
        instance.foregroundBetweenScanPeriod = 2000

        // Add all the beacon types we want to discover
        instance.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_LAYOUT))
        instance.beaconParsers.add(BeaconParser().setBeaconLayout(EDDYSTONE_UID_LAYOUT))
        instance.beaconParsers.add(BeaconParser().setBeaconLayout(EDDYSTONE_URL_LAYOUT))
        instance.beaconParsers.add(BeaconParser().setBeaconLayout(EDDYSTONE_TLM_LAYOUT))

        return instance
    }

    @Module
    abstract inner class BindsModule {
        @Binds
        abstract fun bindBleManager(bleManger: BleManager): BleManagerContract
    }
}
