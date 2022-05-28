package vn.metamon.data.remote

import android.util.Log
import io.grpc.*
import vn.metamon.BuildConfig
import vn.metamon.data.model.ConnectionManager
import java.util.concurrent.TimeUnit

private const val TAG = "GRpcClient"

class GRpcClient constructor(
    host: String,
    port: Int? = null,
    usePlainText: Boolean = false,
    interceptor: ClientInterceptor? = null
) {
    val channel: Channel
        get() = originalChannel

    private val originalChannel: ManagedChannel

    var clientStateListener: ConnectionManager.StatusListener? = null

    init {
        val channelBuilder = if (port == null) {
            ManagedChannelBuilder.forTarget(host)
        } else {
            ManagedChannelBuilder.forAddress(host, port)
        }

        if (usePlainText) {
            channelBuilder.usePlaintext()
        }

        val interceptors = arrayOf(
            interceptor,
            createLoggingInterceptor()
        ).filterNotNull()

        originalChannel = channelBuilder.intercept(interceptors)
            .build()

        val state = originalChannel.getState(false)
        originalChannel.notifyWhenStateChanged(state, GRpcStateChangeListener(state))
    }

    fun shutdownAndWait() {
        originalChannel.shutdownNow().awaitTermination(10, TimeUnit.SECONDS)
    }

    fun disconnect() {
        originalChannel.enterIdle()
    }

    private fun createLoggingInterceptor(): ClientInterceptor? {
        if (!BuildConfig.DEBUG) {
            return null
        }

        return object : ClientInterceptor {
            override fun <ReqT : Any?, RespT : Any?> interceptCall(
                method: MethodDescriptor<ReqT, RespT>?,
                callOptions: CallOptions?,
                next: Channel?
            ): ClientCall<ReqT, RespT> {
                return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                    next?.newCall(
                        method,
                        callOptions
                    )
                ) {
                    override fun sendMessage(message: ReqT) {
                        Log.d(
                            TAG,
                            """${next?.authority()}/${method?.fullMethodName}:
                                |$message""".trimMargin()
                        )
                        super.sendMessage(message)
                    }

                    override fun start(responseListener: Listener<RespT>?, headers: Metadata?) {
                        Log.d(
                            TAG,
                            """${next?.authority()}/${method?.fullMethodName}:
                                    |Headers: $headers
                                """.trimMargin()
                        )
                        val wrapped = object : Listener<RespT>() {
                            override fun onHeaders(headers: Metadata?) {
                                Log.d(
                                    TAG,
                                    """${next?.authority()}/${method?.fullMethodName}:
                                    |Headers: $headers
                                """.trimMargin()
                                )
                                responseListener?.onHeaders(headers)
                            }

                            override fun onClose(status: Status?, trailers: Metadata?) {
                                responseListener?.onClose(status, trailers)
                            }

                            override fun onReady() {
                                responseListener?.onReady()
                            }

                            override fun onMessage(message: RespT) {
                                Log.d(
                                    TAG,
                                    """${next?.authority()}/${method?.fullMethodName}:
                                        |Response: $message
                                    """.trimMargin()
                                )
                                responseListener?.onMessage(message)
                            }
                        }
                        super.start(wrapped, headers)
                    }
                }
            }
        }
    }

    private inner class GRpcStateChangeListener(
        private val previousState: ConnectivityState?
    ) : Runnable {
        override fun run() {
            val newState = originalChannel.getState(false)
            if (newState == ConnectivityState.CONNECTING || newState == previousState) {
                originalChannel.notifyWhenStateChanged(newState, GRpcStateChangeListener(previousState))
                return
            }

            originalChannel.notifyWhenStateChanged(newState, GRpcStateChangeListener(newState))

            if (newState == ConnectivityState.READY) {
                if (previousState == ConnectivityState.TRANSIENT_FAILURE) {
                    clientStateListener?.onReconnected()
                } else {
                    clientStateListener?.onConnected()
                }
                return
            }

            if (newState == ConnectivityState.TRANSIENT_FAILURE) {
                clientStateListener?.onDisconnected()
                return
            }
        }
    }
}