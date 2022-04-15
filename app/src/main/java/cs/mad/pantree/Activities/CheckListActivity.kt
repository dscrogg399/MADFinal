
package cs.mad.pantree.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.LongDef
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import cs.mad.pantree.Adapters.ChecklistAdapter
import cs.mad.pantree.Databases.Database
import cs.mad.pantree.Entities.IngredientDao
import cs.mad.pantree.Entities.InputDao
import cs.mad.pantree.Entities.RecipeDao
import cs.mad.pantree.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import cs.mad.pantree.databinding.ShoppingListBinding

class CheckListActivity : AppCompatActivity() {

//    private val id = intent.getLongExtra("id",1L)

    private lateinit var rcpDao: RecipeDao
    private lateinit var ingDao: IngredientDao
    private lateinit var inpDao: InputDao
    private lateinit var binding: ShoppingListBinding





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShoppingListBinding.inflate(layoutInflater)
        val view = binding.root
        //getting the recipe id from previous screen
        val i = intent
        val id = i.getLongExtra("id", 1L)
        setContentView(view)

        //initializing database
        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, Database.dataBaseName
        ).build()
        rcpDao = db.rcpDao()
        ingDao = db.ingDao()
        inpDao = db.inpDao()

        //setting adapter
        binding.shoppingListRecycler.adapter = ChecklistAdapter(ingDao, id)

        //button that launches webview of recipe
        binding.recipeButton.setOnClickListener {
            runOnIO {
                //getting the url of recipe we're on
                val url = rcpDao.getRecipeById(id).sourceUrl

                //setting intent and launching webview
                val i = Intent(this, WebViewActivity::class.java)
                i.putExtra("url", url)
                ContextCompat.startActivity(this, i, null)

            }
        }

        //when finished with a recipe and shopping list
        binding.doneButton.setOnClickListener {
            //clears the database
            runOnIO {
                rcpDao.deleteAll()
                ingDao.deleteAll()
                inpDao.deleteAll()
            }
            //launches IngredientInputActivity
            val i = Intent(this, IngredientInputActivity::class.java)
            ContextCompat.startActivity(this, i, null)
        }



        loadData(id)

    }

    //loads the ingredients for the recycler view
    private fun loadData(id: Long) {
        runOnIO {
            //loads the ingredients of the recipe we're on by the recipe's ID
            (binding.shoppingListRecycler.adapter as ChecklistAdapter).update((ingDao.getIngredients(id)))
        }
    }

    fun runOnIO(lambda: suspend () -> Unit) {
        runBlocking {
            launch(Dispatchers.IO) { lambda() }
        }
    }
}