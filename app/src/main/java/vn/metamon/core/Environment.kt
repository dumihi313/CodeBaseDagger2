package vn.metamon.core

import android.content.SharedPreferences
import vn.metamon.BuildConfig
import vn.metamon.utils.SingletonHolder

private const val PREF_CURRENT_FLAVOR = "pref_key_current_flavor"

private const val PREF_CURRENT_SOCKET = "pref_key_current_socket"

class Environment private constructor(private val storage: SharedPreferences) {
    companion object : SingletonHolder<SharedPreferences, Environment>(::Environment)

    private val flavors = listOf(Flavor.PROD, Flavor.DEV)

    private var currentIndex = 0

    init {
        currentIndex = storage.getInt(PREF_CURRENT_FLAVOR, 0)
    }

    fun rotate() {
        currentIndex = (currentIndex + 1) % flavors.size
        storage.edit().putInt(PREF_CURRENT_FLAVOR, currentIndex).apply()
    }

    fun current(): Flavor {
        return flavors[currentIndex]
    }

    enum class Flavor(
        val apiHost: String,
        val apiPort: Int
    ) {
        DEV(BuildConfig.DEV_API_HOST, BuildConfig.DEV_API_PORT),
        PROD(BuildConfig.PROD_API_HOST, BuildConfig.PROD_API_PORT);
    }
}