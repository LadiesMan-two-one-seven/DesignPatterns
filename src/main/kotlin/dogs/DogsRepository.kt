package dogs

import observer.Observer
import observer.Observable
import kotlinx.serialization.json.Json
import java.io.File

class DogsRepository private constructor(): Observable<List<Dog>> {

    private val file = File("dogs.json")

    val _dogs: MutableList<Dog> = loadAllDogs()
    override val currentValue: List<Dog>
        get() = _dogs.toList()

    private val _observers = mutableListOf<Observer<List<Dog>>>()
    override val observers: List<Observer<List<Dog>>>
        get() = _observers.toList()

    override fun registerObserver(observer: Observer<List<Dog>>) {
        _observers.add(observer)
        observer.onChanged(currentValue)
    }

    override fun unregisterObserver(observer: Observer<List<Dog>>) {
        _observers.remove(observer)
    }

    fun addOnDogsChangedListener(observer: Observer<List<Dog>>) {
        registerObserver(observer)
    }

    private fun loadAllDogs(): MutableList<Dog> = Json.decodeFromString(file.readText())

    fun addDog(breedName: String, dogName: String, weight: Double) {
        val id = _dogs.maxOf { it.id + 1 }
        val newDog = Dog(id, weight, breedName, dogName)
        _dogs.add(newDog)
        notifyObservers()
    }

    fun deleteDog(id: Int) {
        _dogs.removeIf { it.id == id }
        notifyObservers()
    }

    fun saveChanges() {
        val json = Json.encodeToString(_dogs)
        file.writeText(json)
    }

    companion object {

        private val lock = Any()
        private var instance: DogsRepository? = null

        fun getInstance(password: String): DogsRepository {
            val correct = File("password_dogs.txt").readText().trim()
            if (correct != password) throw IllegalArgumentException("Wrong password")
            instance?.let { return it } // double check
            synchronized(lock) {
                instance?.let { return it }
                
                return DogsRepository().also {
                    instance = it
                }
            }
        }
    }
}