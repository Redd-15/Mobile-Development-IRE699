package hu.aut.android.kotlinshoppinglist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import hu.aut.android.kotlinshoppinglist.data.Task
import kotlinx.android.synthetic.main.dialog_create_item.view.*

/*
Ez a dialógus ablak szolgál az új Shipping Item felvitelére, és a meglevő Shopping Item módosítására
 */

class TaskDialog : DialogFragment() {

    private lateinit var TaskItemHandler: TaskHandler
    //Shopping Item elemek text-ben, ide szükséges a bővítés a Shopping Item új adattagja esetén
    private lateinit var etName: EditText
    private lateinit var etDay_left: EditText
    private lateinit var etDescription: EditText
    private lateinit var etAssigned: EditText

    interface TaskHandler {
        fun TaskItemCreated(item: Task)

        fun TaskItemUpdated(item: Task)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is TaskHandler) {
            TaskItemHandler = context
        } else {
            throw RuntimeException("The Activity does not implement the ShoppingItemHandler interface")
        }
    }
/*Új Shopping Item felvitelekor ez hívódik meg. A felirat a New Item lesz*/
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New Task")

        initDialogContent(builder)

        builder.setPositiveButton("OK") { dialog, which ->
            // keep it empty
        }
        return builder.create()
    }

    private fun initDialogContent(builder: AlertDialog.Builder) {
        /*etItem = EditText(activity)
        builder.setView(etItem)*/

        //dialog_create_item.xml-el dolgozunk
        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_create_item, null)
        //Shopping Item tagok az xml-ből (EditText elemek)
        //Itt is szükséges a bővítés új Shopping Item adattag esetén
        etName = rootView.etName
        etDay_left = rootView.etDay_left
        etDescription=rootView.etDescription
        etAssigned=rootView.etAssigned
        builder.setView(rootView)
        //Megnézzük, hogy kapott-e argumentumot (a fő ablakból), ha igen, akkor az adattagokat beállítjuk erre
        // tehát az Edittext-ek kapnak értéket, és az ablak címét beállítjuk
        val arguments = this.arguments
        if (arguments != null &&
                arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
            val item = arguments.getSerializable(
                    MainActivity.KEY_ITEM_TO_EDIT) as Task
            //Itt is szükséges a bővítés új Shopping Item adattag esetén
            etName.setText(item.name)
            etDay_left.setText(item.day_left.toString())
            etDescription.setText(item.description)
            etAssigned.setText(item.assigned)

            builder.setTitle("Edit Task")
        }
    }


    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
         //OK gomb a dialógus ablakon
        //vizsgálja az eseménykezelője, hogy a dialógus ablak kapott-e paramétereket
        //Ha kapott, akkor a handleItemEdit() hívódik meg (edit)
        //Ha nem kapott, akor a handleItemCreate() hívódik meg (create)
        positiveButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null &&
                        arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog.dismiss()
            } else {
                etName.error = "This field can not be empty"
            }
        }
    }
    //Új elem esetén hvódik meg, egy új ShoppingItem-et hoz létre
    //az itemId azért null, mert a DB adja a kulcsot
    //Itt is szükséges a bővítés új Shopping Item adattag esetén
    private fun handleItemCreate() {
        TaskItemHandler.TaskItemCreated(Task(
                null,
                etName.text.toString(),
                etDescription.text.toString(),
                etAssigned.text.toString(),
                etDay_left.text.toString().toInt(),
                false
        ))
    }
    //Edit esetén hívódik meg, az edit-et csinálja, paraméterként átadja az adatokat
    //Itt is szükséges a bővítés új Shopping Item adattag esetén
    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
                MainActivity.KEY_ITEM_TO_EDIT) as Task
        itemToEdit.name = etName.text.toString()
        itemToEdit.description=etDescription.text.toString()
        itemToEdit.assigned=etAssigned.text.toString()
        itemToEdit.day_left = etDay_left.text.toString().toInt()

        TaskItemHandler.TaskItemUpdated(itemToEdit)
    }
}
