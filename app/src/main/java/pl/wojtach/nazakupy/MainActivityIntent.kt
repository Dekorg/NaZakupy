package pl.wojtach.nazakupy

internal sealed class MainActivityIntent {
    object DoNothing : MainActivityIntent()
    object AddNewItem : MainActivityIntent()
    data class RemoveItem(val item: ShoppingListHeader) : MainActivityIntent()
}