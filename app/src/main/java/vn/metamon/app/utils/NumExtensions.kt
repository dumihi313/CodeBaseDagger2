package vn.metamon.app.utils


infix fun Int?.isIn(range: IntRange): Boolean {
    return this != null && range.first <= this && this <= range.last
}

infix fun Int?.notIn(range: IntRange): Boolean = !this.isIn(range)
