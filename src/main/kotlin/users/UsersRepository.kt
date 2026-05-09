package users

import kotlinx.serialization.json.Json
import java.io.File

class UsersRepository private constructor() {

    init {
        println("init")
    }

    private val file = File("users.json")

    val _users: MutableList<User> = loadAllUsers()

    val users
        get() = _users.toList()

    private fun loadAllUsers(): MutableList<User> = Json.decodeFromString(file.readText().trim())

    companion object {
        private var instance: UsersRepository? = null

        fun getInstance(password: String): UsersRepository {
            val correct = File("password_users.txt").readText().trim()
            if (correct != password) throw IllegalArgumentException("Wrong password")
            if (instance == null) {
                instance = UsersRepository()
            }
            return instance!!
        }
    }
}