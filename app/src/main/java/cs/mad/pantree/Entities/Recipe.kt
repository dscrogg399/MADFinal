package cs.mad.pantree.Entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class APIRecipeContainer(
    val recipes: List<APIRecipe>
)

@Serializable
data class APIRecipe (
    val id: Int,
    val title: String,
    val image: String,
    val missedIngredients: List<APIIngredient>,
    val usedIngredients: List<APIIngredient>
)

@Entity
data class Recipe (
    @PrimaryKey val id: Long? = null,
    val outsideId: Int,
    val title: String,
    val image: String,
    val missedIngredients: List<Long>,
    val usedIngredients: List<Long>,
    val srcUrl: String? = null
        )

fun APIRecipe.toRecipe(): Recipe {
    return Recipe(null, id, title, image, missedIngredients.map {it.id}, usedIngredients.map{it.id})
}
@Dao
interface RecipeDao {

}

/*

 */