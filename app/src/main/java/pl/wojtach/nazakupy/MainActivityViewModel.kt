package pl.wojtach.nazakupy

import android.arch.lifecycle.*
import android.util.Log
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import pl.wojtach.nazakupy.room.HeaderDao
import pl.wojtach.nazakupy.room.ShoppingDataBase

internal class MainActivityViewModel : ViewModel() {

    private lateinit var globalEvents: LiveData<MainActivityEvent>

    private val mediator: MediatorLiveData<MainActivityEvent> = MediatorLiveData<MainActivityEvent>()

    val viewEvents: LiveData<MainActivityEvent>
        get() = mediator

    private lateinit var headersDao: HeaderDao

    init {
        mediator.value = MainActivityEvent.Initial

        launch {
            headersDao = ShoppingDataBase.instance.getHeadersDao()
            globalEvents = headersDao.getAllHeaders()
                    .let { Transformations.map(it) { data ->
                        MainActivityEvent.LoadedFromDb(
                                previous = viewEvents.value ?: MainActivityEvent.Initial,
                                loadedItems = data)
                    } }
            mediator.apply { addSource(globalEvents) { postValue(it)} }
        }

    }

    internal fun dispatchIntent(intent: MainActivityIntent) =
            when (intent) {

                is MainActivityIntent.DoNothing -> Unit

                is MainActivityIntent.AddNewItem -> mediator.postValue(
                        MainActivityEvent.AddedItem(viewEvents.value
                                ?: MainActivityEvent.Initial))

                is MainActivityIntent.RemoveItem -> MainActivityEvent.RemovedItem(
                        previous = viewEvents.value ?: throw IllegalArgumentException(),
                        removedItem = intent.item
                ).apply { mediator.postValue(this) }
                        .takeIf { it.calculateListState().count() == 0 }
                        ?.let { mediator.postValue(MainActivityEvent.RemovedAllItems(it)) }
            }

    override fun onCleared() {
        launch {
            headersDao.insertHeader(*viewEvents.value?.calculateListState?.invoke()?.toList()?.toTypedArray() ?: emptyArray())
        Log.d("MainActivityVievModel", "saving headers")
        }
        super.onCleared()
        Log.d("MainActivityVievModel", "viewmodel cleared")
    }
}