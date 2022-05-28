package vn.metamon.data.remote

import vn.metamon2.grpc.Status

fun Status.isSuccess() = code == 0