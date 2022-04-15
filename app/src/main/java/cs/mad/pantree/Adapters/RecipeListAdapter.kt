package cs.mad.pantree.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cs.mad.pantree.Activities.CheckListActivity
import cs.mad.pantree.Entities.Recipe
import cs.mad.pantree.Entities.RecipeDao
import cs.mad.pantree.R
import cs.mad.pantree.databinding.ItemRecipeBinding

//adapter for the recipe list, takes in recipes and loads their names and images into a RecipeView
class RecipeListAdapter(rcpDao: RecipeDao): RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    //dataset
    private val dataSet = mutableListOf<Recipe>()

    //stores references to the binding, image view, and recipe name text view
    class ViewHolder(bind: ItemRecipeBinding): RecyclerView.ViewHolder(bind.root) {
        val image: ImageView = bind.root.findViewById(R.id.title_image)
        val recipeName: TextView = bind.root.findViewById(R.id.recipe_name)
        val binding: ItemRecipeBinding = bind
    }

    //inflates the view
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    //onBind, populates the recipe view item
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]

        //load image using glide
        //very weird, if image is not available, somehow gives us one of those silver platters with lids that fancy meals are served in
        //this would change if we added a null or default so maybe leave it?
        Glide.with(viewHolder.binding.root)
            .load(item.image)
            .centerCrop()
            .into(viewHolder.image)

        //sets name of recipe
        viewHolder.recipeName.setText(item.title)

        //intent for launching the shopping list, adds the id so we can pull the correct ingredients
        val i = Intent(viewHolder.itemView.context, CheckListActivity::class.java)
        i.putExtra("id", item.id)

        //when you click on a recipe, opens the shopping list for that recipe
        viewHolder.binding.root.setOnClickListener {
            viewHolder.itemView.context.startActivity(i)
        }
    }

    //getItemCount
    override fun getItemCount(): Int {
        return dataSet.size
    }

    //updates the dataSet using dao in activity
    fun update(list: List<Recipe>?) {
        list?.let {
            dataSet.addAll(it)
            notifyDataSetChanged()
        }
    }
}