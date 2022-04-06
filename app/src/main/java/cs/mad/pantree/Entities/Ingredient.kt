package cs.mad.pantree.Entities

import androidx.room.*

data class IngredientContainer(
    val ingredients: List<Ingredient>
)

@Entity
data class Ingredient(
    var name: String?,
    var obtained: Boolean
        ) {
    @PrimaryKey (
        autoGenerate = true
            ) var id: Long = 0
}

@Dao
interface IngredientDao {
    @Query("select * from ingredient")
    suspend fun getAll(): List<Ingredient>

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
