package cs.mad.pantree.Entities

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

//simple structure for pulling the URL of a recipe
@Serializable
data class SourceUrl(
    var sourceUrl:String
)