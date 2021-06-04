package com.example.mangletext.mangling

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ManglingViewModelFactory(
    private val repository: TranslationRepository,
    private val inputQuote: String,
    private val author: String,
    private val outputQuote: String,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManglingViewModel::class.java)) {
            return ManglingViewModel(repository, inputQuote, author, outputQuote, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}