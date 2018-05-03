package pl.wojtach.nazakupy.model

import pl.wojtach.nazakupy.room.ShoppingListHeader

internal sealed class MainActivityIntent {
    object AddNewItem : MainActivityIntent()
    data class RemoveItem(val item: ShoppingListHeader) : MainActivityIntent()
}