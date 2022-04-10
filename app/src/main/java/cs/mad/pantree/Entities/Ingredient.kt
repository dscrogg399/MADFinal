package cs.mad.pantree.Entities

import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class APIIngredientContainer (
    val apiIngredients: List<APIIngredient>
        )

@Serializable
data class APIIngredient(
    var id: Int,
    var amount: Double,
    var unit: String,
    var name: String
)

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

fun APIIngredient.toIngredient(rcpId: Long): Ingredient {
    return Ingredient(null, id, amount, unit, name, rcpId)
}

@Dao
interface IngredientDao {
    @Query("select * from ingredient")
    suspend fun getAll(): List<Ingredient>

    @Query("select * from ingredient where rcpId = :rcpId")
    suspend fun get(rcpId: Long): List<Ingredient>

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


