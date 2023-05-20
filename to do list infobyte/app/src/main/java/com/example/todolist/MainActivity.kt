package com.example.todolist
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(),updateAndDelete {
    lateinit var database: DatabaseReference
    var todoList:MutableList<ToDoModel>?=null
    lateinit var adapter: ToDoAdapter
    private var listViewItem: ListView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        listViewItem= findViewById<ListView>(R.id.item_listview)
        database = FirebaseDatabase.getInstance().reference


        fab.setOnClickListener{view->
            val alertDialog= AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setMessage("Add ToDo item")
            alertDialog.setTitle("Enter ToDo Item")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("add"){dialog,i->
                val todoItemdata = ToDoModel.createList()
                todoItemdata.itemDataText = textEditText.text.toString()
                todoItemdata.done = false

                val newItemdata = database.child("todo").push()
                todoItemdata.UID = newItemdata.key
                newItemdata.setValue(todoItemdata)
                dialog.dismiss()
                Toast.makeText(this,"item saved",Toast.LENGTH_LONG).show()

            }
            alertDialog.show()
        }
        todoList= mutableListOf<ToDoModel>()
        adapter= ToDoAdapter(this,todoList!!)
        listViewItem!!.adapter=adapter
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                todoList!!.clear()
                addItemToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "No Item Data", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addItemToList(snapshot: DataSnapshot) {
        val item = snapshot.children.iterator()
        if(item.hasNext())
        {
            val toDoIndexedValue = item.next()
            val itemsIterator= toDoIndexedValue.children.iterator()
            while (itemsIterator.hasNext())
            {


                    val currentItem = itemsIterator.next()
                    val toDoItemData = ToDoModel.createList()
                    val map = currentItem.getValue() as HashMap<String, Any>
                    toDoItemData.UID = currentItem.key
                    toDoItemData.done = map.get("done") as Boolean?
                    toDoItemData.itemDataText=map.get("itemDataText") as String?
                    todoList!!.add(toDoItemData)

            }
            adapter.notifyDataSetChanged()
        }

    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)

    }

    override fun onItemDelete(itemUID: String) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }
}