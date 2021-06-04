package com.example.mangletext.savedquotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mangletext.mangling.ManglingViewModel

class SavedQuotesViewModelFactory (private val qatDao: QATDao):
ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedQuotesViewModel::class.java)) {
            return SavedQuotesViewModel(qatDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}