package com.example.mangletext.savedquotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SavedQuotesViewModel(val qatDao: QATDao) : ViewModel() {

    //TestData
    private val qat1 = QATObject("The cat refreshes", "some guy",
    "the cat is new", "05/21/21")
    private val qat2 = QATObject( "this is the end", "another guy",
    "the end of this", "5/28/21")
    private val testList = arrayListOf(qat1, qat2)

//    private var currentQuote = MutableLiveData<QATObject?>()
 //   private val quotes = qatDao.getAll()

//    private val _savedQuotes = MutableLiveData<List<QATObject>>()

    var savedQuotes: LiveData<List<QATObject>>
//        get() = _savedQuotes

    init {
//        using test data
//        _savedQuotes.value = testList
//        using db data
//        viewModelScope.launch {
//            populateDb()
//        }
        savedQuotes = qatDao.getAll()
    }
//
//    private suspend fun populateDb(){
//        qatDao.insert(qat1)
//        qatDao.insert(qat2)
//    }

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