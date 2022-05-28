package vn.metamon.data.service

import kotlinx.coroutines.flow.Flow
import vn.metamon.data.exception.ApiException
import vn.metamon.data.model.MetaResult
import vn.metamon.data.model.SignInResult
import vn.metamon.data.remote.GRpcClient
import vn.metamon.data.remote.isSuccess
import vn.metamon2.grpc.DeviceInfo
import vn.metamon2.grpc.PublicGrpc
import vn.metamon2.grpc.SignInRequest
import javax.inject.Inject

class GRpcPassportService @Inject constructor(
    gRpcClient: GRpcClient
) : BaseGRpcService(), PassportService {
    private val publicApiStub = PublicGrpc.newBlockingStub(gRpcClient.channel)

    override fun signInWithGoogle(
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String,
        token: String
    ): Flow<MetaResult<SignInResult>> {
        return execute {
            val requestBuilder = SignInRequest.newBuilder().also {
                it.device = DeviceInfo.newBuilder().also { device ->
                    device.deviceId = deviceId
                    device.deviceName = deviceName
                    device.appVersion = appVersion
                    device.platform = platform
                }.build()

                it.google = SignInRequest.Google.newBuilder().also { google ->
                    google.idToken = token
                }.build()

                it.autoCreate = true
            }

            doLogin(requestBuilder.build())
        }
    }

    override fun signInWithFacebook(
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String,
        token: String
    ): Flow<MetaResult<SignInResult>> {
        return execute {
            val requestBuilder = SignInRequest.newBuilder().also {
                it.device = DeviceInfo.newBuilder().also { device ->
                    device.deviceId = deviceId
                    device.deviceName = deviceName
                    device.appVersion = appVersion
                    device.platform = platform
                }.build()

                it.facebook = SignInRequest.Facebook.newBuilder().also { facebook ->
                    facebook.token = token
                }.build()

                it.autoCreate = true
            }

            doLogin(requestBuilder.build())
        }
    }

    override fun loginWithEmail(
        email: String,
        password: String,
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String
    ): Flow<MetaResult<SignInResult>> {
        return execute {
            val requestBuilder = SignInRequest.newBuilder().also {
                it.device = DeviceInfo.newBuilder().also { device ->
                    device.deviceId = deviceId
                    device.deviceName = deviceName
                    device.appVersion = appVersion
                    device.platform = platform
                }.build()

                it.emailPasswd = SignInRequest.EmailPassword.newBuilder()
                    .setEmail(email)
                    .setPasswd(password)
                    .build()

                it.autoCreate = true
            }
            doLogin(requestBuilder.build())
        }
    }

    private fun doLogin(
        request: SignInRequest,
    ): MetaResult<SignInResult> {
        val response = publicApiStub.signIn(request)
        return if (response.status.isSuccess()) {
            MetaResult.Success(SignInResult(response.profile.toProfile(), response.accessToken))
        } else {
            MetaResult.Error(ApiException(response.status.code, response.status.message))
        }
    }
}