package tech.takahana.android.connectivitywatcher

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class MainViewModel @ViewModelInject constructor(
    @ApplicationContext appContext: Context
) : ViewModel() {

    val watcher by lazy { ConnectivityWatcher(appContext) }
}
