package com.example.mangletext.savedquotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SavedQuotesViewModel(val qatDao: QATDao) : ViewModel() {


    var savedQuotes: LiveData<List<QATObject>>

    init {

        savedQuotes = qatDao.getAll()
    }


    fun save(qatObject: QATObject){
        viewModelScope.launch { saveToDB(qatObject) }
    }

    fun deleteAt(position: Int){
        viewModelScope.launch{ deleteFromDB(savedQuotes.value!!.get(position))}
    }

    private suspend fun saveToDB(qatObject: QATObject){
            qatDao.insert(qatObject)
    }

    private suspend fun deleteFromDB(qatObject: QATObject){
        qatDao.delete(qatObject)
    }
 
}