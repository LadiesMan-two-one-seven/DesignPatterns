package dogs

import kotlinx.serialization.json.Json
import observer.MutableObservable
import observer.Observable
import java.io.File

class DogsRepository private constructor() {

    init {
        println("Creating repository...")
    }

    private val file = File("dogs.json")

    private val dogsList: MutableList<Dog> = loadAllDogs()

    private val _dogs = MutableObservable(dogsList.toList())
    val dogs: Observable<List<Dog>>
        get() = _dogs

    private fun loadAllDogs(): MutableList<Dog> = Json.decodeFromString(file.readText())

    fun addDog(breedName: String, dogName: String, weight: Double) {
        Thread.sleep(10_000)
        val id = dogsList.maxOf { it.id + 1 }
        val newDog = Dog(id, weight, breedName, dogName)
        dogsList.add(newDog)
        _dogs.currentValue = dogsList.toList()
    }

    fun deleteDog(id: Int) {
        Thread.sleep(10_000)
        dogsList.removeIf { it.id == id }
        _dogs.currentValue = dogsList.toList()
    }

    fun saveChanges() {
        val json = Json.encodeToString(dogsList)
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