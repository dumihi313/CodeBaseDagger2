package vn.metamon.app.presentation.home.space

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vn.metamon.app.utils.MetaEvent
import vn.metamon.data.model.Metamon

class SpaceShareViewModel: ViewModel() {

    private val _spaceClick = MutableLiveData<MetaEvent<Metamon>>()
    val spaceClick: LiveData<MetaEvent<Metamon>> = _spaceClick

    fun onSpaceClick(space: Metamon) {
        _spaceClick.value = MetaEvent(space)
    }
}