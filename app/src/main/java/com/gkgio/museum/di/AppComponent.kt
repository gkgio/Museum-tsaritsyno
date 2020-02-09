package com.gkgio.museum.di

import android.app.Application
import android.content.Context
import com.gkgio.museum.feature.profile.ProfileViewModel
import com.gkgio.museum.activity.LaunchActivity
import com.gkgio.museum.activity.LaunchViewModel
import com.gkgio.museum.activity.MainViewModel
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.feature.audios.AudioPlayerViewModel
import com.gkgio.museum.feature.audios.AudiosViewModel
import com.gkgio.museum.feature.auth.AuthViewModel
import com.gkgio.museum.feature.museum.MuseumsViewModel
import com.gkgio.museum.feature.museum.detail.MuseumDetailContainerViewModel
import com.gkgio.museum.feature.museum.detail.MuseumDetailViewModel
import com.gkgio.museum.feature.museum.detail.items.ItemsViewModel
import com.gkgio.museum.feature.splash.SplashViewModel
import com.gkgio.museum.feature.start.StartViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.moshi.Moshi
import dagger.Component
import org.altbeacon.beacon.BeaconManager
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NavigationModule::class,
        AppModule::class,
        BluetoothModule::class
    ]
)
interface AppComponent {

    fun inject(launchActivity: LaunchActivity)
    fun inject(baseFragment: BaseFragment<BaseViewModel>)

    val moshi: Moshi
    val context: Context

    val launchViewModel: LaunchViewModel
    val mainViewModel: MainViewModel
    val profileViewModel: ProfileViewModel
    val museumsViewModel: MuseumsViewModel
    val splashViewModel: SplashViewModel
    val audiosViewModel: AudiosViewModel
    val authViewModel: AuthViewModel
    val startViewModel: StartViewModel
    val museumsDetailContainerViewModel: MuseumDetailContainerViewModel
    val museumsDetailViewModel: MuseumDetailViewModel
    val itemsViewModel: ItemsViewModel
    val audioPlayerViewModel: AudioPlayerViewModel

    fun inject(app: Application)

    //analytics
    val firebaseAnalytics: FirebaseAnalytics

    //Beacon manager
    fun provideBeaconManager(): BeaconManager

    //Cicerone
    val router: Router
    val navigatorHolder: NavigatorHolder
}