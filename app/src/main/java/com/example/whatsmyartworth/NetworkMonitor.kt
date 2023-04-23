package com.example.whatsmyartworth

import android.app.Activity
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.ConnectivityManager
import android.net.Network
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.google.android.material.snackbar.Snackbar

object NetworkMonitor {

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        var callback: ConnectivityManager.NetworkCallback? = null
        fun register(view: View){
            val connectivityManager = view.context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
            callback = object : ConnectivityManager.NetworkCallback() {
                // network is available for use
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Snackbar.make(view, "We are connected to the internet", Snackbar.LENGTH_SHORT)
                        .show()
                }

                // Network capabilities have changed for the network
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                }

                // lost network connection
                override fun onLost(network: Network) {
                    super.onLost(network)
                    Snackbar.make(view, "We are not connected to the internet", Snackbar.LENGTH_INDEFINITE)
                        .show()

                }
            }

            connectivityManager.requestNetwork(networkRequest, callback!! )


        }
        fun unregister(activity:Activity){
            if (callback != null) {
                val connectivityManager =
                    activity.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
                connectivityManager.unregisterNetworkCallback(callback!!)
                callback = null
            }
        }



}