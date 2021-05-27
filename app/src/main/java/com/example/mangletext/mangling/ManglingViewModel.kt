package com.example.mangletext.mangling

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.cloud.translate.Translate
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ManglingViewModel(private val repo: TranslationRepository, inputQuote: String, author:String, app: Application) : AndroidViewModel(app) {
    private val languageList = listOf("english", "korean", "malay", "hindi", "belarusian", "chinese", "english")

    val result = repo.finalTrans

    private val _quotation = MutableLiveData<String>()
    private val _author = MutableLiveData<String>()
    private val _translatedquotes = MutableLiveData<ArrayList<String>>()
    private val _languages = MutableLiveData<ArrayList<String>>()

    private val _outputQuote =  MutableLiveData<String>()

    val quotation: LiveData<String>
        get() = _quotation

    val author: LiveData<String>
        get() = _author

    val translatedquotes: LiveData<ArrayList<String>>
        get() = _translatedquotes

    val languages: LiveData<ArrayList<String>>
        get() = _languages

    val outputQuote: LiveData<String>
        get() = _outputQuote


    init{
        _quotation.value = inputQuote
        _author.value = author

    }

    fun getTranslation(){
        Log.i("MVM", "inside getTranslation")
        launchDataLoad{
           repo.startTranslating(_quotation.value!!)
        }
    }

    private fun launchDataLoad(block: suspend() -> Unit): Job {
        return viewModelScope.launch {
            try {
                Log.i("MVM", "inside launchDataLoad")
                block()
            } catch (e: Exception){
                Log.i("MVM", "Problem")
            }
        }
    }



//    suspend fun translateOnce(input: String, lang1: String, lang2: String){
  //      Log.i("ManglingViewModel", "about to start working")

//        var language1 = when (lang1){
//            "korean" -> TranslateLanguage.KOREAN
//            "malay" -> TranslateLanguage.MALAY
//            "hindi" -> TranslateLanguage.HINDI
//            "belarusian" -> TranslateLanguage.BELARUSIAN
//             "chinese" -> TranslateLanguage.CHINESE
//            else -> TranslateLanguage.ENGLISH
//        }
//        var language2 = when (lang2){
//            "korean" -> TranslateLanguage.KOREAN
//            "malay" -> TranslateLanguage.MALAY
//            "hindi" -> TranslateLanguage.HINDI
//            "belarusian" -> TranslateLanguage.BELARUSIAN
//            "chinese" -> TranslateLanguage.CHINESE
//            else -> TranslateLanguage.ENGLISH
//        }


//        val options = TranslatorOptions.Builder()
//            .setSourceLanguage(language1)
//            .setTargetLanguage(language2)
//            .build()
//
//        val toTranslator = Translation.getClient(options)
//
//        var conditions = DownloadConditions.Builder()
//            .requireWifi()
//            .build()
//        Log.i("MVM", "is this happening?")
//        toTranslator.downloadModelIfNeeded(conditions)
//            .addOnSuccessListener {
//                Log.i("MVM", "how about this")
//                toTranslator.translate(input)
//                    .addOnSuccessListener { translatedText ->
//                        _translatedquotes.value!!.add(translatedText)
//                        Log.i("ManglingViewModel", "list size ${_translatedquotes.value!!.size}")
//                        _languages.value!!.add(language2)
//                    }
//                    .addOnFailureListener {
//                        Log.i("MVM", "problem?")
//                    }
//            }
//            .addOnFailureListener {
//                Log.i("ManglingViewModel", "problem")
//            }
//    }
  //      Log.i("MVM", "finished...?")
    //}



}