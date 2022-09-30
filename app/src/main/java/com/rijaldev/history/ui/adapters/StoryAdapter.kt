package com.rijaldev.history.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.databinding.ItemStoryBinding

class StoryAdapter(
    private val onItemClickCallback: OnItemClickCallback
) : PagingDataAdapter<StoryItemEntity, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    inner class StoryViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryItemEntity?) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story?.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = story?.name
                tvItemDescription.text = story?.description
                tvItemDate.text = story?.createdAt
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(
                        story,
                        ivItemPhoto,
                        tvItemDescription,
                        tvItemName,
                        tvItemDate
                    )
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(
            story: StoryItemEntity?,
            image: ImageView,
            desc: TextView,
            name: TextView,
            date: TextView
        )
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItemEntity>() {
            override fun areItemsTheSame(
                oldItem: StoryItemEntity,
                newItem: StoryItemEntity,
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: StoryItemEntity,
                newItem: StoryItemEntity,
            ): Boolean = oldItem.id == newItem.id
        }
    }
}