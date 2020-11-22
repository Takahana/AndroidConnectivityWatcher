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
import kotlinx.coroutines.channels.ProducerScope
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

            override fun onAvailable(network: Network) = offerConnectivityStatus()

            override fun onLost(network: Network) = offerConnectivityStatus()
        }

        val builder = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // API LEVEL 23以上ならNetworkCapabilities.NET_CAPABILITY_VALIDATEDも指定
            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.shareIn(
        ProcessLifecycleOwner.get().lifecycleScope,
        SharingStarted.WhileSubscribed(),
        1
    )


    private fun ProducerScope<ConnectivityStatus>.offerConnectivityStatus() {
        offer(ConnectivityStatus(connectivityManager))
    }

    class ConnectivityStatus(private val connectivityManager: ConnectivityManager) {

        /**
         * @return boolean Whether or not there is an Internet connection
         */
        fun isEnabled(): Boolean {
            return getActiveNetworkCapabilities().isNotEmpty()
        }

        /**
         * @return boolean Whether or not there is a Wifi Internet connection
         */
        fun isWiFiEnabled(): Boolean {
            return getActiveNetworkCapabilities().any {
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        }

        /**
         * @return boolean Whether or not there is a Cellular internet connection
         */
        fun isCellularEnabled(): Boolean {
            return getActiveNetworkCapabilities().any {
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            }
        }

        private fun getActiveNetworkCapabilities(): List<NetworkCapabilities> {
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
}
