package pl.wojtach.nazakupy

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Test
import pl.wojtach.nazakupy.room.ShoppingListHeader
import java.text.SimpleDateFormat
import java.util.*

class MainActivityEventTest {

    @Test
    fun `Initial state has no elements`() {
        MainActivityEvent.Initial.calculateListState().count() shouldBe 0
    }

    @Test
    fun `Add new item adds default item to previous sequence`() {
        MainActivityEvent.AddedItem(MainActivityEvent.Initial)
                .calculateListState().last() shouldEqual ShoppingListHeader(
                id = 1L,
                formattedDate = System.currentTimeMillis()
                        .let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) },
                name = "Nowa")
    }

    @Test
    fun `Add new item adds single item to previous, non Initial Sequence`() {
        MainActivityEvent.AddedItem(MainActivityEvent.AddedItem(MainActivityEvent.Initial))
                .calculateListState().count() shouldBe 2
    }

    @Test
    fun `Remove item removes item from sequence`() {
        val previousState = MainActivityEvent.AddedItem(MainActivityEvent.AddedItem(MainActivityEvent.Initial))
        val elementToBeRemoved = previousState.calculateListState().first()

        MainActivityEvent.RemovedItem(previousState, elementToBeRemoved)
                .calculateListState()
                .filter { it == elementToBeRemoved }
                .count() shouldBe 0
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Remove item throws IllegalArgumentException when trying to remove non-existing element`() {
        val previousState = MainActivityEvent.AddedItem(MainActivityEvent.AddedItem(MainActivityEvent.Initial))
        val elementToBeRemoved = ShoppingListHeader(id = -1, formattedDate = "nope", name = "nope")

        MainActivityEvent.RemovedItem(previousState, elementToBeRemoved)
                .calculateListState()
                .filter { it == elementToBeRemoved }
                .count() shouldBe 0
    }
}