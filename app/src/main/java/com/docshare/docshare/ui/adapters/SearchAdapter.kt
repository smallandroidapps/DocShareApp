package com.docshare.docshare.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.docshare.docshare.R
import com.docshare.docshare.data.model.AppUser

class SearchAdapter(private val onClick: (AppUser) -> Unit) :
    ListAdapter<AppUser, SearchAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<AppUser>() {
            override fun areItemsTheSame(oldItem: AppUser, newItem: AppUser): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AppUser, newItem: AppUser): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_search_user, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
        holder.btnView.setOnClickListener { onClick(item) }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val btnView: Button = itemView.findViewById(R.id.btnView)

        fun bind(user: AppUser) {
            tvName.text = user.name
            tvPhone.text = user.phone
            ivAvatar.setImageResource(R.drawable.ic_image)
        }
    }
}

