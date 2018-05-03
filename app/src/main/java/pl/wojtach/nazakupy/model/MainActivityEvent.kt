package pl.wojtach.nazakupy.model

import pl.wojtach.nazakupy.room.ShoppingListHeader

internal sealed class MainActivityEvent(
        val calculateListState: () -> Sequence<ShoppingListHeader>
) {
    object Initial : MainActivityEvent(
            calculateListState = { emptyList<ShoppingListHeader>().asSequence() }
    )

    class AddedItem(previous: MainActivityEvent, val addedItem: ShoppingListHeader) : MainActivityEvent(
            calculateListState = { previous.calculateListState() + addedItem }
    )

    class RemovedItem(previous: MainActivityEvent, val removedItem: ShoppingListHeader) : MainActivityEvent(
            calculateListState = { previous.calculateListState().filterNot { it == removedItem } }
    )

    class SyncedWithDb(previous: MainActivityEvent, loadedItems: Array<ShoppingListHeader>) : MainActivityEvent(
            calculateListState = { (loadedItems.asSequence() + previous.calculateListState()).distinct() }
    )
}