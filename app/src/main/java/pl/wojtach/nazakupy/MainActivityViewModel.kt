package pl.wojtach.nazakupy

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

internal class MainActivityViewModel : ViewModel() {

    private val _viewStates = MutableLiveData<MainActivityViewState>()
    val viewStates: LiveData<MainActivityViewState>
        get() = _viewStates

    init {
        _viewStates.value = MainActivityViewState.Initial(onElementAdded = this::onElementAdded)
    }

    private fun onElementAdded(element: ShoppingListHeader): Unit =
            MainActivityViewState.AddedNewElement(
                    previous = _viewStates.value!!,
                    addedElement = element,
                    onElementAdded = this::onElementAdded,
                    onElementRemoved = this::onElementRemoved
            ).let { _viewStates.setValue(it) }

    private fun onElementRemoved(element: ShoppingListHeader): Unit = with(MainActivityViewState.RemovedElement(
            previous = _viewStates.value!!,
            removedElement = element,
            onElementAdded = this::onElementAdded,
            onElementRemoved = this::onElementRemoved
    )) {
        if (calculateListState() == emptyList<ShoppingListHeader>()) _viewStates.setValue(MainActivityViewState.RemovedAllElements(onElementAdded = this@MainActivityViewModel::onElementAdded))
        else _viewStates.setValue(this)
    }
}