package com.example.mangletext.mangling

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.mangletext.QuoteSelectionFragmentDirections
import com.example.mangletext.savedquotes.QATObject
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ManglingViewModel(private val repo: TranslationRepository, inputQuote: String, author:String, outputQuote: String = "", app: Application) : AndroidViewModel(app) {


    private val _quotation = MutableLiveData<String>()
    private val _author = MutableLiveData<String>()

    private val _outputQuote = repo.finalTrans
    private val _status = repo.status

    private var first: Boolean

    val quotation: LiveData<String>
        get() = _quotation

    val author: LiveData<String>
        get() = _author

    val outputQuote: LiveData<String>
        get() = _outputQuote

    val status: LiveData<String>
        get() = _status


    init {
        _quotation.value = inputQuote
        _author.value = author
        first = true
    }

    fun getTranslation() {
        Log.i("MVM", "inside getTranslation")
        if (first) {
            launchDataLoad {
                repo.startTranslating(_quotation.value!!)
                first = false
            }
        } else {
            launchDataLoad {
                repo.startTranslating(_outputQuote.value!!)
            }
        }
    }

    //TODO: is this helping?
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                Log.i("MVM", "inside launchDataLoad")
                block()
            } catch (e: Exception) {
                Log.i("MVM", "Problem: ${e.message}")
            }
        }
    }

    fun saveQAT(): QATObject {

        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)

        val date: String = simpleDateFormat.format(Date())

        //To allow you to save before translating
        val outQuote: String
        if (_outputQuote.value.isNullOrEmpty()) {
            outQuote = ""
        } else {
            outQuote = outputQuote.value!!
        }
        val quoteToSave = QATObject(_quotation.value!!, _author.value!!, outQuote, date)

        return quoteToSave
    }


    val languageList =
        listOf(
            TranslateLanguage.KOREAN,
            TranslateLanguage.MALAY,
            TranslateLanguage.HINDI,
            TranslateLanguage.BELARUSIAN,
            TranslateLanguage.CHINESE
        )





}