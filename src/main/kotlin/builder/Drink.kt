package builder

data class Drink(
    val type: String = "Coffee",
    val additives: List<String> = listOf(),
    val diningOption: String = "To go",
    val temperature: String = "Hot"
) {

    class Builder {
        private var type: String = "Coffee"
        private var additives: List<String> = listOf()
        private var diningOption: String = "To go"
        private var temperature: String = "Hot"

        fun type(type: String): Builder {
            this.type = type
            return this
        }

        fun additives(additives: List<String>): Builder {
            this.additives = additives
            return this
        }

        fun diningOption(diningOption: String): Builder {
            this.diningOption = diningOption
            return this
        }

        fun temperature(temperature: String): Builder {
            this.temperature = temperature
            return this
        }

        fun build(): Drink {
            return Drink(type, additives, diningOption, temperature)
        }
    }
}
