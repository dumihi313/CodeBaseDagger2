package vn.metamon.app.presentation.home.space

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import vn.metamon.app.presentation.utils.StatefulViewModel
import vn.metamon.app.utils.MetaEvent
import vn.metamon.app.utils.flowOnIO
import vn.metamon.app.utils.launch
import vn.metamon.data.model.MetaResult
import vn.metamon.data.model.Space
import vn.metamon.data.model.SpaceUniverse
import vn.metamon.data.repository.SpaceRepository
import javax.inject.Inject

class SpaceViewModel @Inject constructor(
    private val spaceRepository: SpaceRepository
) : StatefulViewModel() {

    private val _uniSpaceList = MutableLiveData<MetaEvent<List<SpaceUniverse>>>()
    val uniSpaceList: LiveData<MetaEvent<List<SpaceUniverse>>> = _uniSpaceList

    private val _spaceList = MutableLiveData<MetaEvent<List<Space>>>()
    val spaceList: LiveData<MetaEvent<List<Space>>> = _spaceList

    fun getUniSpace() {
        launch {
            spaceRepository.getUniSpaces()
                .flowOnIO()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    setError(it)
                }.onCompletion { setLoading(false) }
                .collect {
                    when(it) {
                        is MetaResult.Success -> _uniSpaceList.value = MetaEvent(it.data)
                        is MetaResult.Error -> setError(it.throwable)
                    }
                }
        }
    }

    fun getSpace(uniSpaceId: Int) {
        launch {
            spaceRepository.getSpaces(uniSpaceId)
                .flowOnIO()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    setError(it)
                }.onCompletion { setLoading(false) }
                .collect {
                    when(it) {
                        is MetaResult.Success -> _spaceList.value = MetaEvent(it.data)
                        is MetaResult.Error -> setError(it.throwable)
                    }
                }
        }
    }
}