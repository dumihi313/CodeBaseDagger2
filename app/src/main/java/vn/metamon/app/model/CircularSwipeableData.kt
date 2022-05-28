package vn.metamon.app.model

import vn.metamon.app.utils.hasIndex
import vn.metamon.app.utils.hasMoreThan

class CircularSwipeableStreamData<T> : AbstractSwipeableData<T>() {
    override fun hasNext(): Boolean {
        return items.size > 1 && items.hasIndex(selectedIndex)
    }

    override fun hasPrevious(): Boolean {
        return items.hasMoreThan(1) && items.hasIndex(selectedIndex)
    }

    override fun nextInternal(): T? {
        return if (items.hasIndex(selectedIndex)) {
            selectedIndex = (selectedIndex + 1) % items.size
            items[selectedIndex]
        } else {
            null
        }
    }

    override fun previousInternal(): T? {
        return if (hasPrevious()) {
            val previous = if (selectedIndex > 0) (selectedIndex - 1) else (items.size - 1)
            val result = items[previous]
            selectedIndex = previous
            result
        } else {
            null
        }
    }

    override fun getNext(): T? {
        return if (items.hasIndex(selectedIndex)) {
            items[(selectedIndex + 1) % items.size]
        } else {
            null
        }
    }

    override fun getPrevious(): T? {
        return if (items.hasIndex(selectedIndex)) {
            val previous = if (selectedIndex > 0) (selectedIndex - 1) else (items.size - 1)
            items[previous]
        } else {
            null
        }
    }

    override fun getSelectedItem(): T? {
        return if (items.hasIndex(selectedIndex)) items[selectedIndex] else null
    }

    override fun removeSelectedItem(): T? {
        return if (items.hasIndex(selectedIndex)) {
            val removed = items.removeAt(selectedIndex)
            selectedIndex -= 1
            if (selectedIndex < 0) {
                selectedIndex = items.size - 1
            }
            removed
        } else {
            null
        }
    }
}