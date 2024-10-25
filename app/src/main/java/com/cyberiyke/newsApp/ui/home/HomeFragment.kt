package com.cyberiyke.newsApp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyberiyke.newsApp.R
import com.cyberiyke.newsApp.databinding.FragmentHomeBinding
import com.cyberiyke.newsApp.ui.MainActivity
import com.cyberiyke.newsApp.ui.adapter.ArticlesAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.search.SearchView.TransitionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.


    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel


    private lateinit var homeAdapter: ArticlesAdapter<HomeViewModel>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.searchBar)


        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.article.observe(viewLifecycleOwner) { news ->
            if (news.isNullOrEmpty()){
                fetchArticles()

                binding.swipeRefreshLayout.isRefreshing = true
            }else{
                binding.rvPreachings.layoutManager = LinearLayoutManager(activity)
                binding.rvPreachings.adapter = homeAdapter
                homeAdapter.articles = news.toMutableList()
                binding.swipeRefreshLayout.isRefreshing = false

            }

        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchArticles()

            binding.swipeRefreshLayout.isRefreshing = true
        }

        homeViewModel.searchResults.observe(viewLifecycleOwner){ results ->
            binding.rvResults.layoutManager = LinearLayoutManager(activity)
            binding.rvResults.adapter = homeAdapter

            if (results != null) homeAdapter.setSearchResults(results.toMutableList())

        }

        binding.searchView.addTransitionListener { searchView, previousState, newState ->
            when(newState){
                TransitionState.SHOWING ->   (activity as MainActivity).setBottomNavigationVisibility(false)
                TransitionState.HIDING -> (activity as MainActivity).setBottomNavigationVisibility(true)
                TransitionState.HIDDEN -> (activity as MainActivity).setBottomNavigationVisibility(true)
                TransitionState.SHOWN -> (activity as MainActivity).setBottomNavigationVisibility(false)
            }
        }

        // Check if the RecyclerView is scrolled to the top
        // Then refresh
        binding.rvPreachings.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val isAtTop = !recyclerView.canScrollVertically(-1)
                // Enable swipe refresh only when the RecyclerView is at the top
                binding.swipeRefreshLayout.isEnabled = isAtTop
            }
        })
        binding.rvResults.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val isAtTop = !recyclerView.canScrollVertically(-1)
                // Enable swipe refresh only when the RecyclerView is at the top
                binding.swipeRefreshLayout.isEnabled = isAtTop
            }
        })



        searchFromApi()
        setUpOnclickListener()
    }

    private fun fetchArticles() {
        lifecycleScope.launch {
            homeViewModel.fetchArticle("us", "science", "en", 20, 1)
            // Stop the refresh animation
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun setUpOnclickListener(){

         homeAdapter = ArticlesAdapter( homeViewModel, {

             val bundle = Bundle().apply {
                 putString("url", it.articleUrl) // Pass the article URL
             }
             findNavController().navigate(R.id.action_navigation_home_to_newsItemFragment, bundle)
         })
    }

    private fun searchFromApi(){
        binding.searchView.editText.apply {

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {}
            })

            setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER
                            && event.action == KeyEvent.ACTION_DOWN)) {

                    val query = text.toString()
                    homeViewModel.searchArticles(query)
                    true
                } else {
                    false
                }
            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}