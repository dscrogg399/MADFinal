package cs.mad.pantree.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.room.Dao
import androidx.room.Room
import androidx.room.RoomDatabase
import cs.mad.pantree.Adapters.IngredientInputAdapter
import cs.mad.pantree.R
import cs.mad.pantree.Databases.IngredientDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import cs.mad.pantree.Entities.IngredientDao
import cs.mad.pantree.databinding.ActivityIngredientInputBinding
import cs.mad.pantree.databinding.ActivityIngredientInputBinding.inflate

class IngredientInputActivity : AppCompatActivity() {
    //binding var
    private lateinit var binding: ActivityIngredientInputBinding
    private lateinit var ingDao: IngredientDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient_input)
        binding = ActivityIngredientInputBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val db = Room.databaseBuilder(
            applicationContext,
            IngredientDatabase::class.java, IngredientDatabase.dataBaseName
        ).build()
        ingDao = db.ingDao()

        binding.ingredientRecyclerView.adapter = IngredientInputAdapter(ingDao)

        binding.addIngredientButton.setOnClickListener {
            (binding.ingredientRecyclerView.adapter as IngredientInputAdapter).addItem()
                binding.ingredientRecyclerView.smoothScrollToPosition((binding.ingredientRecyclerView.adapter as IngredientInputAdapter).itemCount - 1)
        }

        loadData()




    }


    private fun loadData() {
        runOnIO {
            (binding.ingredientRecyclerView.adapter as IngredientInputAdapter).update((ingDao.getAll()))
        }
    }

    private fun clearStorage() {
        runOnIO {
            ingDao.deleteAll()
        }
    }

    fun runOnIO(lambda: suspend () -> Unit) {
        runBlocking {
            launch(Dispatchers.IO) { lambda() }
        }
    }



}