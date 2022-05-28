package vn.metamon.data.model

data class Cover(
    val index: Int,
    val type: Int,
    val version: Int
)

val Cover.ext : String
    get() = when(type) {
        1 -> "jpg"
        2 -> "mp4"
        else -> "Unidentified"
    }