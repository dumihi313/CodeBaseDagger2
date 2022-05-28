package vn.metamon.core

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import vn.metamon.utils.SingletonHolder

private const val PREF_APP_CONFIGS = "application_configs_key"

class AppConfigStorage private constructor(
    private val params: Pair<Context, Gson>
) {
    companion object : SingletonHolder<Pair<Context, Gson>, AppConfigStorage>(::AppConfigStorage)

    private val localStore: SharedPreferences =
        params.first.getSharedPreferences("app_configs", Context.MODE_PRIVATE)

    private var _currentConfig: Configs

    val currentConfig: Configs
        get() = _currentConfig

    init {
        val gson = params.second
        _currentConfig = try {
            gson.fromJson(localStore.getString(PREF_APP_CONFIGS, ""), Configs::class.java)
                ?: Configs.DEFAULT
        } catch (e: Exception) {
            e.printStackTrace()
            Configs.DEFAULT
        }
    }

    fun setConfig(config: Configs?) {
        if (config == null) {
            _currentConfig = Configs.DEFAULT
            return
        }

        _currentConfig = config

        localStore.edit().putString(PREF_APP_CONFIGS, params.second.toJson(_currentConfig)).apply()
    }
}