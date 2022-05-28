package vn.metamon.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vn.metamon.app.utils.MetaEvent

class EmptyFragmentSharedViewModel : ViewModel() {

    private val _event = MutableLiveData<MetaEvent<Unit>>()

    val event: LiveData<MetaEvent<Unit>>
        get() = _event

    fun triggerEvent() {
        _event.postValue(MetaEvent(Unit))
    }
}