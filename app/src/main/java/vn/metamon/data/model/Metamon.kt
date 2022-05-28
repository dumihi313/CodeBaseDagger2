package vn.metamon.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Metamon(
    val id: Int = 0,
    val kingdomId: Int = 0,
    val rarity: String = "",
    val status: Boolean = false,
    val name: String = "",
    val cover: String = "",
): Parcelable