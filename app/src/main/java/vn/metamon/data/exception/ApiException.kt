package vn.metamon.data.exception

import vn.metamon2.grpc.Status

class ApiException @JvmOverloads constructor(
    val code: Int,
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {
    companion object {
        fun wrap(code: Int, message: String?): ApiException {
            return ApiException(code, message)
        }
        fun wrap(code: Int, message: String?, cause: Throwable?): ApiException {
            return ApiException(
                code,
                message = message,
                cause = cause
            )
        }
        fun wrap(status: Status): ApiException {
            return ApiException(status.code, status.message)
        }
    }
}