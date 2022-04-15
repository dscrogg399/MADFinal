package cs.mad.pantree.Entities

import androidx.room.*
import kotlinx.serialization.Serializable

//container for API call
@Serializable
data class APIIngredientContainer (
    val apiIngredients: List<APIIngredient>
        )

//API Ingredient, store its id, amount, unit, and name
@Serializable
data class APIIngredient(
    var id: Int,
    var amount: Double,
    var unit: String,
    var name: String
)

//database Ingredient, generate a unique id, store its outsideId(not necessary), amount, unit, name, the id of the recipe it belongs to,
//and whether or not it is already obtained(via user input, default is false)
@Entity
data class Ingredient (
    @PrimaryKey (
        autoGenerate = true
    ) var id: Long? = null,
    var outsideId: Int,
    var amount: Double,
    var unit: String,
    var name: String,
    var rcpId: Long,
    var missed: Boolean = false
        )

//convertor, turns an API ingredient into a database ingredient, takes in the recipe ID
fun APIIngredient.toIngredient(rcpId: Long): Ingredient {
    return Ingredient(null, id, amount, unit, name, rcpId)
}

//dao, standard except we use a getIngredients function that takes in a recipe ID and returns all the ingredients used in that recipe
@Dao
interface IngredientDao {
    @Query("select * from ingredient")
    suspend fun getAll(): List<Ingredient>

    @Query("select * from ingredient where rcpId = :rcpId")
    suspend fun getIngredients(rcpId: Long): List<Ingredient>

    @Insert
    suspend fun insert(ig: Ingredient) : Long //returns type

    @Insert
    suspend fun insertAll(igs: List<Ingredient>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(ig: Ingredient)

    @Delete
    suspend fun delete(ig: Ingredient)

    @Query("delete from ingredient")
    suspend fun deleteAll()
}


