package users

import kotlinx.serialization.json.Json
import java.io.File

class UsersRepository private constructor() {

    private val file = File("users.json")

    val _users: MutableList<User> = loadAllUsers()
    val users
        get() = _users.toList()

    private val observers = mutableListOf<Display>()

    private fun notifyObservers() {
        for (observer in observers) {
            observer.onChanged(users)
        }
    }

    fun registerObserver(observer: Display) {
        observers.add(observer)
        observer.onChanged(users)
    }

    private fun loadAllUsers(): MutableList<User> = Json.decodeFromString(file.readText().trim())

    fun addUser(firstName: String, lastName: String, age: Int) {
        val id = users.maxOf { it.id + 1 }
        val newUser = User(id, age, firstName, lastName)
        _users.add(newUser)
        notifyObservers()
    }

    fun deleteUser(id: Int) {
        _users.removeIf { it.id == id }
        notifyObservers()
    }

    fun saveChanges() {
        val json = Json.encodeToString(_users)
        file.writeText(json)
    }

    companion object {

        private val lock = Any()
        private var instance: UsersRepository? = null

        fun getInstance(password: String): UsersRepository {
            val correct = File("password_users.txt").readText().trim()
            if (correct != password) throw IllegalArgumentException("Wrong password")
            instance?.let { return it } // double check
            synchronized(lock) {
                instance?.let { return it }

                return UsersRepository().also {
                    instance = it
                }
            }
        }
    }
}