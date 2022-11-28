package com.dicoding.rifqi.storyapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.rifqi.storyapp.R
import com.dicoding.rifqi.storyapp.data.Resource
import com.dicoding.rifqi.storyapp.data.preference.UserPreference
import com.dicoding.rifqi.storyapp.databinding.ActivityMainBinding
import com.dicoding.rifqi.storyapp.ui.MainViewModelFactory
import com.dicoding.rifqi.storyapp.ui.login.LoginActivity
import com.dicoding.rifqi.storyapp.ui.login.LoginViewModel
import com.dicoding.rifqi.storyapp.ui.story.StoryActivity
import com.dicoding.rifqi.storyapp.ui.story.StoryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var listAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setupViewModel()
        setupView()
        action()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        setupView()
    }

    private fun setupView() {
        listAdapter = StoryAdapter()

        loginViewModel.getUserToken().observe(this){ token ->
            if (token.isNotEmpty()){
                mainViewModel.stories.observe(this) {
                    when (it) {
                        is Resource.Loading -> showLoading(true)
                        is Resource.Success -> {
                            it.data?.let { stories -> listAdapter.setStory(stories) }
                            showLoading(false)
                        }
                        is Resource.Error -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.getStories(token)
                }
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        with(activityMainBinding.snapStory) {
            setHasFixedSize(true)
            adapter = listAdapter
        }
    }

    private fun action() {
        activityMainBinding.createAddStory.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle()
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        activityMainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupViewModel() {
        val pref = UserPreference.getInstance(dataStore)
        val viewModelFactory = MainViewModelFactory(pref)

        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        loginViewModel= ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.logout -> {
                val alertDialog = this.let {
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.apply {
                        setTitle(context.getString(R.string.confirmation))
                        setMessage(context.getString(R.string.you_sure))
                        setPositiveButton(
                            R.string.yes
                        ) { _, _ ->
                            mainViewModel.clearDataSession()
                            startActivity(Intent(baseContext, LoginActivity::class.java))
                            finish()
                        }
                        setNegativeButton(
                            R.string.no
                        ) { _, _ -> }
                    }
                    builder.create()
                }
                alertDialog.show()
                true
            }
            else -> true
        }
    }

    companion object {
        const val STR_TOKEN = "TOKEN"
        const val IS_SUCCESS = 201
    }
}