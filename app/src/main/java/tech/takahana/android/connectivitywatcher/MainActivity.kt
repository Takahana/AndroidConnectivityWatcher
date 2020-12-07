package tech.takahana.android.connectivitywatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import tech.takahana.android.connectivitywatcher.extension.showToast

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val watcher by lazy {
        ConnectivityWatcher(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeConnectivity()
    }

    private fun observeConnectivity() {
        lifecycleScope.launchWhenResumed {
            watcher.status.collect { status ->
                if (!status.isEnabled()) {
                    // Disconnected action
                    showToast("No Internet connection")
                } else {
                    if (status.isCellularEnabled()) {
                        showToast("Cellular Internet connection is enabled")
                    }
                    if (status.isWiFiEnabled()) {
                        showToast("Wifi Internet connection is enabled")
                    }
                }
            }
        }
    }
}
