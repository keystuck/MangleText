package com.example.mangletext

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangletext.network.QoTD
import com.example.mangletext.network.QuoteApi
import com.example.mangletext.network.QuoteApiService
import kotlinx.coroutines.launch
import java.lang.Exception

enum class QoTDStatus {LOADING, ERROR, DONE}

class QuoteSelectionViewModel : ViewModel() {
    private val _status = MutableLiveData<QoTDStatus>()

    val status: LiveData<QoTDStatus>
        get() = _status

    private val _quotation = MutableLiveData<QoTD?>()

    val quotation: LiveData<QoTD?>
        get() = _quotation

    init{
        getQuoteOfTheDay()
    }

    private fun getQuoteOfTheDay(){
        viewModelScope.launch{
            _status.value = QoTDStatus.LOADING
            try{
                var quote = QuoteApi.retrofitService.getQuote().contents.quotes[0]
                _quotation.value = quote
                _status.value = QoTDStatus.DONE
                Log.i("QuoteSelectionViewModel", "done: ${quote.quote}")

            } catch (e: Exception){
                _status.value = QoTDStatus.ERROR
                _quotation.value = null
                Log.i("QuoteSelectionViewModel", "error")
            }
        }
    }

}