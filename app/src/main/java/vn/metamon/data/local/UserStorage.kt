package vn.metamon.data.local

import vn.metamon.data.model.User

interface UserStorage {
    fun load(): User

    fun save(user: User)

    fun hasAgreedStreamingPolicy(): Boolean

    fun markAgreedStreamingPolicy()
}