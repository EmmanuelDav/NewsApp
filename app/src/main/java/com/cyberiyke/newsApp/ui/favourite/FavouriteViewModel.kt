package com.cyberiyke.newsApp.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cyberiyke.newsApp.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FavouriteViewModel  @Inject constructor(repository: ArticleRepository): ViewModel() {

    var favourite = repository.getFavouriteArticle()
}