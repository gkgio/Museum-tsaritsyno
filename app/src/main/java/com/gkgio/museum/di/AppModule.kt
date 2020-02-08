package com.gkgio.museum.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.gkgio.museum.base.ResourceManager
import com.gkgio.museum.base.ResourceManagerImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule.BindsModule::class])
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context = app.applicationContext

    @Singleton
    @Provides
    fun providePrefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Module
    abstract inner class BindsModule {

        @Binds
        abstract fun ResourceManager(arg: ResourceManagerImpl): ResourceManager

        /* @Binds
         abstract fun bindErrorReporter(arg: ErrorReporterImpl): ErrorReporter*/
    }
}