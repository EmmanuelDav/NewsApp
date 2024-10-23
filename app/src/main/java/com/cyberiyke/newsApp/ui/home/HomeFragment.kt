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
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout


    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    private val homeAdapter = ArticlesAdapter {}


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
            binding.rvPreachings.layoutManager = LinearLayoutManager(activity)
            binding.rvPreachings.adapter = homeAdapter
            if (news != null) homeAdapter.articles = news.toMutableList()

        }

        homeViewModel.searchResults.observe(viewLifecycleOwner){ results ->
            if (results != null) homeAdapter.setSearchResults(results.toMutableList())
            binding.rvResults.adapter = homeAdapter

        }

        lifecycleScope.launch {
            homeViewModel.fetchArticle("us", "science", "en", 20, 1)
        }


        binding.searchView.addTransitionListener { searchView, previousState, newState ->
            if (newState === TransitionState.SHOWING) {
                (activity as MainActivity).setBottomNavigationVisibility(true)

            } else {
                (activity as MainActivity).setBottomNavigationVisibility(false)
                homeAdapter.exitSearchMode()
            }
        }

        searchFromApi()
    }

    private fun searchFromApi(){
        binding.rvResults.layoutManager = LinearLayoutManager(activity)
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