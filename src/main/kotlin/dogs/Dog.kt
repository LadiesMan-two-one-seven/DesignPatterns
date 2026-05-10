package dogs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dog(
     @SerialName("id") val id: Int,
     @SerialName("weight") val weight: Double,
     @SerialName("breed_name") val breedName: String,
     @SerialName("dog_name") val dogName: String
) {
}