package dogs

import kotlinx.serialization.json.Json
import observer.MutableObservable
import java.io.File

class DogsRepository private constructor() {

    private val file = File("dogs.json")

    private val _dogs: MutableList<Dog> = loadAllDogs()
    val dogs = MutableObservable(_dogs.toList())

    private fun loadAllDogs(): MutableList<Dog> = Json.decodeFromString(file.readText())

    fun addDog(breedName: String, dogName: String, weight: Double) {
        val id = _dogs.maxOf { it.id + 1 }
        val newDog = Dog(id, weight, breedName, dogName)
        _dogs.add(newDog)
        dogs.currentValue = _dogs.toList()
    }

    fun deleteDog(id: Int) {
        _dogs.removeIf { it.id == id }
        dogs.currentValue = _dogs.toList()
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