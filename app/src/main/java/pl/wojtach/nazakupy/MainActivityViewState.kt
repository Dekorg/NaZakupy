package pl.wojtach.nazakupy

internal sealed class MainActivityViewState(
        val onElementAdded: (ShoppingListHeader) -> Unit) {

    abstract fun calculateListState(): List<ShoppingListHeader>

    class Initial(onElementAdded: (ShoppingListHeader) -> Unit) : MainActivityViewState(onElementAdded) {

        override fun calculateListState(): List<ShoppingListHeader> = emptyList()
    }

    class AddedNewElement(
            val previous: MainActivityViewState,
            val addedElement: ShoppingListHeader,
            onElementAdded: (ShoppingListHeader) -> Unit
    ) : MainActivityViewState(onElementAdded) {

        override fun calculateListState(): List<ShoppingListHeader> =
                (previous.calculateListState() + addedElement)
    }


}