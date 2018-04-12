package pl.wojtach.nazakupy

internal sealed class MainActivityAction {
    object DoNothing : MainActivityAction()
    object AddNewItem : MainActivityAction()
    data class RemoveItem(val item: ShoppingListHeader) : MainActivityAction()
}