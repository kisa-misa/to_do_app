package com.example.todofirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun getBdReference(): DatabaseReference{
    return FirebaseDatabase.getInstance().reference
}

fun getTaskReference(): DatabaseReference{
    return getBdReference().child("tasks")
}

fun getTasks(onItemUpdate: (Array<Task>) -> Unit){
        val ref = getTaskReference()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tasks = dataSnapshot.children.mapNotNull { it.getValue(Task::class.java) }
                val taskStrings = tasks.toTypedArray()
                onItemUpdate(taskStrings)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // ошибка
            }
        })
}

fun deleteTaskFromDb(id: String){
    val ref = getTaskReference()
    ref.child(id).removeValue()
}

fun changeStatus(id: String, status: Boolean){
    val ref = getTaskReference()
    ref.child(id).child("status").setValue(status)
}

fun newTask(title: String){
    val ref = getBdReference()
    val key = ref.push().key
    val task = key?.let { it1 -> Task(it1, title, false) }
    if (key != null) {
        ref.child("tasks").child(key).setValue(task)
    }
}

fun changeTitle(id: String, title: String){
    val ref = getTaskReference()
    ref.child(id).child("title").setValue(title)
}