package pl.wojtach.nazakupy

import java.text.SimpleDateFormat
import java.util.*

private fun createIdForNewItem(existingItems: Sequence<ShoppingListHeader>): Long =
        existingItems.map { it.id }.let { ids -> generateSequence(1L, { it + 1 }).first { it !in ids } }

internal sealed class MainActivityEvent(
        val calculateListState: () -> Sequence<ShoppingListHeader>
) {
    object Initial : MainActivityEvent(
            calculateListState = { emptyList<ShoppingListHeader>().asSequence() }
    )

    class AddedItem(previous: MainActivityEvent) : MainActivityEvent(
            calculateListState = {
                previous.calculateListState()
                        .let { previousListState ->
                            previousListState + ShoppingListHeader(
                                    id = createIdForNewItem(previousListState),
                                    formattedDate = System.currentTimeMillis()
                                            .let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) },
                                    name = "Nowa")
                        }
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
}