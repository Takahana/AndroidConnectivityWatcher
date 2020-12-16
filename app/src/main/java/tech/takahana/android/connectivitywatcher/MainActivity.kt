package tech.takahana.android.connectivitywatcher

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import tech.takahana.android.connectivitywatcher.extension.showToast

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeConnectivity()
    }

    private fun observeConnectivity() {
        lifecycleScope.launchWhenResumed {
            mainViewModel.connectivity.collectLatest {
                if (!it.isEnabled()) {
                    // Disconnected action
                    showToast("No Internet connection")
                } else {
                    if (it.isCellularEnabled()) {
                        showToast("Cellular Internet connection is enabled")
                    }
                    if (it.isWiFiEnabled()) {
                        showToast("Wifi Internet connection is enabled")
                    }
                }
            }
        }
    }
}
