package pl.wojtach.nazakupy.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.launch
import pl.wojtach.nazakupy.room.HeaderDao
import pl.wojtach.nazakupy.room.ShoppingDataBase

internal class MainActivityViewModel : ViewModel() {

    private lateinit var globalEvents: LiveData<MainActivityEvent>

    private val mediator: MediatorLiveData<MainActivityEvent> = MediatorLiveData()

    val viewEvents: LiveData<MainActivityEvent>
        get() = mediator

    private lateinit var headersDao: HeaderDao

    init {
        mediator.value = MainActivityEvent.Initial

        launch {
            headersDao = ShoppingDataBase.instance.getHeadersDao()
            globalEvents = headersDao.getAllHeaders()
                    .let {
                        Transformations.map(it) { data ->
                            MainActivityEvent.LoadedFromDb(
                                    previous = viewEvents.value
                                            ?: MainActivityEvent.Initial,
                                    loadedItems = data)
                        }
                    }
            mediator.apply { addSource(globalEvents) { postValue(it) } }
        }

    }

    internal fun dispatchIntent(intent: MainActivityIntent) = launch {
        when (intent) {
            is MainActivityIntent.AddNewItem -> {
                val newHeader = createNewHeader(existingItems = mediator.value?.calculateListState?.invoke()
                        ?: emptySequence())
                launch {
                    val newEvent = MainActivityEvent.AddedItem(previous = mediator.value
                            ?: MainActivityEvent.Initial, addedItem = newHeader)
                    mediator.postValue(newEvent)
                }
                launch { headersDao.insertHeader(newHeader) }
            }

            is MainActivityIntent.RemoveItem -> {
                launch {
                    val removedEvent = MainActivityEvent.RemovedItem(
                            previous = mediator.value ?: throw IllegalArgumentException(),
                            removedItem = intent.item
                    )
                    mediator.postValue(removedEvent)
                    if (removedEvent.calculateListState().count() == 0) {
                        mediator.postValue(MainActivityEvent.RemovedAllItems(previous = removedEvent))
                    }
                }
                launch { headersDao.deleteHeader(intent.item) }
            }
        }
    }
}