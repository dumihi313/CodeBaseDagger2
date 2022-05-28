package vn.metamon.data.model

import vn.metamon.data.remote.GRpcClient
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

interface ConnectionManager {
    fun registerStatusListener(listener: StatusListener)

    fun unregisterStatusListener(listener: StatusListener)

    fun disconnect()

    interface StatusListener {
        fun onConnected()

        fun onDisconnected()

        fun onReconnected()
    }

    abstract class StatusListenerAdapter : StatusListener {
        override fun onConnected() {
        }

        override fun onDisconnected() {
        }

        override fun onReconnected() {
        }
    }
}

internal class GRpcConnectionManager @Inject constructor(
    private val gRpcClient: GRpcClient
) : ConnectionManager {
    private val statusListeners: MutableSet<ConnectionManager.StatusListener> =
        Collections.newSetFromMap(ConcurrentHashMap())

    init {
        gRpcClient.clientStateListener = object : ConnectionManager.StatusListener {
            override fun onConnected() {
                val snapshot = getListenerSnapshot()
                for (l in snapshot) {
                    l.onConnected()
                }
            }

            override fun onDisconnected() {
                val snapshot = getListenerSnapshot()
                for (l in snapshot) {
                    l.onDisconnected()
                }
            }

            override fun onReconnected() {
                val snapshot = getListenerSnapshot()
                for (l in snapshot) {
                    l.onReconnected()
                }
            }
        }
    }

    private fun getListenerSnapshot(): Set<ConnectionManager.StatusListener> {
        val snapshot = HashSet<ConnectionManager.StatusListener>()
        val iterator = statusListeners.iterator()
        while (iterator.hasNext()) {
            snapshot.add(iterator.next())
        }

        return snapshot
    }

    override fun registerStatusListener(listener: ConnectionManager.StatusListener) {
        statusListeners.add(listener)
    }

    override fun unregisterStatusListener(listener: ConnectionManager.StatusListener) {
        statusListeners.remove(listener)
    }

    override fun disconnect() {
        gRpcClient.disconnect()
    }
}
