package cs.mad.pantree.Databases

import androidx.room.Database
import androidx.room.RoomDatabase
import cs.mad.pantree.Entities.*

@Database(entities = [UserInput::class, Recipe::class, Ingredient::class], version = 1)
abstract class Database:RoomDatabase() {
    companion object {
        const val dataBaseName = "DATABASE"
    }

    abstract fun inpDao(): InputDao
    abstract fun rcpDao(): RecipeDao
    abstract fun ingDao(): IngredientDao
}