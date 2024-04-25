package com.example.submissionawal.data.ui.fragment

import com.example.submissionawal.data.ui.UserAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.data.ui.detail.DetailViewModel
import com.example.submissionawal.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private var username: String? = null
    private var position: Int = 0
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var adapter: UserAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        adapter = UserAdapter()
        binding.rvFollowRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@FollowFragment.adapter
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        if (position == 1) {
            detailViewModel.getFollowers(username.toString())
            detailViewModel.followers.observe(viewLifecycleOwner) { followers ->
                adapter.submitList(followers)
            }
        } else {
            detailViewModel.getFollowing(username.toString())
            detailViewModel.following.observe(viewLifecycleOwner) { following ->
                adapter.submitList(following)
            }
        }
    }

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

