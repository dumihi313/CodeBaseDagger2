package vn.metamon.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Space(
    val id: Int = 0,
    val uniSpaceId: Int = 0,
    val name: String = "",
    val cover: String = "",
    val rarity: String = "",
    val totalViewer: Int = 0,
    val totalShare: Int = 0,
    val totalHeart: Int = 0,
    val totalDonate: Int = 0,
    val totalSubscriber: Int = 0
): Parcelable