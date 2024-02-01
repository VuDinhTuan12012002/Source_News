package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.reponsitory.NewsRepository

class NewsViewModelProviderFactory(val app : Application, val newsReponsitory : NewsRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsReponsitory) as T
    }
}