package cs.mad.pantree.Adapters

import android.util.Log
import cs.mad.pantree.Entities.IngredientDao
import cs.mad.pantree.Entities.RecipeDao
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cs.mad.pantree.R
import cs.mad.pantree.databinding.ChecklistItemBinding
import androidx.databinding.DataBindingUtil
import cs.mad.pantree.Entities.Ingredient
import cs.mad.pantree.Entities.Recipe

//adapter for the shopping list screen, needs the ingredient dao and the id of the recipe we selected
class ChecklistAdapter(ingredientDao: IngredientDao, id: Long) : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {

    //data set
    private val dataSet = mutableListOf<Ingredient>()

    //storing references for the ingredient name and the check box
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientName: TextView = view.findViewById(R.id.checklist_ingredient)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
    }

    //inflates the checklist_item layout
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.checklist_item, viewGroup, false))
    }

    //on bind, sets the name and amounts of ingredients needed
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        val checkListText = item.name + " " + item.amount + " " + item.unit
        viewHolder.ingredientName.setText(checkListText)
        //if the item has already been obtained(user input), checks it off pre-emptively
        if (!item.missed) {
            viewHolder.checkBox.setChecked(true)
        }
    }
    //getItemCount
    override fun getItemCount(): Int {
        return dataSet.size
    }

    //updates the dataset using input from the dao in the activity
    fun update(list: List<Ingredient>?) {
        list?.let {
            dataSet.addAll(it)
            notifyDataSetChanged()
        }
    }

}