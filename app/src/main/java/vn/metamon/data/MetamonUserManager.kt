package vn.metamon.data

import vn.metamon.data.local.UserStorage
import vn.metamon.data.model.User
import vn.metamon.data.model.User.Companion.GUEST
import vn.metamon.utils.SingletonHolder

class MetamonUserManager private constructor(
    private val storage: UserStorage
) {
    companion object : SingletonHolder<UserStorage, MetamonUserManager>(::MetamonUserManager)

    private var _currentUser: User = storage.load()

    var currentUser: User
        get() = _currentUser
        set(value) {
            _currentUser = value
            storage.save(_currentUser)
        }

    fun isUserLoggedIn(): Boolean = currentUser.credential.accessToken.isNotEmpty()

    fun clear() {
        currentUser = GUEST
    }
}