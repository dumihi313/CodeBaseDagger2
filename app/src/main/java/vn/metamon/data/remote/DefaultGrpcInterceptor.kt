package vn.metamon.data.remote

import io.grpc.*
import vn.metamon.app.DeviceInfo
import vn.metamon.app.di.AppVersion
import vn.metamon.app.di.PlatformCode
import vn.metamon.data.MetamonUserManager
import javax.inject.Inject

class DefaultGrpcInterceptor @Inject constructor(
    private val userManager: MetamonUserManager,
    @PlatformCode private val platform: String,
    private val deviceInfo: DeviceInfo,
    @AppVersion private val appVersion: String,
) : ClientInterceptor {
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
            override fun start(responseListener: Listener<RespT>?, headers: Metadata?) {
                val defaultHeaders = Metadata()
                val tokenKey = Metadata.Key.of(AUTHORIZATION, Metadata.ASCII_STRING_MARSHALLER)
                val platform = Metadata.Key.of(PLATFORM, Metadata.ASCII_STRING_MARSHALLER)
                val appVersion = Metadata.Key.of(APP_VERSION, Metadata.ASCII_STRING_MARSHALLER)
                val deviceId = Metadata.Key.of(DEVICE_ID, Metadata.ASCII_STRING_MARSHALLER)
                val deviceName = Metadata.Key.of(DEVICE_NAME, Metadata.ASCII_STRING_MARSHALLER)
                defaultHeaders.put(tokenKey, userManager.currentUser.credential.accessToken)
                defaultHeaders.put(platform, this@DefaultGrpcInterceptor.platform)
                defaultHeaders.put(appVersion, this@DefaultGrpcInterceptor.appVersion)
                defaultHeaders.put(deviceId, deviceInfo.deviceId)
                defaultHeaders.put(deviceName, deviceInfo.deviceName)

                headers?.merge(defaultHeaders)
                super.start(responseListener, headers)
            }
        }
    }
}