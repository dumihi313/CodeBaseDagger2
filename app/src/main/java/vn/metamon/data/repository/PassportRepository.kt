package vn.metamon.data.repository

import kotlinx.coroutines.flow.Flow
import vn.metamon.data.model.MetaResult
import vn.metamon.data.model.SignInResult

interface PassportRepository {
    fun signInWithGoogle(
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String,
        token: String
    ): Flow<MetaResult<SignInResult>>

    fun signInWithFacebook(
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String,
        token: String
    ): Flow<MetaResult<SignInResult>>

    fun loginWithEmail(
        email: String,
        password: String,
        deviceId: String,
        deviceName: String,
        appVersion: String,
        platform: String,
    ): Flow<MetaResult<SignInResult>>
}