package com.example.mangletext

import android.app.Application
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.*
import com.example.mangletext.network.QoTD
import com.example.mangletext.network.QuoteApi
import kotlinx.coroutines.launch

enum class QoTDStatus {LOADING, ERROR, DONE}

class QuoteSelectionViewModel(inputQuote: String?, author: String?, app: Application) : AndroidViewModel(app) {

    private val resources = getApplication<Application>().resources
    private val backupQuote = resources.getString(R.string.backup_quote)
    private var testQoTD = QoTD(backupQuote,
        backupQuote.length,
        resources.getString(R.string.backup_author),
        arrayOf("hello"), "category", "en", "1-1-01", "302423", "323492", "background", "title")

    private val _status = MutableLiveData<QoTDStatus>()

    val status: LiveData<QoTDStatus>
        get() = _status

    private val _quotation = MutableLiveData<QoTD?>()

    val quotation: LiveData<QoTD?>
        get() = _quotation


    init{
        getQuoteOfTheDay(inputQuote, author)
    }

    private fun getQuoteOfTheDay(inputString:String?, author: String?) {

        if (inputString != null && author != null &&
            !inputString.isEmpty()
        ) {
            Log.i("QSVM", "use cached data $inputString and $author")
            _quotation.value = QoTD(inputString, inputString.length, author)
        } else {
            Log.i("QSVM", "get data from network")
            _quotation.value = testQoTD
//        viewModelScope.launch{
//            _status.value = QoTDStatus.LOADING
//            try{
//                var qoTD = QuoteApi.retrofitService.getQuote().contents.quotes[0]
//                _quotation.value = qoTD
//                _status.value = QoTDStatus.DONE
//                Log.i("QuoteSelectionViewModel", "done: ${qoTD.quote}")
//
//            } catch (e: Exception){
//                _status.value = QoTDStatus.ERROR
//                _quotation.value = null
//                Log.i("QuoteSelectionViewModel", "${e.message}")
//            }
//        }

        }
    }

    fun replaceQuote(text: String){
        _quotation.value = QoTD(text, text.length, "You")
    }
}

