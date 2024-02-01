package com.example.myapplication.models

data class NewsReponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)