package com.gkgio.museum.activity

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.RemoteException
import android.provider.Settings
import android.view.View
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.R
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.ext.observeValue
import com.gkgio.museum.ext.requestLocationPermission
import com.gkgio.museum.feature.audios.AudioPlayerSheet
import com.gkgio.museum.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_main.*
import org.altbeacon.beacon.*
import timber.log.Timber

class MainFragment : BaseFragment<MainViewModel>(), BottomBarTabsSwitcher, BeaconConsumer,
    RangeNotifier {

    private companion object {

        val TAG = MainFragment::class.java.simpleName

        private const val PAGE_CACHE_SIZE = 4
        private const val PAGE_MUSEUMS = 0
        private const val PAGE_AUDIOS = 1
        private const val PAGE_SETTINGS = 2
    }

    private var beaconManager: BeaconManager? = null

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.mainViewModel
    }

    override fun onResume() {
        super.onResume()
        beaconManager = AppInjector.appComponent.provideBeaconManager()

        if (beaconManager?.isBound(this) != true) {
            Timber.d("binding beaconManager")
            beaconManager?.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewPager.adapter = MainPagerAdapter(childFragmentManager)
        mainViewPager.offscreenPageLimit = PAGE_CACHE_SIZE


        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab_museums -> {
                    mainViewPager.setCurrentItem(0, false)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab_audios -> {
                    mainViewPager.setCurrentItem(1, false)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab_profile -> {
                    mainViewPager.setCurrentItem(2, false)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        viewModel.askLocationPermission.observeValue(this) {
            checkLocationPermission()
        }

        viewModel.enableLocationServices.observeValue(this) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        viewModel.showErrorBluetoothNotAvailable.observeValue(this) {
            showErrorBluetoothNotAvailable()
        }

        viewModel.startEnableBluetoothIntent.observeValue(this) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBtIntent)
        }

        viewModel.state.observeValue(this) { state ->
            state.currentIBeacon?.let {
                showDialog(AudioPlayerSheet.newInstance(it), TAG)
            }
        }
    }

    override fun switchToMuseumsTab() {
        bottomNavigation.selectedItemId = R.id.tab_museums
        mainViewPager.setCurrentItem(PAGE_MUSEUMS, false)
    }

    override fun switchToAudiosTab() {
        bottomNavigation.selectedItemId = R.id.tab_audios
        mainViewPager.setCurrentItem(PAGE_AUDIOS, false)
    }

    override fun switchToProfileTab() {
        bottomNavigation.selectedItemId = R.id.tab_profile
        mainViewPager.setCurrentItem(PAGE_SETTINGS, false)
    }

    override fun onStop() {
        super.onStop()
        unbindBeaconManager()
    }


    private fun unbindBeaconManager() {
        if (beaconManager?.isBound(this) == true) {
            Timber.d("Unbinding from beaconManager")
            beaconManager?.unbind(this)
        }
    }

    private fun showErrorBluetoothNotAvailable() {
        DialogUtils.showError(
            view, getString(R.string.bluetooth_disabled)
        )
    }

    private fun checkLocationPermission() {
        requestLocationPermission()
            .subscribe {
                viewModel.onLocationPermissionResultLoaded(it)
            }.addDisposable()
    }

    override fun getApplicationContext(): Context {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unbindService(p0: ServiceConnection?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBeaconServiceConnect() {
        Timber.d("beaconManager is bound, ready to start scanning")
        beaconManager?.addRangeNotifier(this)

        try {
            beaconManager?.startRangingBeaconsInRegion(
                Region(
                    "com.gkgio.museum",
                    null,
                    null,
                    null
                )
            )
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, p1: Region?) {
        beacons ?: return

        val beacon = beacons.minBy { it.distance }
        beacon?.let {
            if (beacon.distance < 2) {
                viewModel.onNewBeaconDetected(beacon)
            }
        }
    }
}

interface BottomBarTabsSwitcher {
    fun switchToMuseumsTab()
    fun switchToAudiosTab()
    fun switchToProfileTab()
}
