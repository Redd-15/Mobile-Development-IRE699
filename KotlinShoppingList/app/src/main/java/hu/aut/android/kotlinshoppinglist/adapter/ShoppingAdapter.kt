package hu.aut.android.kotlinshoppinglist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import hu.aut.android.kotlinshoppinglist.MainActivity
import hu.aut.android.kotlinshoppinglist.R
import hu.aut.android.kotlinshoppinglist.adapter.ShoppingAdapter.ViewHolder
import hu.aut.android.kotlinshoppinglist.data.AppDatabase
import hu.aut.android.kotlinshoppinglist.data.Task
import hu.aut.android.kotlinshoppinglist.touch.ShoppingTouchHelperAdapter
import kotlinx.android.synthetic.main.row_item.view.*
import java.util.*

class ShoppingAdapter : RecyclerView.Adapter<ViewHolder>, ShoppingTouchHelperAdapter {
    /* ShoppingItem elemek listája*/
    private val items = mutableListOf<Task>()
    private val context: Context

    constructor(context: Context, items: List<Task>) : super() {
        this.context = context
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.row_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*Itt kérjük le az egyes ShoppingItem elemek adattagjait, itt is szükséges az adattaggal a bővítés*/
        holder.tvName.text = items[position].name
        holder.tvDay_left.text = items[position].day_left.toString()
        holder.cbDone.isChecked = items[position].done
        holder.tvDescription.text =items[position].description
        holder.tvAssigned.text =items[position].assigned
        /*Delete gomb eseménykezeője (a főoldalon)*/
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }
        /*Edit gomb eseménykezelője (a főoldalon), megnyitja az edit dialógust, átadja az adott ShoppingItem-et neki*/
        holder.btnEdit.setOnClickListener {
            (holder.itemView.context as MainActivity).showEditItemDialog(
                    items[holder.adapterPosition])
        }
        /*Checkbox eseménykezelője, állítja a checkbox értékét, azaz a ShoppingItem-nek, az isChecked adattagját.
        Az adatbázisban is frissíti
         */
        holder.cbDone.setOnClickListener {
            items[position].done = holder.cbDone.isChecked
            val dbThread = Thread {
                //Itt frissíti a DB-ben
                AppDatabase.getInstance(context).TaskItemDao().updateItem(items[position])
            }
            dbThread.start()
        }
    }
    /*Új elem hozzáadásakor hívódik meg*/
    fun addItem(item: Task) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }
    /*Elem törlésekor hívódik meg. Az adatbázisból törli az elemet (DAO-n keresztül)*/
    fun deleteItem(position: Int) {
        val dbThread = Thread {
            AppDatabase.getInstance(context).TaskItemDao().deleteItem(
                    items[position])
            (context as MainActivity).runOnUiThread{
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        }
        dbThread.start()
    }
    /*Update-kor hívódik meg*/
    fun updateItem(item: Task) {
        val idx = items.indexOf(item)
        items[idx] = item
        notifyItemChanged(idx)
    }

    override fun onItemDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*a ShoppingItem elemek, ide kell a bővítés új taggal*/
        /*Itt a gombokat, checkboxot is lekérjük*/
        val tvName: TextView = itemView.tvName
        val tvDescription:TextView=itemView.tvDescription
        val tvDay_left: TextView = itemView.tvDay_left
        val tvAssigned:TextView=itemView.tvAssigned
        val cbDone: CheckBox = itemView.cbDone
        val btnDelete: Button = itemView.btnDelete
        val btnEdit: Button = itemView.btnEdit
    }
}