package vn.metamon.app.model

interface SwipeableData<T> {
    var selectedIndex: Int

    fun getItemCount(): Int

    fun getData(): List<T>?

    operator fun hasNext(): Boolean

    fun hasPrevious(): Boolean

    operator fun next(): T?

    fun previous(): T?

    fun getNext(): T?

    fun getPrevious(): T?

    fun getSelectedItem(): T?

    @Deprecated(
        message = "This method cause bug when remove on list has 2 items",
        replaceWith = ReplaceWith("markCurrentDirty()"),
        level = DeprecationLevel.WARNING
    )
    fun removeSelectedItem(): T?

    fun reset()

    fun setData(items: List<T>?)

    fun addNextItem(item: T)

    fun markCurrentDirty()
}