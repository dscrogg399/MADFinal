package cs.mad.pantree.Entities

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class SourceUrl(
    var sourceUrl:String
)