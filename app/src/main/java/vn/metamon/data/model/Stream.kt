package vn.metamon.data.model

data class Stream(
    val streamId: Int,
    val streamCover: String = "",
    val streamTitle: String = "",
    val streamLocation: String = "",
    val profile: Profile = Profile(),
    val point: Int = 0,
    val classifyIcon: String = ""
)
