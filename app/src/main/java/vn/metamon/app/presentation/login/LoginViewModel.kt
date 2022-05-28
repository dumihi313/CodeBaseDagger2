package vn.metamon.app.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import vn.metamon.app.DeviceInfo
import vn.metamon.app.di.AppVersion
import vn.metamon.app.di.PlatformCode
import vn.metamon.data.MetamonUserManager
import vn.metamon.data.model.*
import vn.metamon.data.repository.PassportRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val passportRepository: PassportRepository,
    private val deviceInfo: DeviceInfo,
    @AppVersion private val appVersion: String,
    @PlatformCode private val platform: String,
    private val userManager: MetamonUserManager
) : ViewModel() {

    private val _loginResult = MutableLiveData<MetaResult<Boolean>>()
    val loginResult: LiveData<MetaResult<Boolean>>
        get() = _loginResult

    private val _logoutResult = MutableLiveData<MetaResult<Boolean>>()
    val logoutResult: LiveData<MetaResult<Boolean>>
        get() = _logoutResult

    fun loginGoogle(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            passportRepository.signInWithGoogle(
                deviceInfo.deviceId,
                deviceInfo.deviceName,
                appVersion,
                platform,
                token
            ).collect { onLoginResult(it, User.LoginFrom.GOOGLE) }
        }
    }

    fun loginFacebook(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            passportRepository.signInWithFacebook(
                deviceInfo.deviceId,
                deviceInfo.deviceName,
                appVersion,
                platform.toString(),
                token
            ).collect { onLoginResult(it, User.LoginFrom.FACEBOOK) }
        }
    }

    fun loginEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            onLoginResult(MetaResult.Success(SignInResult(Profile(), "token")), User.LoginFrom.EMAIL)
//            passportRepository.loginWithEmail(
//                email,
//                password,
//                deviceInfo.deviceId,
//                deviceInfo.deviceName,
//                appVersion,
//                platform
//            ).collect { onLoginResult(it, User.LoginFrom.EMAIL) }
        }
    }

    private fun onLoginResult(result: MetaResult<SignInResult>, from: User.LoginFrom) {
        if (result is MetaResult.Success) {
            val data = result.data
            userManager.currentUser = User(
                data.profile,
                Credential(data.accessToken),
                from
            )
            _loginResult.postValue(MetaResult.Success(true))
        } else if (result is MetaResult.Error) {
            val cause = if (result.throwable is ApiException) {
                result.throwable.statusCode.toString()
            } else {
                result.throwable.toString()
            }
            _loginResult.postValue(MetaResult.Error(Throwable(cause)))
        }
    }
}