package com.example.mangletext

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mangletext.mangling.ManglingViewModel

class QuoteSelectionViewModelFactory(
    private val inputQuote: String?,
    private val author: String?,
    private val accessedDate: String?,
    private val app: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuoteSelectionViewModel::class.java)) {
            return QuoteSelectionViewModel(inputQuote, author, accessedDate, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}