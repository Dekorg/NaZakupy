package pl.wojtach.nazakupy.model

import pl.wojtach.nazakupy.room.ShoppingListHeader

private fun createIdForNewItem(existingItems: Sequence<ShoppingListHeader>): Long =
        existingItems.map { it.id }.let { ids -> generateSequence(1L, { it + 1 }).first { it !in ids } }

internal sealed class MainActivityEvent(
        val calculateListState: () -> Sequence<ShoppingListHeader>
) {
    object Initial : MainActivityEvent(
            calculateListState = { emptyList<ShoppingListHeader>().asSequence() }
    )

    class AddedItem(previous: MainActivityEvent, addedItem: ShoppingListHeader) : MainActivityEvent(
            calculateListState = {
                previous.calculateListState() + addedItem
            }
    )

    class RemovedItem(previous: MainActivityEvent, removedItem: ShoppingListHeader) : MainActivityEvent(
            calculateListState = {
                previous.calculateListState()
                        .takeIf { it.contains(removedItem) }?.minus(removedItem)
                        ?: throw IllegalArgumentException()
            }
    )

    class RemovedAllItems(previous: MainActivityEvent) : MainActivityEvent(
            calculateListState = { emptyList<ShoppingListHeader>().asSequence() }
    )

    class LoadedFromDb(previous: MainActivityEvent, loadedItems: Array<ShoppingListHeader>) : MainActivityEvent(
            calculateListState = { (loadedItems.asSequence() + previous.calculateListState()).distinct() }
    )
}