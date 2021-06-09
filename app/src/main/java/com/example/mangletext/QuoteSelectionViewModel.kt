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

//TODO: preload translation models because VERY SLOW otherwise!
enum class QoTDStatus {LOADING, ERROR, DONE}

class QuoteSelectionViewModel(inputQuote: String?, author: String?, accessedDate: String?, app: Application) : AndroidViewModel(app) {

    private val resources = getApplication<Application>().resources
    private val backupQuote = resources.getString(R.string.backup_quote)
    private var testQoTD = QoTD(backupQuote,
        backupQuote.length,
        resources.getString(R.string.backup_author),
//        arrayOf("hello"), "category", "en", "1-1-01", "302423", "323492", "background", "title")
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
                Log.i("QSVM", "use cached data $inputString and $author")
                _quotation.value = QoTD(inputString, inputString.length, author)
                _dateForCache.value = ""
            }
            else {
                _quotation.value = testQoTD
            }
            } else {
                Log.i("QSVM", "get data from network")
//            _quotation.value = testQoTD
                viewModelScope.launch {
                    _status.value = QoTDStatus.LOADING
                    Log.i("QSVM", "in viewModelScope, about to start try")
                    //TODO: add timeout & show status?
                    try {
                        Log.i("QSVM", "in viewModelScope, about to start network call")
                        var contents = QuoteApi.retrofitService.getQuote().contents
                        Log.i("QSVM", "got contents")
                        var qoTD = contents.quotes[0]
                        Log.i("QSVM", "got the quotation!")
                        _quotation.value = qoTD
                        _status.value = QoTDStatus.DONE
                        Log.i("QuoteSelectionViewModel", "done: ${qoTD.quote}")
                        _dateForCache.value = date
                        Log.i("QSVM", "changing date ${_dateForCache.value}")

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

