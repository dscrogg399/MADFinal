package cs.mad.pantree.WebServices

import cs.mad.pantree.Entities.RecipeContainer
import cs.mad.pantree.Entities.SourceUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class RecipeWebservice {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val recipeService = retrofit.create(RecipeSearchService::class.java)



    interface RecipeSearchService {

        @GET("/recipes/findByIngredients")
        fun searchRecipes(
            @Query("apiKey") apiKey: String,
            @Query("ingredients") ingredients: String,
            @Query("number") maxResults: Int,
            @Query("ranking") maxUsed: Int

        ): Call<RecipeContainer>

    }

    interface RecipeInfoService {
        @GET("/{id}/information")
        fun pullRecipe(
            @Path("id") id: Int,
            @Query("apiKey") apiKey: String
        ): Call<SourceUrl>
    }
}