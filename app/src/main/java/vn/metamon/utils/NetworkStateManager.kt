package vn.metamon.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import vn.metamon.app.di.AppScoped
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AppScoped
class NetworkStateManager @Inject constructor(context: Context) {
    private fun log(msg: String) {
        Log.d("NetworkStateManager", msg)
    }

    val isAvailable: Boolean
        get() = _isAvailable.get()

    private var _isAvailable: AtomicBoolean = AtomicBoolean(false)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isAvailable.set(true)
            listenersSnapshot().forEach {
                it.onAvailable()
            }
        }

        override fun onLost(network: Network) {
            _isAvailable.set(false)
            listenersSnapshot().forEach {
                it.onLost()
            }
        }
    }

    private val listeners = mutableSetOf<Listener>()

    init {
        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (PlatformUtils.isAtLeastN()) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    fun addListener(listener: Listener) {
        synchronized(listeners) {
            listeners.add(listener)
        }
    }

    fun unregister(listener: Listener) {
        synchronized(listeners) {
            listeners.remove(listener)
        }
    }

    private fun listenersSnapshot(): Set<Listener> {
        synchronized(listeners) {
            return HashSet(listeners)
        }
    }

    interface Listener {
        fun onAvailable()

        fun onLost()
    }
}