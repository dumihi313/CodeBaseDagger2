package vn.metamon.data.service

import vn.metamon.data.model.Profile
import vn.metamon.data.model.Stream
import vn.metamon2.grpc.UserProfile

fun UserProfile.toProfile(): Profile =
    Profile(
        userId = uid,
        displayName = displayName,
        gender = gender,
        signature = signature,
        followerCount = nFollower,
        followCount = nFollowee,
        level = level,
        covers = emptyList(),
        topFans = topFanList.map { it.toProfile() },
    )

fun UserProfile.toStream(): Stream =
    Stream(
        streamId = uid,
        profile = this.toProfile()
    )