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

class IngredientInputAdapter(private val inpDao: InputDao) : RecyclerView.Adapter<IngredientInputAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<UserInput>()

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
        viewHolder.ingredientName.setText(item.name)
        viewHolder.ingredientInput.hint = "Ingredient"
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


        viewHolder.deleteButton.setOnClickListener {
                runOnIO {
                    inpDao.delete(item)
                }
                    dataSet.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun addItem() {
        var newInp = UserInput(null)
        runOnIO {
            newInp.id = inpDao.insert(newInp)
        }
        dataSet.add(newInp)
        notifyItemInserted(dataSet.lastIndex)
    }

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
}