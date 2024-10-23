package com.cyberiyke.newsApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberiyke.newsApp.databinding.FragmentHomeBinding
import com.cyberiyke.newsApp.ui.adapter.ArticlesAdapter
import dagger.hilt.android.AndroidEntryPoint


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
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.fetchArticle("us","technology","en", null,null)
        binding.rvPreachings.layoutManager = LinearLayoutManager(activity)
        binding.rvPreachings.adapter = homeAdapter
        homeViewModel.article.observe(viewLifecycleOwner, Observer {
            homeAdapter.articleMutableList = it.toMutableList()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}