package pl.wojtach.nazakupy

import pl.wojtach.nazakupy.room.ShoppingListHeader

internal sealed class MainActivityIntent {
    object DoNothing : MainActivityIntent()
    object AddNewItem : MainActivityIntent()
    data class RemoveItem(val item: ShoppingListHeader) : MainActivityIntent()
}