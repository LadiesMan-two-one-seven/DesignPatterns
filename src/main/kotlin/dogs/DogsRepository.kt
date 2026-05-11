package dogs

import observer.Observer
import kotlinx.serialization.json.Json
import java.io.File

class DogsRepository private constructor() {

    private val file = File("dogs.json")

    val _dogs: MutableList<Dog> = loadAllDogs()
    val dogs
        get() = _dogs.toList()

    private val observers = mutableListOf<Observer<List<Dog>>>()

    private fun notifyObservers() {
        for (observer in observers) {
            observer.onChanged(dogs)
        }
    }

    fun registerObserver(observer: Observer<List<Dog>>) {
        observers.add(observer)
        observer.onChanged(dogs)
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