package multithreading

import kotlin.concurrent.thread
import kotlin.random.Random

fun main() {
    print("Enter number from 0 to 1_000_000_000: ")
    val number = readln().toInt()
    var win = false
    thread {
        var seconds = 1
        while (!win) {
            println(seconds++)
            Thread.sleep(1000)
        }
    }
    thread {
        while (true) {
            val option = Random.nextInt(1, 1_000_000_001)
            if (option == number) {
                println("I win. Your number is $number")
                win = true
                break
            }
        }
    }
}