package vn.metamon.app.presentation.stream

import javax.inject.Inject
import vn.metamon.app.di.AppScoped
import vn.metamon.app.model.CircularSwipeableStreamData
import vn.metamon.data.model.Stream

@AppScoped
class StreamPlaylistStore @Inject constructor() {
    val streamList = CircularSwipeableStreamData<Stream>()

    fun setStreamList(list: List<Stream>?) {
        streamList.setData(list)
    }

    fun addNextStreamToList(item: Stream) {
        streamList.addNextItem(item)
    }
}