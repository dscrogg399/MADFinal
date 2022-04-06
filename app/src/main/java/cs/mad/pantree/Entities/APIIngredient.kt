package cs.mad.pantree.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class APIIngredientContainer (
    val apiIngredients: List<APIIngredient>
        )

@Entity @Serializable
data class APIIngredient(
    @PrimaryKey var id: Long,
    var amount: Long,
    var unit: String,
    var name: String
)