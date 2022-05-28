package vn.metamon.data.service

import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import vn.metamon.app.ErrorCodes
import vn.metamon.data.exception.ApiException
import vn.metamon.data.model.MetaResult
import vn.metamon.utils.MetaFlow

abstract class BaseGRpcService {
    fun <T> execute(block: () -> MetaResult<T>): MetaFlow<T> {
        return flow {
            try {
                emit(block.invoke())
            } catch (e: StatusRuntimeException) {
                if (e.status.code.value() == Status.UNAVAILABLE.code.value()) {
                    emit(MetaResult.Error(ApiException.wrap(ErrorCodes.NETWORK_ERROR, e.message, e)))
                } else {
                    emit(MetaResult.Error(ApiException.wrap(e.status.code.value(), e.message, e)))
                }
            }
        }
    }

    fun <T1, T2> executeZip(block1: () -> MetaResult<T1>, block2: () -> MetaResult<T2>): MetaFlow<Pair<T1?, T2?>> {
        return flow {
            try{
                var data1: T1? = null
                var data2: T2? = null
                val flow1 = flowOf(block1.invoke())
                val flow2 = flowOf(block2.invoke())
                flow1.zip(flow2) { result1, result2 ->
                    return@zip Pair(result1, result2)
                }.collect {  data ->
                    if (data.first is MetaResult.Success) {
                        data1 = (data.first as MetaResult.Success<T1>).data
                    } else {
                        emit(data.first as MetaResult.Error)
                    }
                    if (data.second is MetaResult.Success) {
                        data2 = (data.second as MetaResult.Success<T2>).data
                    } else {
                        emit(data.second as MetaResult.Error)
                    }
                }
                emit(MetaResult.Success(Pair(data1, data2)))
            } catch (e: StatusRuntimeException) {
                if (e.status.code.value() == Status.UNAVAILABLE.code.value()) {
                    emit(MetaResult.Error(ApiException.wrap(ErrorCodes.NETWORK_ERROR, e.message, e)))
                } else {
                    emit(MetaResult.Error(ApiException.wrap(e.status.code.value(), e.message, e)))
                }
            }
        }
    }
}
