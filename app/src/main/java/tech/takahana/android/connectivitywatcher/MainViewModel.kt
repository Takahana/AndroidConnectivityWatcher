package tech.takahana.android.connectivitywatcher

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.SharedFlow

@ActivityScoped
class MainViewModel @ViewModelInject constructor(
    @ApplicationContext appContext: Context
) : ViewModel() {

    private val connectivityWatcher by lazy { ConnectivityWatcher(appContext) }
    val connectivity: SharedFlow<ConnectivityStatus>
        get() = connectivityWatcher.status
}
