package com.whx.jetpacktest.widget

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.dp
import kotlinx.android.synthetic.main.activity_widget_test.*

class WidgetTestActivity : BaseActivity(), ActionMode.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_widget_test)

        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 10.dp().toInt()
            }
        })
        list.adapter = MAdapter()

        scrollView.setTextList(listOf("what", "the", "fuck"))

        selectable_text.customSelectionActionModeCallback = this
    }

    override fun onResume() {
        super.onResume()
        marquee_text.startScroll()
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val menuInflater = mode?.menuInflater
        if (menu != null && menuInflater != null) {
            menu.clear()
            menuInflater.inflate(R.menu.select_menu, menu)

            return true
        }
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.select_item1 -> {
                Toast.makeText(this, "item 1", Toast.LENGTH_SHORT).show()
            }
            R.id.select_item2 -> {
                Toast.makeText(this, "item 2", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {

    }

    class MAdapter : RecyclerView.Adapter<MViewHolder>() {
        override fun getItemCount(): Int {
            return 2
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
            return MViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.temp_item, parent, false))
        }

        override fun onBindViewHolder(holder: MViewHolder, position: Int) {
            holder.bindView(position)
        }
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(position: Int) {
            if (itemView is SwipeView) {
                itemView.findViewById<TextView>(R.id.delete_text).text = "delete#$position"
                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, "delete#$position", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}