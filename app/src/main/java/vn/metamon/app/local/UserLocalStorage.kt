package vn.metamon.app.local

import android.content.SharedPreferences
import vn.metamon.data.di.UserPrefs
import vn.metamon.data.local.UserStorage
import vn.metamon.data.model.Credential
import vn.metamon.data.model.Profile
import vn.metamon.data.model.Profile.Companion.DEFAULT_DISPLAY_NAME
import vn.metamon.data.model.Profile.Companion.DEFAULT_FOLLOWER_COUNT
import vn.metamon.data.model.Profile.Companion.DEFAULT_FOLLOW_COUNT
import vn.metamon.data.model.Profile.Companion.DEFAULT_GENDER
import vn.metamon.data.model.Profile.Companion.DEFAULT_ID
import vn.metamon.data.model.Profile.Companion.DEFAULT_LEVEL
import vn.metamon.data.model.Profile.Companion.DEFAULT_SIGNATURE
import vn.metamon.data.model.User
import javax.inject.Inject

private const val KEY_USER_ID = "pref_user_id"
private const val KEY_USER_DISPLAY_NAME = "pref_user_display_name"
private const val KEY_USER_LEVEL = "pref_user_level"
private const val KEY_USER_GENDER = "pref_user_gender"
private const val KEY_USER_SIGNATURE = "pref_user_signature"
private const val KEY_FOLLOWER_COUNT = "pref_user_follower_count"
private const val KEY_FOLLOW_COUNT = "pref_user_follow_count"

private const val KEY_USER_ACCESS_TOKEN = "pref_access_token"

private const val KEY_HAS_AGREED_STREAMING_POLICY = "pref_has_agreed_streaming_policy"

class UserLocalStorage @Inject constructor(
    @UserPrefs private val pref: SharedPreferences
) : UserStorage {
    override fun load(): User {
        val profile = Profile(
            userId = pref.getInt(KEY_USER_ID, DEFAULT_ID),
            displayName = pref.getString(KEY_USER_DISPLAY_NAME, DEFAULT_DISPLAY_NAME) ?: DEFAULT_DISPLAY_NAME,
            level = pref.getInt(KEY_USER_LEVEL, DEFAULT_LEVEL),
            gender = pref.getInt(KEY_USER_GENDER, DEFAULT_GENDER),
            signature = pref.getString(KEY_USER_SIGNATURE, DEFAULT_SIGNATURE) ?: DEFAULT_SIGNATURE,
            followerCount = pref.getInt(KEY_FOLLOWER_COUNT, DEFAULT_FOLLOWER_COUNT),
            followCount = pref.getInt(KEY_FOLLOW_COUNT, DEFAULT_FOLLOW_COUNT)
        )
        val credential = Credential(
            pref.getString(KEY_USER_ACCESS_TOKEN, "") ?: ""
        )

        return User(profile, credential)
    }

    override fun save(user: User) {
        pref.edit().apply {
            putInt(KEY_USER_ID, user.profile.userId)
            putString(KEY_USER_DISPLAY_NAME, user.profile.displayName)
            putInt(KEY_USER_GENDER, user.profile.gender)
            putString(KEY_USER_SIGNATURE, user.profile.signature)
            putInt(KEY_FOLLOWER_COUNT, user.profile.followerCount)
            putInt(KEY_FOLLOW_COUNT, user.profile.followCount)
            putString(KEY_USER_ACCESS_TOKEN, user.credential.accessToken)
        }.apply()
    }

    override fun hasAgreedStreamingPolicy(): Boolean =
        pref.getBoolean(KEY_HAS_AGREED_STREAMING_POLICY, false)

    override fun markAgreedStreamingPolicy() {
        pref.edit().putBoolean(KEY_HAS_AGREED_STREAMING_POLICY, true).apply()
    }
}