package vn.metamon.app.utils

fun <T> List<T>?.rangeCheck(index: Int): Boolean {
    return if (this == null) false else (0 <= index && index < this.size)
}

fun Collection<*>?.hasMoreThan(count: Int) = this != null && size > count

fun Collection<*>?.hasAtLeast(count: Int) = this != null && size >= count

fun Collection<*>?.hasItems() = this != null && size > 0

fun List<*>?.hasIndex(index: Int) = this != null && index isIn indices

