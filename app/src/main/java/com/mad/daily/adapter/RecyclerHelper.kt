package com.mad.daily.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


import com.mad.daily.R

class RecyclerHelper(private val adapter: TaskAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

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
            val builder = AlertDialog.Builder(adapter.getContext())
            builder.setTitle("Deletion confirmation")
                .setMessage("Are you sure you want to delete it?")
                .setPositiveButton(
                    "Yes"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    adapter.removeItem(
                        position
                    )
                }
                .setNegativeButton(
                    "No"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    adapter.notifyItemChanged(
                        position
                    )
                }
            val dialog = builder.create()
            dialog.show()
        } else {
            adapter.editItem(position)
        }
    }

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


        var iconLeft = 0
        var iconRight = 0

        if (dX > 0) {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit)
            background = ColorDrawable(Color.BLUE)
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete)
            background = ColorDrawable(Color.RED)
        }


        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconBottom = iconTop + icon.intrinsicHeight

        if (dX > 0) {
            iconLeft = itemView.left + iconMargin
            iconRight = iconLeft + icon.intrinsicWidth
            background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )
        } else if (dX < 0) {
            iconRight = itemView.right - iconMargin
            iconLeft = iconRight - icon.intrinsicWidth
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        }
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        background.draw(c)
        icon.draw(c)
    }
}