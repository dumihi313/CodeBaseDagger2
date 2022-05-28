package vn.metamon.core

import vn.metamon.BuildConfig

data class Configs(
    val showStarTab: Boolean,
    val rankingUrl: String,
    val currentVersion: String,
    val lowestVersion: String,
    val agencyDashboardUrl: String? = null,
    val eventUrl: String? = null,
) {
    companion object {
        @JvmStatic
        val DEFAULT = Configs(
            false,
            BuildConfig.DEFAULT_URL,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_NAME,
            BuildConfig.DEFAULT_URL,
            BuildConfig.DEFAULT_URL,
        )
    }
}
