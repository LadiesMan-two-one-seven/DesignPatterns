package users

import observer.Observer
import observer.Observable
import kotlinx.serialization.json.Json
import java.io.File

class UsersRepository private constructor(): Observable<List<User>> {

    private val file = File("users.json")

    private val _users: MutableList<User> = loadAllUsers()
    override val currentValue: List<User>
        get() = _users.toList()

    private val _observers = mutableListOf<Observer<List<User>>>()
    override val observers
        get() = _observers.toList()

    override fun registerObserver(observer: Observer<List<User>>) {
        _observers.add(observer)
        observer.onChanged(currentValue)
    }

    override fun unregisterObserver(observer: Observer<List<User>>) {
        _observers.remove(observer)
    }

    fun addOnUsersChangedListener(observer: Observer<List<User>>) {
        registerObserver(observer)
    }

    private fun loadAllUsers(): MutableList<User> = Json.decodeFromString(file.readText().trim())

    fun addUser(firstName: String, lastName: String, age: Int) {
        val id = currentValue.maxOf { it.id + 1 }
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