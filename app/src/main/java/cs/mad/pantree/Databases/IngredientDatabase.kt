package cs.mad.pantree.Databases

import androidx.room.Database
import androidx.room.RoomDatabase
import cs.mad.pantree.Entities.Ingredient
import cs.mad.pantree.Entities.IngredientDao

@Database(entities = [Ingredient::class], version = 1)
abstract class IngredientDatabase:RoomDatabase() {
    companion object {
        const val dataBaseName = "INGREDIENT_DATABASE"
    }

    abstract fun ingDao(): IngredientDao
}