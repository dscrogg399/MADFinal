package cs.mad.pantree.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class SourceUrl (
    val sourceUrl: String
        )