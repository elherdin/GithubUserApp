package com.example.submissionawal.data.ui.favorite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.data.response.ItemsItem
import com.example.submissionawal.data.ui.UserAdapter
import com.example.submissionawal.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        supportActionBar?.hide()

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUser.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this) { favoriteUsers ->
            if (favoriteUsers != null) {
                val items = favoriteUsers.map { favoriteUser ->
                    ItemsItem(
                        favoriteUser.login,
                        favoriteUser.id,
                        favoriteUser.avatarUrl
                    )
                }
                adapter.submitList(items)
            }
        }
    }
}