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
                    onElementAdded = this::onElementAdded
            ).let { _viewStates.postValue(it) }
}