package tech.takahana.android.connectivitywatcher

import android.net.NetworkCapabilities

class ConnectivityStatus(private val networkCapabilities: List<NetworkCapabilities>) {

    /**
     * @return boolean Whether or not there is an Internet connection
     */
    fun isEnabled(): Boolean {
        return networkCapabilities.isNotEmpty()
    }

    /**
     * @return boolean Whether or not there is a Wifi Internet connection
     */
    fun isWiFiEnabled(): Boolean {
        return networkCapabilities.any {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }

    /**
     * @return boolean Whether or not there is a Cellular internet connection
     */
    fun isCellularEnabled(): Boolean {
        return networkCapabilities.any {
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }
}
