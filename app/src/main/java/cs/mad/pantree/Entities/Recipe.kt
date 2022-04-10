package cs.mad.pantree.Entities

import android.util.Log
import androidx.room.*
import cs.mad.pantree.WebServices.RecipeWebservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//@Serializable
//data class APIRecipeContainer(
//    val apiRecipes: List<APIRecipe>
//)

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
    @PrimaryKey (
        autoGenerate = true
    ) var id: Long? = null,
    val outsideId: Int,
    val title: String,
    val image: String,
    val sourceUrl: String
        )

fun APIRecipe.storeInDatabase(rcpDao: RecipeDao, ingDao: IngredientDao) {
    Log.d("id", this.id.toString())
    RecipeWebservice().recipeUrlService.pullRecipe(this.id, "5fb674b8b4e3412993d19c551979f844").enqueue(object:
        Callback<SourceUrl> {
        override fun onResponse(
            call: Call<SourceUrl>,
            response: Response<SourceUrl>
        ) {
            if (response.isSuccessful) {
                Log.d("onResponse url", "download success!")
                val url = response.body()
                val rcp = url?.let { Recipe(null, id, title, image, it.sourceUrl) }
                runOnIO {
                    if (rcp != null) {
                        rcp.id = rcpDao.insert(rcp)
                    }
                }

                for (missedIngredient in missedIngredients) {
                    if (rcp != null) {
                        rcp.id?.let {
                            var ing = missedIngredient.toIngredient(it)
                            ing.missed = true
                            runOnIO {
                                ingDao.insert(ing)
                            }
                        }
                    }

                }

                for (usedIngredient in usedIngredients) {
                    if (rcp != null) {
                        rcp.id?.let {
                            var ing = usedIngredient.toIngredient(it)
                            runOnIO {
                                ingDao.insert(ing)
                            }
                        }
                    }
                }

            }

        }

        override fun onFailure(call: Call<SourceUrl>, t: Throwable) {
            Log.d("onFailure url", "failed")
        }
    })






}
@Dao
interface RecipeDao {
    @Query("select * from recipe")
    suspend fun getAll(): List<Recipe>

    @Query("select * from recipe where outsideId = outsideId")
    suspend fun getRecipe(): Recipe

    @Insert
    suspend fun insert(rcp: Recipe) : Long //returns type

    @Insert
    suspend fun insertAll(rcps: List<Recipe>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(rcp: Recipe)

    @Delete
    suspend fun delete(rcp: Recipe)

    @Query("delete from recipe")
    suspend fun deleteAll()

}

fun runOnIO(lambda: suspend () -> Unit) {
    runBlocking {
        launch(Dispatchers.IO) { lambda() }
    }
}

/*

 */