package pl.wojtach.nazakupy

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import pl.wojtach.nazakupy.room.ShoppingDataBase

internal class MainActivityViewModel : ViewModel() {

    private val _viewEvents = MediatorLiveData<MainActivityEvent>()
    val viewEvents: LiveData<MainActivityEvent>
        get() = _viewEvents

    init {
        _viewEvents.value = MainActivityEvent.Initial
        ShoppingDataBase.instance.getHeadersDao().getAllHeaders()
                .let { _viewEvents.addSource(it) {data -> _viewEvents.postValue(MainActivityEvent.LoadedFromDb(previous = _viewEvents.value!!, loadedItems = data!!)) } }
    }

    internal fun dispatchIntent(intent: MainActivityIntent) =
            when (intent) {

                is MainActivityIntent.DoNothing -> Unit

                is MainActivityIntent.AddNewItem -> _viewEvents.postValue(
                        MainActivityEvent.AddedItem(viewEvents.value
                                ?: MainActivityEvent.Initial))

                is MainActivityIntent.RemoveItem -> MainActivityEvent.RemovedItem(
                        previous = viewEvents.value ?: throw IllegalArgumentException(),
                        removedItem = intent.item
                ).apply { _viewEvents.postValue(this) }
                        .takeIf { it.calculateListState().count() == 0 }
                        ?.let { _viewEvents.postValue(MainActivityEvent.RemovedAllItems(it)) }
            }
}