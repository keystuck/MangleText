package com.example.mangletext.mangling

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception

class TranslationRepository() {
    private val English = TranslateLanguage.ENGLISH
    private val languageList =
        listOf(
            TranslateLanguage.KOREAN,
            TranslateLanguage.MALAY,
            TranslateLanguage.HINDI,
            TranslateLanguage.BELARUSIAN,
            TranslateLanguage.CHINESE)

    private val displayLanguageList =
        listOf(
        "Korean",
        "Malay",
        "Hindi",
        "Belarusian",
        "Chinese")
    private var transCounter = 1;

    val languageContext = newSingleThreadContext("languageContext")

    private val _finalTrans = MutableLiveData<String>()
    val finalTrans: LiveData<String>
        get() = _finalTrans

    private val _langModels = MutableLiveData<Int>()
    val langModels: LiveData<Int>
        get() = _langModels


    private val _status = MutableLiveData<String>("Translate me")
    val status: LiveData<String>
        get() = _status

    private lateinit var translation: String

    suspend fun startTranslating(startingText: String) = runBlocking{
        translation = startingText
        withContext(languageContext){
            repeat(1){
                getTranslation()
            }
        }
    }



    fun setTranslation(savedTranslation: String){
        _status.value = "Translate with Google"
        _finalTrans.value = savedTranslation
    }


    fun getTranslation(){

        if (transCounter >= languageList.size){
            transCounter = 0
        }
            var lang1 = English
            var lang2 = languageList[transCounter]

            try {
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(lang1)
                    .setTargetLanguage(lang2)
                    .build()

                val options2 = TranslatorOptions.Builder()
                    .setSourceLanguage(lang2)
                    .setTargetLanguage(lang1)
                    .build()

                val toTranslator = Translation.getClient(options)
                val toTranslator2 = Translation.getClient(options2)

                var conditions = DownloadConditions.Builder()
                    .requireWifi()
                    .build()

                toTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        _status.value = "Through ${displayLanguageList[transCounter]}..."
                        toTranslator.translate(translation)
                            .addOnSuccessListener { translatedText ->
                                toTranslator2.translate(translatedText)
                                    .addOnSuccessListener { translated2 ->
                                        _finalTrans.value = translated2
                                        _status.value = "Translate Again"
                                    }
                                transCounter++
                            }
                            .addOnFailureListener {
                                transCounter = languageList.size +2
                            }
                    }
                    .addOnFailureListener {
                        Log.i("TransRepo", "problem")
                    }
            } catch (e: Exception) {
                Log.i("TransRepo", "caught a diff exception: ${e.message}")
            }
        }

    init{
        _langModels.value = 0
        _status.value = "Downloading..."
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        for(language in languageList)
        {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(language)
                .build()

            val translator: Translator = Translation.getClient(options)

            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    _langModels.value = _langModels.value!! + 1
                    Log.i("Repo", "downloaded or retrieved $language")
                }
                .addOnFailureListener {
                    _langModels.value = -6
                    Log.i("Repo", "error downloading $language")
                }
        }
        _status.value = "Translate with Google"
    }
}
