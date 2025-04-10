package com.rcl.kduopass

import android.app.Application
import com.rcl.kduopass.di.AndroidPlatformComponent
import com.rcl.kduopass.di.AppComponent
import com.rcl.kduopass.di.create

class App : Application() {
    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        val platformComponent = AndroidPlatformComponent::class.create(this.applicationContext)
        appComponent = AppComponent::class.create(platformComponent)
    }
    fun appComponent(): AppComponent = appComponent
}