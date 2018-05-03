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
            setupGlobalEvents()
            bindGlobalEventsToMediator()
        }

    }

    private fun bindGlobalEventsToMediator() {
        with(mediator) {
            addSource(globalEvents) { event ->
                if (getListState(event).toSet() != getLatestEvent().calculateListState().toSet()) postValue(event)
            }
        }
    }

    private fun setupGlobalEvents() {
        globalEvents = headersDao.getAllHeaders()
                .let {
                    Transformations.map(it) { data ->
                        MainActivityEvent.SyncedWithDb(
                                previous = getLatestEvent(),
                                loadedItems = data)
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
    private fun getListState(event: MainActivityEvent?) = event?.calculateListState?.invoke()
            ?: emptySequence()
}