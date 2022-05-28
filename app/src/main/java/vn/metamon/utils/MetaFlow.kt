package vn.metamon.utils

import kotlinx.coroutines.flow.Flow
import vn.metamon.data.model.MetaResult

typealias MetaFlow<T> = Flow<MetaResult<T>>