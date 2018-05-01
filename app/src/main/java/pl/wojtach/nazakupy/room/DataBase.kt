package pl.wojtach.nazakupy.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [ShoppingListHeader::class], version = 1)
abstract class ShoppingDataBase : RoomDatabase() {

    abstract fun getHeadersDao(): HeaderDao

    companion object {
        val instance: ShoppingDataBase by lazy {
            Room.databaseBuilder(
                    appContext,
                    ShoppingDataBase::class.java,
                    ShoppingDataBase::class.java.simpleName)
                    .build()
        }

        private lateinit var appContext: Context

        fun setAppContext(appContext: Context) {
            this.appContext = appContext
        }

    }
}