package com.irlab.crawlingapp.server

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irlab.crawlingapp.R

class PostAdapter(private val dataSet: ArrayList<ItemList>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.list_title)
        private val tvName: TextView = view.findViewById(R.id.list_name)
        private val tvLike: TextView = view.findViewById(R.id.list_like)

        fun bind(item: ItemList) {
            tvTitle.text = item.title
            tvName.text = item.name
            tvLike.text = item.like

            itemView.setOnClickListener {
                val intent = Intent(itemView.context,ReadPostActivity::class.java)
                intent.putExtra("제목", item.title)
                intent.putExtra("좋아요 수", item.like)
                intent.putExtra("내용", item.contents)
                intent.putExtra("작성자", item.name)
                intent.putExtra("작성일", item.postTime)

                val context = itemView.context
                context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(dataSet[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size
}