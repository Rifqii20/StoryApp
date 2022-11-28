package com.dicoding.rifqi.storyapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.rifqi.storyapp.data.response.ListStory
import com.dicoding.rifqi.storyapp.databinding.ActivityDetailBinding
import com.dicoding.rifqi.storyapp.ui.withDateFormat

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA = "extra_data_story"
    }

    private lateinit var activityDetailBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        val story = intent.getParcelableExtra<ListStory>(EXTRA_DATA) as ListStory
        activityDetailBinding.apply {
            story.apply {
                tvName.text = name
                tvCreatedTime.withDateFormat(story.createdAt.toString())
                tvDescription.text = description

                Glide.with(baseContext)
                    .load(photoUrl)
                    .override(500)
                    .into(ivStory)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = story.name
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}