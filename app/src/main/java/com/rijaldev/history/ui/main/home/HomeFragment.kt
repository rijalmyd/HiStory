package com.rijaldev.history.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.rijaldev.history.R
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.databinding.FragmentHomeBinding
import com.rijaldev.history.ui.adapters.LoadingStateAdapter
import com.rijaldev.history.ui.adapters.StoryAdapter
import com.rijaldev.history.ui.detail.DetailActivity
import com.rijaldev.history.ui.maps.MapsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), StoryAdapter.OnItemClickCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpView()
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.home_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
            when (menuItem.itemId) {
                R.id.btn_maps -> {
                    val intent = Intent(requireActivity(), MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
    }

    private fun setUpView() {
        isOnLoading(true)
        homeViewModel.getToken().observe(viewLifecycleOwner) { token ->
            token?.let {
                homeViewModel.getStories(it).observe(viewLifecycleOwner, storiesObserver)
            }
        }

        storyAdapter = StoryAdapter(this)
        binding?.rvHome?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    private val storiesObserver = Observer<PagingData<StoryItemEntity>> {
        isOnLoading(false)
        storyAdapter.submitData(lifecycle, it)
    }

    private fun isOnLoading(isLoading: Boolean) {
        binding?.apply {
            shimmer.isVisible = isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        storyAdapter.submitData(lifecycle, PagingData.empty())
    }

    override fun onItemClicked(
        story: StoryItemEntity?, image: ImageView, desc: TextView, name: TextView, date: TextView
    ) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            Pair(desc, "description"),
            Pair(image, "image"),
            Pair(name, "name"),
            Pair(date, "date")
        )
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.DETAIL, story)
        startActivity(intent, options.toBundle())
    }
}