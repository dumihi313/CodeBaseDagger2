package vn.metamon.utils.event

fun interface EventListener {
    fun onEvent(id: Int, vararg args: Any?)
}