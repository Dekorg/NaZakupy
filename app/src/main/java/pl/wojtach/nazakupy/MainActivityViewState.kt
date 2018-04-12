package pl.wojtach.nazakupy

internal sealed class MainActivityViewState(
        val calculateListState: () -> List<ShoppingListHeader>) {


    class Initial(val onElementAdded: (ShoppingListHeader) -> Unit) : MainActivityViewState(
            calculateListState = { emptyList<ShoppingListHeader>() }
    )

    class Loading(val previous: MainActivityViewState?) : MainActivityViewState(
            calculateListState = {
                (previous?.calculateListState?.invoke()) ?: emptyList()
            })

    class AddedNewElement(
            val previous: MainActivityViewState,
            addedElement: ShoppingListHeader,
            val onElementAdded: (ShoppingListHeader) -> Unit,
            val onElementRemoved: (ShoppingListHeader) -> Unit
    ) : MainActivityViewState(
            calculateListState = {
                previous.calculateListState().let { previousList ->
                    (previousList.mapNotNull { it.id }.max() ?: 0)
                            .let { previousMaxId ->
                                previousList + addedElement.copy(id = previousMaxId + 1)
                            }
                }
            })

    class RemovedElement(
            val previous: MainActivityViewState,
            removedElement: ShoppingListHeader,
            val onElementAdded: (ShoppingListHeader) -> Unit,
            val onElementRemoved: (ShoppingListHeader) -> Unit

    ) : MainActivityViewState(
            calculateListState = { (previous.calculateListState() - removedElement) }
    )

    class RemovedAllElements(
            val onElementAdded: (ShoppingListHeader) -> Unit
    ) : MainActivityViewState(
            calculateListState = { emptyList() }
    )


}