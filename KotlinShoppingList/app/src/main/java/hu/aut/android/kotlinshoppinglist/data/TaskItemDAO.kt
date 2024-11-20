package hu.aut.android.kotlinshoppinglist.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
/*
Itt az adatbázis műveletek találhatóak.
Új adattagkor (új ShippingItem adattag), nem szükséges módosítani itt.
 */
@Dao
interface TaskItemDAO {

    //Az összes listázása
    @Query("SELECT * FROM tasklist")
    fun findAllItems(): List<Task>

    //Egy elem beszúrása
    @Insert
    fun insertItem(item: Task): Long
    //Egy törlése
    @Delete
    fun deleteItem(item: Task)
    //Egy módosítása
    @Update
    fun updateItem(item: Task)

}
