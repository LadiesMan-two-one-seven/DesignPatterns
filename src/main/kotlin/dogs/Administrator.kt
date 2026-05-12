package dogs

class Administrator {

    val repository = DogsRepository.getInstance("qwerty")

    fun work() {
        while (true) {
            print("Enter an operation: ")
            val operations = Operation.entries
            for ((index, operation) in operations.withIndex()) {
                print("$index - ${operation.title}")
                if (index == operations.lastIndex) {
                    print(": ")
                } else {
                    print(", ")
                }
            }
            val operationIndex = readln().toInt()
            val operation = operations[operationIndex]
            when (operation) {
                Operation.EXIT -> {
                    repository.saveChanges()
                    break
                }
                Operation.ADD_DOG -> addDog()
                Operation.DELETE_DOG -> deleteDog()
            }
        }
    }

    private fun addDog() {
        print("Enter breed name: ")
        val breedName = readln()
        print("Enter dog name: ")
        val dogName = readln()
        print("Enter weight: ")
        val weight = readln().toDouble()
        DogsInvoker.addCommand {
            repository.addDog(breedName, dogName, weight)
        }
    }

    private fun deleteDog() {
        print("Enter id: ")
        val id = readln().toInt()
        DogsInvoker.addCommand {
            repository.deleteDog(id)
        }
    }
}