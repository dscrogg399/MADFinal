package cs.mad.pantree.Activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import cs.mad.pantree.Adapters.IngredientInputAdapter
import cs.mad.pantree.R
import cs.mad.pantree.Databases.Database
import cs.mad.pantree.Entities.*
import cs.mad.pantree.WebServices.RecipeWebservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


import cs.mad.pantree.databinding.ActivityIngredientInputBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientInputActivity : AppCompatActivity(), Callback<List<APIRecipe>> {
    //binding var
    private lateinit var binding: ActivityIngredientInputBinding
    private lateinit var inpDao: InputDao
    private lateinit var rcpDao: RecipeDao
    private lateinit var ingDao: IngredientDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient_input)
        binding = ActivityIngredientInputBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //building database
        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, Database.dataBaseName
        ).build()
        inpDao = db.inpDao()
        //need these two for API call
        rcpDao = db.rcpDao()
        ingDao = db.ingDao()

        //setting adapter
        binding.ingredientRecyclerView.adapter = IngredientInputAdapter(inpDao)

        //add ingredient button, adds another text field to recycler using addItem()
        binding.addIngredientButton.setOnClickListener {
            (binding.ingredientRecyclerView.adapter as IngredientInputAdapter).addItem()
                binding.ingredientRecyclerView.smoothScrollToPosition((binding.ingredientRecyclerView.adapter as IngredientInputAdapter).itemCount - 1)
        }

        //search button, calls the api
        binding.searchButton.setOnClickListener {

            //pulling the text fields into one string for API search
            var ings = (binding.ingredientRecyclerView.adapter as IngredientInputAdapter).getSearchString()
            //makes the API call
            apiCall(ings)

        }

        //call load data
        loadData()



    }

    //calls the API webservice for searching by ingredient
    private fun apiCall(ings: String) {
        runOnIO {
            rcpDao.deleteAll()
        }
        RecipeWebservice().recipeSearchService.searchRecipes("f645e9e38de842cfa8a46a4346419b2f", ings, 5, 1).enqueue(this)
    }

    //onResponse
    override fun onResponse(
        call: Call<List<APIRecipe>>,
        response: Response<List<APIRecipe>>
    ) {
        //if successful,
        if (response.isSuccessful) {
            Log.d("onResponse", "download success!")
            //grabs response body
            val arcps = response.body()
            //sends it to the database input function in the Recipe entity
            var count = arcps?.size?: 0
            arcps?.forEachIndexed {index, recipe -> recipe.storeInDatabase(rcpDao, ingDao, this) {
                count--
                if (count == 0) {
                    //launches the RecipeList activity
                    val i = Intent(this, RecipeListActivity::class.java)
                    startActivity(i)
                }
            }
            }
        }
    }

    //on failure
    override fun onFailure(call: Call<List<APIRecipe>>, t: Throwable) {
        Log.e("onFailure", t.message!!)
    }

    //loads previous user input into text fields if app has been closed
    private fun loadData() {
        runOnIO {
            (binding.ingredientRecyclerView.adapter as IngredientInputAdapter).update((inpDao.getAll()))
        }
    }


    fun runOnIO(lambda: suspend () -> Unit) {
        runBlocking {
            launch(Dispatchers.IO) { lambda() }
        }
    }



}

