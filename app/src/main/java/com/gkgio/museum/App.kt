package com.gkgio.museum

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.di.AppModule
import com.gkgio.museum.di.DaggerAppComponent
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initDagger()
        initTimber()
        initRxErrorHandler()
    }

    private fun initDagger(){
        val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        AppInjector.appComponent = appComponent
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initRxErrorHandler() {
        RxJavaPlugins.setErrorHandler {
            Timber.e(it, "Rx error")
            // TODO логировать в релизе ошибки в ремоут
        }
    }
}