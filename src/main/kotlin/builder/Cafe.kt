package builder

fun main() {
    val drink = Drink.Builder()
        .type("Tea")
        .diningOption("In cafe")
        .temperature("Cold")
        .build()
    val drink2 = Drink(type = "Coffee")
    println(drink)
    println(drink2)
}