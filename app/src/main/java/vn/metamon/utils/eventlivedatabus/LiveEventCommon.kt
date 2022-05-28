package vn.metamon.utils.eventlivedatabus

import androidx.lifecycle.BusLiveData

interface LiveEventCommon {
    fun backFromEmptyFragmentListener(): BusLiveData<Unit>
}