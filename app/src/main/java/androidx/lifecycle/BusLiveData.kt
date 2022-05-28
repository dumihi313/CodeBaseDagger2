package androidx.lifecycle


import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import vn.metamon.utils.eventlivedatabus.LiveDataBusLogger
import vn.metamon.utils.eventlivedatabus.core.BaseBusObserverWrapper
import vn.metamon.utils.eventlivedatabus.core.BusAlwaysActiveObserver
import vn.metamon.utils.eventlivedatabus.core.BusLifecycleObserver
import vn.metamon.utils.eventlivedatabus.core.LiveDataBusCore


class BusLiveData<T>(private val mKey:String) : MutableLiveData<T>() {

    private val TAG = "BusLiveData"

    private val mObserverMap: MutableMap<Observer<in T>, BaseBusObserverWrapper<T>> = mutableMapOf()

    private val mMainHandler = Handler(Looper.getMainLooper())

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val exist = mObserverMap.getOrPut(observer) {
            BusLifecycleObserver(observer, owner, this).apply {
                mObserverMap[observer] = this
                owner.lifecycle.addObserver(this)
            }
        }
        super.observe(owner, exist)
        LiveDataBusLogger.d(TAG, "observe() called with: owner = [$owner], observer = [$observer]")
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(observer)
        val exist = mObserverMap.getOrPut(observer) {
            BusAlwaysActiveObserver(observer, this).apply {
                mObserverMap[observer] = this
            }
        }
        super.observeForever(exist)
        LiveDataBusLogger.d(TAG, "observeForever() called with: observer = [$observer]")
    }

    @MainThread
    fun observeSticky(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, observer)
        LiveDataBusLogger.d(
            TAG,
            "observeSticky() called with: owner = [$owner], observer = [$observer]"
        )
    }

    @MainThread
    fun observeStickyForever(observer: Observer<T>){
        super.observeForever(observer)
        LiveDataBusLogger.d(TAG, "observeStickyForever() called with: observer = [$observer]")
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        val exist = mObserverMap.remove(observer) as? Observer<in T>
        if (exist != null) {
            super.removeObserver(exist)
            LiveDataBusLogger.d(TAG, "removeObserver() called with: observer = [$observer]")
        } else {
            LiveDataBusLogger.d(TAG, "removeObserver() called with: observer = [null]")
        }
    }

    @MainThread
    override fun removeObservers(owner: LifecycleOwner) {
        mObserverMap.iterator().forEach {
            if (it.value.isAttachedTo(owner)) {
                mObserverMap.remove(it.key)
            }
        }
        super.removeObservers(owner)
        LiveDataBusLogger.d(TAG, "removeObservers() called with: owner = [$owner]")
    }

    override fun postValue(value: T) {
        mMainHandler.post {
            setValue(value)
        }
    }

    @MainThread
    override fun onInactive() {
        super.onInactive()
        if (!hasObservers()) {
            // 当 LiveData 没有活跃的观察者时，可以移除相关的实例
            LiveDataBusCore.getInstance().mBusMap.remove(mKey)
        }
        LiveDataBusLogger.d(TAG, "onInactive() called")
    }

    @MainThread
    public override fun getVersion(): Int {
        return super.getVersion()
    }
}