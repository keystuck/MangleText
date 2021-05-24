package com.example.mangletext

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mangletext.network.QoTD

enum class QoTDStatus {LOADING, ERROR, DONE}

class QuoteSelectionViewModel(inputQuote: String?, author: String?, app: Application) : AndroidViewModel(app) {

    private var testQoTD = QoTD("ohthis", 9, "somebody somewhere", arrayOf("hello"), "category", "en", "1-1-01", "302423", "323492", "background", "title")

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

        if ( inputString != null && author != null &&
            !inputString.isEmpty()) {
            _quotation.value = QoTD(inputString, inputString.length, author)
        } else {
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
//                Log.i("QuoteSelectionViewModel", "error")
//            }
//        }
        }
    }

    fun replaceQuote(text: String){
        _quotation.value = QoTD(text, text.length, "You")
    }
}

