package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.Article
import com.example.myapplication.models.NewsReponse
import com.example.myapplication.reponsitory.NewsRepository
import com.example.myapplication.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(app : Application, val newsRepository: NewsRepository ) : AndroidViewModel(app) {
    val headlines : MutableLiveData<Resource<NewsReponse>> = MutableLiveData()
    //trang tieu de
    var headlinePage = 1
    var headlineResponse : NewsReponse? = null

    val search : MutableLiveData<Resource<NewsReponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse : NewsReponse? = null
    var newSearchQuery : String? = null
    var oldSearchQuery : String? = null

    private fun handleHeadlinesResponse(response: Response<NewsReponse>) : Resource<NewsReponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                headlinePage++
                if(headlineResponse ==null){
                    headlineResponse = resultResponse
                } else {
                    val oldArticles = headlineResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(headlineResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsReponse>): Resource<NewsReponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                if(searchNewsResponse == null || newSearchQuery != oldSearchQuery){
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else{
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavourites(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
}