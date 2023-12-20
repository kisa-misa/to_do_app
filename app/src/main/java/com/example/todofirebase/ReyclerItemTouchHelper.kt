import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todofirebase.R
import com.example.todofirebase.TodoListAdapter

class ReyclerItemTouchHelper(context: Context, adapter: TodoListAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val adapter: TodoListAdapter
    private val context: Context

    init {
        this.adapter = adapter
        this.context = context
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Удаление задачи")
            builder.setMessage("Вы действительно хотите удалить задачу?")
            builder.setPositiveButton(
                "Подтвердить"
            ) { _, _ -> adapter.deleteTask(position) }
            builder.setNegativeButton(
                "Отменить"
            ) { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition) }
            val dialog = builder.create()
            dialog.show()
        } else {
            adapter.editTask(position)
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }
    }

    @SuppressLint("ResourceType")
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val icon: Drawable?
        val background: ColorDrawable
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20
        if (dX > 0) {
            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_edit)
            background = ColorDrawable(Color.rgb(0, 102, 51))
        } else {
            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete)
            background = ColorDrawable(Color.RED)
        }
        assert(icon != null)
        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        if (dX > 0) { // свайп право
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
            )
        } else if (dX < 0) { // свайп влево
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else {
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
        icon.draw(c)
    }
}