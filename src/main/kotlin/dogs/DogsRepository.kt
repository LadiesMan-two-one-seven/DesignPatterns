package dogs

import kotlinx.serialization.json.Json
import java.io.File

class DogsRepository private constructor() {

    init {
        println("init")
    }

    val file = File("dogs.json")

    private val _dogs = loadAllDogs()

    val dogs
        get() = _dogs.toList()

    private fun loadAllDogs(): MutableList<Dog> = Json.decodeFromString(file.readText())

    companion object {

        private val lock = Any()
        private var instance: DogsRepository? = null

        fun getInstance(password: String): DogsRepository {
            val correct = File("password_dogs.txt").readText().trim()
            if (correct != password) throw IllegalArgumentException("Wrong password")
            instance?.let { return it } // double check
            synchronized(lock)  {
                instance?.let { return it }
                
                return DogsRepository().also {
                    instance = it
                }
            }
        }
    }
}