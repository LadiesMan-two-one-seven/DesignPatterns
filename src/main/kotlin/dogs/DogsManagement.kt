package dogs

import users.UsersRepository

fun main() {
    DogsRepository.getInstance("qwerty").dogs.forEach(::println)
}