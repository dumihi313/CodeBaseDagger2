package vn.metamon.app.presentation.login

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginResultViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()

    val loginResult: LiveData<Boolean>
        get() = _loginResult

    @UiThread
    fun setLoginResult(success: Boolean) {
        _loginResult.value = success
    }
}