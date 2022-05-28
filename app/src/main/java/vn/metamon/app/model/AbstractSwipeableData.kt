package vn.metamon.app.model

import vn.metamon.app.utils.hasIndex
import java.util.*

abstract class AbstractSwipeableData<T> : SwipeableData<T> {
    override var selectedIndex = -1
        set(value) = if (items.hasIndex(value)) {
            field = value
        } else {
            field = -1
        }

    protected val items = mutableListOf<T>()

    protected var dirtyIndex = -1

    override fun setData(items: List<T>?) {
        reset()
        if (items?.isNotEmpty() == true) {
            this.items.addAll(items)
        }
    }

    override fun addNextItem(item: T) {
        this.items.add(selectedIndex + 1, item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun reset() {
        selectedIndex = -1
        items.clear()
    }

    override fun getData(): List<T>? {
        return ArrayList(items)
    }

    final override fun next(): T? {
        removeDirtyAndFixCurrentIndexIfPossible()
        return nextInternal()
    }

    protected abstract fun nextInternal(): T?

    final override fun previous(): T? {
        removeDirtyAndFixCurrentIndexIfPossible()
        return previousInternal()
    }

    protected abstract fun previousInternal(): T?

    private fun removeDirtyAndFixCurrentIndexIfPossible() {
        if (dirtyIndex != -1) {
            items.removeAt(dirtyIndex)
            if (dirtyIndex >= selectedIndex) {
                selectedIndex--
            }
            dirtyIndex = -1
        }
    }

    override fun markCurrentDirty() {
        dirtyIndex = selectedIndex
    }
}