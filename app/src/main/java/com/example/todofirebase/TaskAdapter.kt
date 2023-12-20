package com.example.todofirebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodoListAdapter(private var myDataset: Array<Task>, private var activity: MainActivity) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myDataset[position]
        holder.checkBox.text = item.title
        holder.checkBox.isChecked = item.status
        holder.checkBox.setOnCheckedChangeListener{ _, isChecked ->
            changeTaskStatus(position, isChecked)
        }
    }

    fun deleteTask(position: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val item = myDataset[position]

            deleteTaskFromDb(item.id)
            val result = myDataset.toMutableList()
            result.removeAt(position)
            myDataset = result.toTypedArray()
        }
    }

    private fun changeTaskStatus(position: Int, status: Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val item = myDataset[position]

            changeStatus(item.id, status)
        }
    }

    fun editTask(position: Int){
        val item = myDataset[position]
        val bundle = Bundle()
        bundle.putString("id", item.id)
        bundle.putString("task", item.title)
        val fragment = AddNewTask()
        fragment.arguments = bundle
        fragment.show(activity.supportFragmentManager, "dialog")
    }

    override fun getItemCount() = myDataset.size


}