package cs.mad.pantree.Adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import cs.mad.pantree.Entities.InputDao
import cs.mad.pantree.Entities.UserInput
import cs.mad.pantree.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//Input screen adapter, has a recycler view of text fields and automatically repopulates text fields using a data base if the app is closed and reopened without clearing
class IngredientInputAdapter(private val inpDao: InputDao) : RecyclerView.Adapter<IngredientInputAdapter.ViewHolder>() {
    //dataset
    private val dataSet = mutableListOf<UserInput>()

    //stores references to the TextInputLayout, EditText Layout, and delete button
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientInput: TextInputLayout = view.findViewById(R.id.text_field_layout)
        val ingredientName: TextInputEditText = view.findViewById(R.id.ingredient)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder (
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_ingredient, viewGroup, false)
                )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        //if we have an input already, set the text
        viewHolder.ingredientName.setText(item.name)

        //set the hint
        viewHolder.ingredientInput.hint = "Ingredient"

        //textChanged listener, updates the database if user adds a new ingredient
        viewHolder.ingredientName.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                item.name = "" + p0
            }
            override fun afterTextChanged(p0: Editable?) {
                runOnIO {
                    inpDao.update(item)
                }
            }
        })

        //delete button listener, deletes the view and removes from database
        viewHolder.deleteButton.setOnClickListener {
                runOnIO {
                    inpDao.delete(item)
                }
                    dataSet.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
        }
    }

    //getItemCount
    override fun getItemCount(): Int {
        return dataSet.size
    }

    //addItem function, creates a new blank user input field, inserts it into database
    fun addItem() {
        var newInp = UserInput(null)
        runOnIO {
            newInp.id = inpDao.insert(newInp)
        }
        dataSet.add(newInp)
        notifyItemInserted(dataSet.lastIndex)
    }

    //update used to repopulate text fields on reopen
    fun update(list: List<UserInput>?) {
        list?.let {
            dataSet.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun runOnIO(lambda: suspend () -> Unit) {
        runBlocking {
            launch(Dispatchers.IO) { lambda() }
        }
    }

    //function properly concatenates all user input into proper format for API call
    //DO NOT TOUCH, for loops are weird in Kotlin and this works
    fun getSearchString() :String {
        var inputs: List<UserInput>
        var searchString: String = ""
        runOnIO {
            inputs = inpDao.getAll()
            val size = inputs.size
            searchString = searchString + inputs[0].name.toString()
            for (i in 1..size-1) {
                searchString = searchString + ",+" + inputs[i].name.toString()
            }
        }

        return searchString

    }
}