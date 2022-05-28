package vn.metamon.data.model

data class User(
    var profile: Profile,
    val credential: Credential,
    val loginVia: LoginFrom = LoginFrom.UNKNOWN
) {
    companion object {
        val GUEST = User(Profile(), Credential(), LoginFrom.UNKNOWN)
    }

    enum class LoginFrom {
        FACEBOOK,
        GOOGLE,
        EMAIL,
        UNKNOWN
    }
}

