package hu.aut.android.kotlinshoppinglist.data

import java.io.Serializable

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/*
Adatbázis táblát készti el.
Táblanév:shoppingitem.
Oszlopok:itemId, name,  price, bought.
@PrimaryKey(autoGenerate = true): elsődleges kulcs, automatikusan generálva.
Ide szükséges a bővítés új adattal.
 */
@Entity(tableName = "tasklist")
data class Task(@PrimaryKey(autoGenerate = true) var itemId: Long?,
                @ColumnInfo(name = "name") var name: String,
                @ColumnInfo(name = "description") var description: String,
                @ColumnInfo(name = "assigned_to") var assigned: String,
                @ColumnInfo(name = "day_left") var day_left: Int,
                @ColumnInfo(name = "done") var done: Boolean
) : Serializable
