package vn.metamon.data.model

import android.graphics.drawable.Drawable

data class Profile(
    val userId: Int = DEFAULT_ID,
    var displayName: String = DEFAULT_DISPLAY_NAME,
    val level: Int = DEFAULT_LEVEL,
    val gender: Int = DEFAULT_GENDER,
    var signature: String = DEFAULT_SIGNATURE,
    val followerCount: Int = DEFAULT_FOLLOWER_COUNT,
    val followCount: Int = DEFAULT_FOLLOW_COUNT,
    val covers: List<Cover> = listOf(),
    var topFans: List<Profile> = listOf(),
    val vipId : Int = 0
) {
    companion object {
        const val DEFAULT_ID = -1
        const val DEFAULT_DISPLAY_NAME = "MetamonUser"
        const val DEFAULT_LEVEL = 0
        const val DEFAULT_GENDER = 0
        const val DEFAULT_SIGNATURE = ""
        const val DEFAULT_FOLLOWER_COUNT = 0
        const val DEFAULT_FOLLOW_COUNT = 0
    }

    var vipChatIconDrawable: Drawable? = null
        get() = field
        set(value) {
            field = value
        }

    val isVip: Boolean
        get() = vipId != 0
}

val Profile.displayId: String
    get() = userId.toString()

enum class CoverSize(val size: Int) {
    SMALL(100),
    MEDIUM(200),
    LARGE(512),
}
