package vn.metamon.utils

open class SingletonHolder<in P, out R: Any>(creator: (P) -> R) {
    private var creator: ((P) -> R)? = creator
    @Volatile private var instance: R? = null

    fun getInstance(arg: P): R {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}