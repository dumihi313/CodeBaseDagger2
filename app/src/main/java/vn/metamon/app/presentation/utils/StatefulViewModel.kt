package vn.metamon.app.presentation.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vn.metamon.app.utils.MetaEvent

abstract class StatefulViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<MetaEvent<Boolean>>()

    val isLoading: LiveData<MetaEvent<Boolean>>
        get() = _isLoading

    private val _error = MutableLiveData<MetaEvent<Throwable>>()

    val error: LiveData<MetaEvent<Throwable>>
        get() = _error

    @MainThread
    protected fun setLoading(loading: Boolean) {
        _isLoading.postValue(MetaEvent(loading))
    }

    @MainThread
    protected fun setError(error: Throwable) {
        _error.postValue(MetaEvent(error))
    }
}