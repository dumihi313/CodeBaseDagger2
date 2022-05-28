package vn.metamon.utils.event

object Events {
    private const val BASE = 0
    const val USER_LOGGED_IN = BASE + 1
    const val USER_LOGGED_OUT = BASE + 2
    const val SWITCH_ENVIRONMENT = BASE + 3
    const val SWITCH_SOCKET = BASE + 4
    const val EVENT_BACK_FROM_EMPTY_FRAGMENT = BASE + 5
}