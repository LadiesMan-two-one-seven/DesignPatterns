package dogs

import command.Command

sealed interface AdministratorCommand : Command {

    data class AddDog(
        val repository: DogsRepository,
        val breedName: String,
        val dogName: String,
        val weight: Double
    ): AdministratorCommand {

        override fun execute() {
            repository.addDog(breedName, dogName, weight)
        }
    }

    data class DeleteDog(
        val repository: DogsRepository,
        val id: Int
    ): AdministratorCommand {

        override fun execute() {
            repository.deleteDog(id)
        }
    }

    data class SaveChanges(
        val repository: DogsRepository
    ): AdministratorCommand {

        override fun execute() {
            repository.saveChanges()
        }
    }
}