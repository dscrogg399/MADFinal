package cs.mad.pantree.Entities

import androidx.room.*



//very very simple database entity we use to keep the text fields on the Input screen updated when the app hasn't been cleared but has been
//closed then opened again

@Entity
data class UserInput(
    var name: String?,
    @PrimaryKey (
        autoGenerate = true
            ) var id: Long? = null
    )

@Dao
interface InputDao {
    @Query("select * from userinput")
    suspend fun getAll(): List<UserInput>

    @Insert
    suspend fun insert(ui: UserInput) : Long //returns type

    @Insert
    suspend fun insertAll(ui: List<UserInput>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(ui: UserInput)

    @Delete
    suspend fun delete(ui: UserInput)

    @Query("delete from userinput")
    suspend fun deleteAll()
}
