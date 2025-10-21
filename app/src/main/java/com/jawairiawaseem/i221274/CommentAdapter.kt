package com.jawairiawaseem.i221274

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jawairiawaseem.i221274.model.Comment
import android.view.View

class CommentAdapter(private val items: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tv: TextView = v as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val tv = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return VH(tv)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val c = items[pos]
        h.tv.text = "${c.username}: ${c.text}"
    }

    override fun getItemCount() = items.size

    fun submit(list: List<Comment>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }
}
