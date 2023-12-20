package com.example.todofirebase

import ReyclerItemTouchHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var adapter: TodoListAdapter


        var recycle: RecyclerView = findViewById(R.id.todoview)
        var view = this
        recycle.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            getTasks { task ->
                adapter = TodoListAdapter(task, this@MainActivity)
                val touchHelper = ReyclerItemTouchHelper(view, adapter)
                val itemTouchHelper = ItemTouchHelper(touchHelper)
                itemTouchHelper.attachToRecyclerView(recycle)
                recycle.adapter = adapter
            }
        }



        val fab: FloatingActionButton = findViewById(R.id.addNewTask)
        fab.setOnClickListener{
            val dialogFragment = AddNewTask()
            dialogFragment.show(supportFragmentManager, "dialog")
        }
    }
}