package vn.metamon.core

import vn.metamon.BuildConfig

data class Configs(
    val streamServer: StreamServer,
    val showStarTab: Boolean,
    val rankingUrl: String,
    val userDiamondDetailUrl: String,
    val classifyMissionUrl: String,
    val currentVersion: String,
    val lowestVersion: String,
    val dailyMissionUrl: String? = null,
    val idolOnboardingUrl: String? = null,
    val agencyDashboardUrl: String? = null,
    val csUrl: String? = null,
    val levelUrl: String? = null,
    val attractivePointUrl: String? = null,
    val starDetailUrl: String? = null,
    val userBagUrl: String? = null,
    val eventUrl: String? = null,
    val eventIconUrl: String? = null,
    val userMissionUrl: String? = null,
    val userMissionIconUrl: String? = null,
) {
    companion object {
        @JvmStatic
        val DEFAULT = Configs(
            StreamServer(
                BuildConfig.DEFAULT_STREAM_HOST,
                BuildConfig.DEFAULT_STREAM_PORT
            ),
            false,
            BuildConfig.DEFAULT_URL,
            BuildConfig.DEFAULT_URL,
            BuildConfig.DEFAULT_URL,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_NAME
        )
    }

    data class StreamServer(
        val host: String,
        val port: Int
    )
}
