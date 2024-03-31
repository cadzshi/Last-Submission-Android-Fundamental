package com.dicoding.midsubmission.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.midsubmission.adapter.UserAdapter
import com.dicoding.midsubmission.data.remote.response.ItemsItem
import com.dicoding.midsubmission.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private var position: Int = 0
    private var username: String? = null
    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserAdapter()
        binding.rvFollow.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        if (position == 0) {
            followViewModel.followers.observe(viewLifecycleOwner) { followers ->
                detailFollowers(followers)
            }
            followViewModel.showFollowers(username ?: "")
        } else {
            followViewModel.following.observe(viewLifecycleOwner) { following ->
                detailFollowing(following)
            }
            followViewModel.showFollowing(username ?: "")
        }
        followViewModel.showLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun detailFollowers(value: List<ItemsItem>?) {
        val adapter = UserAdapter()
        adapter.submitList(value)
        binding.rvFollow.adapter = adapter
    }

    private fun detailFollowing(value: List<ItemsItem>?) {
        val adapter = UserAdapter()
        adapter.submitList(value)
        binding.rvFollow.adapter = adapter
    }
    private fun showLoading(state: Boolean) { binding.progressBar2.visibility = if (state) View.VISIBLE else View.GONE }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"

        fun newInstance(position: Int, username: String): FollowFragment {
            return FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                    putString(ARG_USERNAME, username)
                }
            }
        }
    }
}