package pl.wojtach.nazakupy

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Test

class MainActivityViewStateTest {

    @Test
    fun `Initial state has no elements`() {
        MainActivityViewState.Initial.calculateListState().count() shouldBe 0
    }

    @Test
    fun `Add new item adds default item to previous sequence`() {
        MainActivityViewState.AddedItem(MainActivityViewState.Initial)
                .calculateListState().last() shouldEqual ShoppingListHeader(
                id = 1L,
                formattedDate = "dzis",
                name = "Nowa")
    }

    @Test
    fun `Add new item adds single item to previous, non Initial Sequence`() {
        MainActivityViewState.AddedItem(MainActivityViewState.AddedItem(MainActivityViewState.Initial))
                .calculateListState().count() shouldBe 2
    }

    @Test
    fun `Remove item removes item from sequence`() {
        val previousState = MainActivityViewState.AddedItem(MainActivityViewState.AddedItem(MainActivityViewState.Initial))
        val elementToBeRemoved = previousState.calculateListState().first()

        MainActivityViewState.RemovedItem(previousState, elementToBeRemoved)
                .calculateListState()
                .filter { it == elementToBeRemoved }
                .count() shouldBe 0
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Remove item throws IllegalArgumentException when trying to remove non-existing element`() {
        val previousState = MainActivityViewState.AddedItem(MainActivityViewState.AddedItem(MainActivityViewState.Initial))
        val elementToBeRemoved = ShoppingListHeader(id = -1, formattedDate = "nope", name = "nope")

        MainActivityViewState.RemovedItem(previousState, elementToBeRemoved)
                .calculateListState()
                .filter { it == elementToBeRemoved }
                .count() shouldBe 0
    }
}