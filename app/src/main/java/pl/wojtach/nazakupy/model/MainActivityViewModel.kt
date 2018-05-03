package pl.wojtach.nazakupy.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.launch
import pl.wojtach.nazakupy.room.HeaderDao
import pl.wojtach.nazakupy.room.ShoppingDataBase
import pl.wojtach.nazakupy.room.ShoppingListHeader

internal class MainActivityViewModel : ViewModel() {

    private lateinit var globalEvents: LiveData<Array<ShoppingListHeader>>

    private val mediator: MediatorLiveData<MainActivityEvent> = MediatorLiveData()

    val viewEvents: LiveData<MainActivityEvent>
        get() = mediator

    private lateinit var headersDao: HeaderDao

    init {
        mediator.value = MainActivityEvent.Initial

        launch {
            headersDao = ShoppingDataBase.instance.getHeadersDao()
            globalEvents = headersDao.getAllHeaders()
            bindGlobalEventsToMediator()
        }

    }

    private fun bindGlobalEventsToMediator() {
        with(mediator) {
            addSource(globalEvents) { data ->
                launch {
                    data?.toSet()
                            .takeIf { it != getLatestEvent().calculateListState().toSet() }
                            ?.let {
                                postValue(MainActivityEvent.SyncedWithDb(previous = getLatestEvent(), loadedItems = data
                                        ?: emptyArray()))
                            }
                }
            }
        }
    }

    internal fun dispatchIntent(intent: MainActivityIntent) = launch { resolveIntent(intent) }

    private fun resolveIntent(intent: MainActivityIntent) = when (intent) {

        is MainActivityIntent.AddNewItem -> createNewHeader(existingItems = getLatestEvent().calculateListState())
                .let { MainActivityEvent.AddedItem(previous = getLatestEvent(), addedItem = it) }
                .let { mediator.postValue(it); headersDao.insertHeader(it.addedItem) }


        is MainActivityIntent.RemoveItem -> MainActivityEvent.RemovedItem(previous = getLatestEvent(), removedItem = intent.item)
                .let { mediator.postValue(it); headersDao.deleteHeader(it.removedItem) }
    }

    private fun getLatestEvent() = mediator.value ?: MainActivityEvent.Initial
}