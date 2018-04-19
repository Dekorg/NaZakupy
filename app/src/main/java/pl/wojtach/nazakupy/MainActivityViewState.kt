package pl.wojtach.nazakupy

private fun createIdForNewItem(existingItems: Sequence<ShoppingListHeader>): Long =
        existingItems.map { it.id }.let { ids -> generateSequence(1L, { it + 1 }).first { !(it in ids) } }

internal sealed class MainActivityViewState(
        val previous: MainActivityViewState?,
        val calculateListState: () -> Sequence<ShoppingListHeader>,
        val getAddItemAction: () -> MainActivityAction,
        val getRemoveItemAction: (ShoppingListHeader) -> MainActivityAction
) {
    object Initial : MainActivityViewState(
            previous = null,
            calculateListState = { emptyList<ShoppingListHeader>().asSequence() },
            getAddItemAction = { MainActivityAction.AddNewItem },
            getRemoveItemAction = { _ -> MainActivityAction.DoNothing }
    )

    class AddedItem(previous: MainActivityViewState) : MainActivityViewState(
            previous = previous,
            calculateListState = {
                previous.calculateListState()
                        .let { previousListState ->
                            previousListState + ShoppingListHeader(
                                    id = createIdForNewItem(previousListState),
                                    formattedDate = "dzis",
                                    name = "Nowa")
                        }
            },
            getAddItemAction = { MainActivityAction.AddNewItem },
            getRemoveItemAction = { item -> MainActivityAction.RemoveItem(item) }
    )

    class RemovedItem(previous: MainActivityViewState, removedItem: ShoppingListHeader) : MainActivityViewState(
            previous = previous,
            calculateListState = {
                previous.calculateListState()
                        .takeIf { it.contains(removedItem) }?.minus(removedItem)
                        ?: throw IllegalArgumentException()
            },
            getAddItemAction = { MainActivityAction.AddNewItem },
            getRemoveItemAction = { item -> MainActivityAction.RemoveItem(item) }
    )

    class RemovedAllItems(previous: MainActivityViewState) : MainActivityViewState(
            previous = previous,
            calculateListState = { emptyList<ShoppingListHeader>().asSequence() },
            getAddItemAction = { MainActivityAction.AddNewItem },
            getRemoveItemAction = { _ -> MainActivityAction.DoNothing }
    )
}