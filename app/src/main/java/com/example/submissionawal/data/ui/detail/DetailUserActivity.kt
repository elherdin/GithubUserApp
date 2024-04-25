package com.example.submissionawal.data.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.example.submissionawal.R
import com.example.submissionawal.data.response.DetailUserResponse
import com.example.submissionawal.data.ui.fragment.SectionsPagerAdapter
import com.example.submissionawal.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailViewModel
    private var userData: String? = null
    private var _isChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabLyt: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabLyt, viewPager) { tabLyt, position ->
            tabLyt.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        supportActionBar?.hide()

        userData = intent.getStringExtra("username")

        val stringUser = userData.toString()

        viewModel.getDetailUser(stringUser)

        viewModel.detailUser.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val username = intent.getStringExtra(EXTRA_NAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        sectionsPagerAdapter.username = username.toString()
        sectionsPagerAdapter.id = id

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    _isChecked = count > 0
                }
                binding.toggleFavorite.isChecked = _isChecked
            }
        }

        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                if (username != null) {
                    if (avatarUrl != null) {
                        viewModel.addToFavorite(username, id, avatarUrl)
                    }
                }
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.toggleFavorite.isChecked = _isChecked
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailUser(user: DetailUserResponse) {
        binding.tvName.text = user.login
        binding.tvUsername.text = user.name
        binding.ivPhoto.load(user.avatarUrl) {
            transformations(CircleCropTransformation())
        }
        binding.tvFollowers.text = "${user.followers} Followers"
        binding.tvFollowing.text = "${user.following} Following"
    }

    companion object {
        const val EXTRA_NAME = "username"
        const val EXTRA_ID = "id"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.text1,
            R.string.text2
        )
    }
}
