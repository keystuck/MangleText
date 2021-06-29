package com.example.mangletext

import android.app.Application
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.mangletext.network.QoTD
import com.example.mangletext.network.QuoteApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class QoTDStatus {LOADING, ERROR, DONE}

class QuoteSelectionViewModel(inputQuote: String?, author: String?, accessedDate: String?, app: Application) : AndroidViewModel(app) {

    private val resources = getApplication<Application>().resources
    private val backupQuote = resources.getString(R.string.backup_quote)
    private var testQoTD = QoTD(backupQuote,
        backupQuote.length,
        resources.getString(R.string.backup_author)
            )

    private val _status = MutableLiveData<QoTDStatus>()

    val status: LiveData<QoTDStatus>
        get() = _status

    private val _dateForCache = MutableLiveData<String>()

    val dateForCache: LiveData<String>
        get() = _dateForCache

    private val _quotation = MutableLiveData<QoTD?>()

    val quotation: LiveData<QoTD?>
        get() = _quotation


    init{
        getQuoteOfTheDay(inputQuote, author, accessedDate)
    }

    private fun getQuoteOfTheDay(inputString:String?, author: String?, accessedDate: String?) {
        //get the current date to check against the cached value
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)

        //real date
        val date: String = simpleDateFormat.format(Date())

        //test date to force mismatch
//      val date = "2020-03-02"

        //we have current values - no network needed
        if (accessedDate.equals(date)) {

            if (inputString != null && author != null &&
                inputString.isNotEmpty()
            ) {
                _quotation.value = QoTD(inputString, inputString.length, author)
                _dateForCache.value = ""
            }
            else {
                _quotation.value = testQoTD
            }
            } else {
//            _quotation.value = testQoTD
                viewModelScope.launch {
                    _status.value = QoTDStatus.LOADING
                    //TODO: add timeout & show status?
                    try {
                        var contents = QuoteApi.retrofitService.getQuote().contents
                        var qoTD = contents.quotes[0]
                        _quotation.value = qoTD
                        _status.value = QoTDStatus.DONE
                        _dateForCache.value = date

                    } catch (e: Exception) {
                        _status.value = QoTDStatus.ERROR
                        _quotation.value = testQoTD
                        Toast.makeText(
                            getApplication(),
                            "unable to retrieve quote; using sample instead",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i("QuoteSelectionViewModel", "${e.message}")
                        _dateForCache.value = ""
                    }
                }

            }

    }


}

