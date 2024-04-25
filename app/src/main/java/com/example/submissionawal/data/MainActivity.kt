package com.example.submissionawal.data

import android.content.Intent
import com.example.submissionawal.data.ui.UserAdapter
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.submissionawal.data.local.SettingPreferences
import com.example.submissionawal.data.setting.SettingActivity
import com.example.submissionawal.data.ui.MainViewModel
import com.example.submissionawal.data.ui.favorite.FavoriteActivity
import com.example.submissionawal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString().trim()
                searchBar.setText(query)
                searchView.hide()
                dataFromApi(query)
                false
            }
        }

        adapter = UserAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.listUsers.observe(this) { listUsers ->
            adapter.submitList(listUsers)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.heartButton.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }

        binding.settingButton.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    private fun dataFromApi(query: String) {
        viewModel.searchUsers(query)
    }
}
