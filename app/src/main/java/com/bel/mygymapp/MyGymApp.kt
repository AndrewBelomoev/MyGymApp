package com.bel.mygymapp

import android.app.Application
import com.bel.mygymapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyGymApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyGymApp)
            modules(appModule)
        }
    }
}
