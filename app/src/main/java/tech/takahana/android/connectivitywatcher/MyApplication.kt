package tech.takahana.android.connectivitywatcher

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import tech.takahana.android.connectivitywatcher.di.AppComponent
import tech.takahana.android.connectivitywatcher.di.DaggerAppComponent

@HiltAndroidApp
open class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}
