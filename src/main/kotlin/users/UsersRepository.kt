package users

import kotlinx.serialization.json.Json
import observer.MutableObservable
import observer.Observable
import java.io.File

class UsersRepository private constructor() {

    private val file = File("users.json")

    private val usersList: MutableList<User> = loadAllUsers()

    private val _users= MutableObservable(usersList.toList())
    val users: Observable<List<User>>
        get() = _users

    private val _oldestUser= MutableObservable(usersList.maxBy { it.age })
    val oldestUser: Observable<User>
        get() = _oldestUser

    private fun loadAllUsers(): MutableList<User> = Json.decodeFromString(file.readText().trim())

    fun addUser(firstName: String, lastName: String, age: Int) {
        val id = usersList.maxOf { it.id + 1 }
        val user = User(id, age, firstName, lastName)
        usersList.add(user)
        _users.currentValue = usersList.toList()
        if (age > _oldestUser.currentValue.age) {
            _oldestUser.currentValue = user
        }
    }

    fun deleteUser(id: Int) {
        usersList.removeIf { it.id == id }
        _users.currentValue = usersList.toList()
        val newOldest = usersList.maxBy { it.age }
        if (newOldest != _oldestUser.currentValue) {
            _oldestUser.currentValue = newOldest
        }
    }

    fun saveChanges() {
        val json = Json.encodeToString(usersList)
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