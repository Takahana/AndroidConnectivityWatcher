package tech.takahana.android.connectivitywatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeConnectivity()
    }

    private fun observeConnectivity() {
        val watcher = ConnectivityWatcher(this)
        lifecycleScope.launch {
            watcher.statusFlow.collect { status ->
                if (!status.isConnected()) {
                    // Disconnected action
                    Log.v("CONN", "disconnected")
                } else {
                    // switch wifi or cellular
                    if (status.isCellular()) {
                        Log.v("CONN", "cellular is available")
                    }
                    if (status.isWiFi()) {
                        Log.v("CONN", "wifi is available")
                    }
                }
            }
        }
    }
}
