package vn.metamon.data.model

sealed class MetaResult<out R> {
    data class Success<out T>(val data: T) : MetaResult<T>()
    data class Error(val throwable: Throwable) : MetaResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[throwable=$throwable]"
        }
    }
}

val MetaResult<*>.succeeded
    get() = this is MetaResult.Success && data != null

val <T> MetaResult<T>.data: T?
    get() = (this as? MetaResult.Success)?.data

fun <T> MetaResult<T>.successOr(fallback: T): T {
    return (this as? MetaResult.Success<T>)?.data ?: fallback
}

fun <T, R> MetaResult<T>.map(transform: (T?) -> R): MetaResult<R> {
    return when (this) {
        is MetaResult.Success -> MetaResult.Success(transform(data))
        is MetaResult.Error -> MetaResult.Error(throwable)
    }
}