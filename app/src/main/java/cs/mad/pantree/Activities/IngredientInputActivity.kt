package cs.mad.pantree.Activities

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

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, Database.dataBaseName
        ).build()
        inpDao = db.inpDao()
        rcpDao = db.rcpDao()
        ingDao = db.ingDao()

        binding.ingredientRecyclerView.adapter = IngredientInputAdapter(inpDao)

        binding.addIngredientButton.setOnClickListener {
            (binding.ingredientRecyclerView.adapter as IngredientInputAdapter).addItem()
                binding.ingredientRecyclerView.smoothScrollToPosition((binding.ingredientRecyclerView.adapter as IngredientInputAdapter).itemCount - 1)
        }

        loadData()

        apiTest()

    }


    private fun loadData() {
        runOnIO {
            (binding.ingredientRecyclerView.adapter as IngredientInputAdapter).update((inpDao.getAll()))
        }
    }

    private fun clearStorage() {
        runOnIO {
            inpDao.deleteAll()
        }
    }

    fun runOnIO(lambda: suspend () -> Unit) {
        runBlocking {
            launch(Dispatchers.IO) { lambda() }
        }
    }

    private fun apiTest() {
        RecipeWebservice().recipeSearchService.searchRecipes("5fb674b8b4e3412993d19c551979f844", "apples,+flour,+sugar", 5, 1).enqueue(this)


    }

    override fun onResponse(
        call: Call<List<APIRecipe>>,
        response: Response<List<APIRecipe>>
    ) {
        if (response.isSuccessful) {
            Log.d("onResponse", "download success!")
            val arcps = response.body()
            arcps?.map { it.storeInDatabase(rcpDao, ingDao) }


        }
    }



    override fun onFailure(call: Call<List<APIRecipe>>, t: Throwable) {
        Log.e("onFailure", t.message!!)
    }
}

