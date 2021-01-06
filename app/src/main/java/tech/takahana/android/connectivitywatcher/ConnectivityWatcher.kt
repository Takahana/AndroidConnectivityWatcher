package tech.takahana.android.connectivitywatcher

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn

@ExperimentalCoroutinesApi
class ConnectivityWatcher(private val context: Context) {

    private val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val status = callbackFlow<ConnectivityStatus> {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                offer(ConnectivityStatus(getNetworkCapabilities()))
            }

            override fun onLost(network: Network) {
                offer(ConnectivityStatus(getNetworkCapabilities()))
            }
        }

        val builder = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Also adds NetworkCapabilities.NET_CAPABILITY_VALIDATED if the API LEVEL 23 or higher
            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
        .buffer(onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .shareIn(
            ProcessLifecycleOwner.get().lifecycleScope,
            SharingStarted.WhileSubscribed(),
            replay = 1
        )

    private fun getNetworkCapabilities(): List<NetworkCapabilities> {
        return connectivityManager.allNetworks
            .mapNotNull { network ->
                connectivityManager.getNetworkCapabilities(network)
            }
            .filter {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                            it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                } else {
                    it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                }
            }
    }
}
