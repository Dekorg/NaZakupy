package pl.wojtach.nazakupy.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity
data class ShoppingListHeader(
        @PrimaryKey val id: Long,
        val formattedDate: String,
        val name: String)

@Entity(foreignKeys = [ForeignKey(entity = ShoppingListHeader::class,
        parentColumns = ["id"],
        childColumns = ["shoppingListId"])])
data class ShoppingListItem(
        @PrimaryKey val id: Long,
        val name: String,
        val shoppingListId: Long
)