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
import vn.metamon.data.model.*
import vn.metamon.data.repository.MetamonRepository
import javax.inject.Inject

class MetamonViewModel @Inject constructor(
    private val metamonRepository: MetamonRepository
) : StatefulViewModel() {

    private val _kingdomList = MutableLiveData<MetaEvent<List<KingDom>>>()
    val kingdomList: LiveData<MetaEvent<List<KingDom>>> = _kingdomList

    private val _metamonList = MutableLiveData<MetaEvent<List<Metamon>>>()
    val metamonList: LiveData<MetaEvent<List<Metamon>>> = _metamonList

    fun getKingdom() {
        launch {
            metamonRepository.getKingDom()
                .flowOnIO()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    setError(it)
                }.onCompletion { setLoading(false) }
                .collect {
                    when(it) {
                        is MetaResult.Success -> _kingdomList.value = MetaEvent(it.data)
                        is MetaResult.Error -> setError(it.throwable)
                    }
                }
        }
    }

    fun getMetamons(kingdomeId: Int) {
        launch {
            metamonRepository.getMetamons(kingdomeId)
                .flowOnIO()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    setError(it)
                }.onCompletion { setLoading(false) }
                .collect {
                    when(it) {
                        is MetaResult.Success -> _metamonList.value = MetaEvent(it.data)
                        is MetaResult.Error -> setError(it.throwable)
                    }
                }
        }
    }
}