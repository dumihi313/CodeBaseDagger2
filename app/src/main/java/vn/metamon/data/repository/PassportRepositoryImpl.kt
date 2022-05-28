package vn.metamon.data.repository

import kotlinx.coroutines.flow.Flow
import vn.metamon.data.model.MetaResult
import vn.metamon.data.model.SignInResult
import vn.metamon.data.service.PassportService
import javax.inject.Inject

class PassportRepositoryImpl @Inject constructor(
    private val passportService: PassportService
) : PassportRepository {

    override fun signInWithGoogle(
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String,
        token: String
    ): Flow<MetaResult<SignInResult>> {
        return passportService.signInWithGoogle(deviceId, deviceName, appVersion, platform, token)
    }

    override fun signInWithFacebook(
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String,
        token: String
    ): Flow<MetaResult<SignInResult>> {
        return passportService.signInWithFacebook(
            deviceId,
            deviceName,
            appVersion,
            platform,
            token
        )
    }

    override fun loginWithEmail(
        email: String,
        password: String,
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String,
    ): Flow<MetaResult<SignInResult>> {
        return passportService.loginWithEmail(
            email,
            password,
            deviceId,
            deviceName,
            appVersion,
            platform,
        )
    }

}