package content

data class ApiKey(val id: String, val secretKey: String)

data class LoginData(val userName: String, val password: String) {
    override fun toString(): String {
        return "[$userName|$password]"
    }
}