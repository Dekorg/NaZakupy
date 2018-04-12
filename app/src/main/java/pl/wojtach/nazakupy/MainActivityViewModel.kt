package pl.wojtach.nazakupy

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

internal class MainActivityViewModel : ViewModel() {

    private val _viewStates = MutableLiveData<MainActivityViewState>()
    val viewStates: LiveData<MainActivityViewState>
        get() = _viewStates

    init {
        _viewStates.value = MainActivityViewState.Initial
    }

    internal fun dispatchAction(action: MainActivityAction) =
            when (action) {
                is MainActivityAction.DoNothing -> Unit
                is MainActivityAction.AddNewItem -> _viewStates.postValue(
                        MainActivityViewState.AddedItem(viewStates.value
                                ?: MainActivityViewState.Initial))
                is MainActivityAction.RemoveItem -> MainActivityViewState.RemovedItem(
                        previous = viewStates.value ?: throw IllegalArgumentException(),
                        removedItem = action.item
                ).apply { _viewStates.postValue(this) }
                        .takeIf { it.calculateListState().isEmpty() }
                        ?.let { _viewStates.postValue(MainActivityViewState.RemovedAllItems(it)) }
            }
}