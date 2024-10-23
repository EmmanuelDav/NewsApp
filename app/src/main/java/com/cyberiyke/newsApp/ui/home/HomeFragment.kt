package com.cyberiyke.newsApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberiyke.newsApp.databinding.FragmentHomeBinding
import com.cyberiyke.newsApp.ui.adapter.ArticlesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

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
        binding.rvPreachings.layoutManager = LinearLayoutManager(activity)
        binding.rvPreachings.adapter = homeAdapter
        homeViewModel.fetchCachedArticles()

        homeViewModel.articleLiveData.observe(viewLifecycleOwner, Observer { news ->
            news.let {
                homeAdapter.articleMutableList = news.toMutableList()
            }
        })

        lifecycleScope.launch{
            homeViewModel.fetchArticle("us","technology","en", 20,1)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}