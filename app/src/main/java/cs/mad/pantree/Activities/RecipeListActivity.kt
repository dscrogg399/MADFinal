package cs.mad.pantree.Activities

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

import cs.mad.pantree.Adapters.RecipeListAdapter
import cs.mad.pantree.Databases.Database
import cs.mad.pantree.Entities.APIRecipe
import cs.mad.pantree.Entities.IngredientDao
import cs.mad.pantree.Entities.RecipeDao
import cs.mad.pantree.Entities.storeInDatabase
import cs.mad.pantree.R
import cs.mad.pantree.WebServices.RecipeWebservice
import cs.mad.pantree.databinding.RecipeListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeListActivity: AppCompatActivity() {
    private lateinit var binding: RecipeListBinding
    private lateinit var rcpDao: RecipeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_list)
        binding = RecipeListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //building database
        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, Database.dataBaseName
        ).build()
        rcpDao = db.rcpDao()

        //setting adapter
        binding.recipeRecycler.adapter = RecipeListAdapter(rcpDao)
        //calling load data
        loadData()
    }


    //loads the recipes into the adapter to populate recycler view
    private fun loadData() {
        runOnIO {
            (binding.recipeRecycler.adapter as RecipeListAdapter).update((rcpDao.getAll()))
        }
    }

    fun runOnIO(lambda: suspend () -> Unit) {
        runBlocking {
            launch(Dispatchers.IO) { lambda() }
        }
    }
}