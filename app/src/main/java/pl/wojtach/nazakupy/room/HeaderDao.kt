package pl.wojtach.nazakupy.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface HeaderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
fun insertHeader(vararg header: ShoppingListHeader): Unit

    @Delete
fun deleteHeader(vararg header: ShoppingListHeader): Unit

    @Query("SELECT * FROM ShoppingListHeader")
fun getAllHeaders(): LiveData<Array<ShoppingListHeader>>


    @Query("SELECT * FROM ShoppingListHeader WHERE id = :id")
    fun getHeaderById(id: Long): LiveData<ShoppingListHeader>
}