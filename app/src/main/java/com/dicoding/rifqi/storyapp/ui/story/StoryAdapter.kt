package com.dicoding.rifqi.storyapp.ui.story

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.rifqi.storyapp.data.response.ListStory
import com.dicoding.rifqi.storyapp.databinding.ItemStoryBinding
import com.dicoding.rifqi.storyapp.ui.main.DetailActivity
import com.dicoding.rifqi.storyapp.ui.withDateFormat

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private val listStory = ArrayList<ListStory>()

    @SuppressLint("NotifyDataSetChanged")
    fun setStory(story: ArrayList<ListStory>) {
        listStory.clear()
        listStory.addAll(story)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    inner class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStory) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(binding.imgStory)
                tvRvName.text = story.name
                tvRvCreate.withDateFormat(story.createdAt.toString())
                tvRvDesc.text = story.description
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_DATA, story)
                }
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgStory, "image"),
                        Pair(binding.tvRvName, "name"),
                        Pair(binding.tvRvDesc, "description"),
                    )
                it.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}