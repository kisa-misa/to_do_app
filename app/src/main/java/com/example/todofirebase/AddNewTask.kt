package com.example.todofirebase

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewTask: BottomSheetDialogFragment(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TodoStyle)
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.new_task, null)
        val bundle = arguments
        var isUpdate = false
        builder.setView(view)
        if (bundle != null) {
            Log.d("bundle", bundle.toString())
            isUpdate = true
        }

        val addButton = view?.findViewById<Button>(R.id.add_button)
        if (isUpdate){
            val editText = view?.findViewById<EditText>(R.id.title_input)
            editText?.setText(bundle?.getString("task"))
        }
        addButton?.setOnClickListener {
            val title = view?.findViewById<EditText>(R.id.title_input)?.text.toString()


            CoroutineScope(Dispatchers.IO).launch {
                if (isUpdate){
                    val id = bundle?.getString("id")
                    if (id != null) {
                        changeTitle(id, title)
                    }
                }
                else{
                    newTask(title)
                }
            }

            dismiss()
        }

        return view
    }
}