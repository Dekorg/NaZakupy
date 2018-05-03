package pl.wojtach.nazakupy.model

import pl.wojtach.nazakupy.room.ShoppingListHeader
import java.text.SimpleDateFormat
import java.util.*

fun createNewHeader(existingItems: Sequence<ShoppingListHeader>) = ShoppingListHeader(
        id = createIdForNewItem(existingItems),
        formattedDate = System.currentTimeMillis()
                .let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) },
        name = "Nowa")

private fun createIdForNewItem(existingItems: Sequence<ShoppingListHeader>): Long =
        existingItems.map { it.id }.let { ids -> generateSequence(1L, { it + 1 }).first { it !in ids } }