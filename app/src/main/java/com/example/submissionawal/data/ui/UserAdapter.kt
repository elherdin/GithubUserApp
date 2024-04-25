package com.example.submissionawal.data.ui

import android.content.Intent
import com.example.submissionawal.data.response.ItemsItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.submissionawal.data.ui.detail.DetailUserActivity
import com.example.submissionawal.databinding.UserLayoutBinding

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsItem) {
            binding.tvName.text = item.login
            binding.ivPhoto.load(item.avatarUrl) {
                transformations(CircleCropTransformation())
            }

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailUserActivity::class.java)
                intent.putExtra("username", item.login)
                intent.putExtra("id", item.id)
                intent.putExtra("extra_url", item.avatarUrl)
                context.startActivity(intent)
            }
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<ItemsItem>() {
        override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem == newItem
        }
    }
}
