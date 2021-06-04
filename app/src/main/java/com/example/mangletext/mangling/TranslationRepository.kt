package com.example.mangletext.mangling

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.api.services.translate.Translate
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception

class TranslationRepository() {
    private val languageList =
        listOf(TranslateLanguage.ENGLISH,
            TranslateLanguage.KOREAN,
            TranslateLanguage.MALAY,
            TranslateLanguage.HINDI,
            TranslateLanguage.BELARUSIAN,
            TranslateLanguage.CHINESE,
            TranslateLanguage.ENGLISH)


    private val displayLanguageList =
        listOf("English",
        "Korean",
        "Malay",
        "Hindi",
        "Belarusian",
        "Chinese",
        "English")
    private var transCounter = 0;



    val languageContext = newSingleThreadContext("languageContext")

    private val _finalTrans = MutableLiveData<String>()
    val finalTrans: LiveData<String>
        get() = _finalTrans

    //field to show if we need to translate at all
    //1: to translate
    //2: translating
    //3: done
    //4: no translation
    private val _status = MutableLiveData<String>("Translate me")
    val status: LiveData<String>
        get() = _status

    private lateinit var translation: String

    suspend fun startTranslating(startingText: String) = runBlocking{
        Log.i("Repo", "status is ${_status.value}")
        translation = startingText
        withContext(languageContext){
            repeat(1){
                getTranslation()
            }
        }
    }

    fun setTranslation(savedTranslation: String){
        _status.value = "Saved values"
        _finalTrans.value = savedTranslation
    }


    fun getTranslation(){

        if (transCounter < languageList.size-1) {
            var lang1 = languageList[transCounter]
            var lang2 = languageList[transCounter + 1]
            Log.i("Repo", "from $lang1 to $lang2")

            try {
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(lang1)
                    .setTargetLanguage(lang2)
                    .build()

                val toTranslator = Translation.getClient(options)

                var conditions = DownloadConditions.Builder()
                    .requireWifi()
                    .build()

                toTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        Log.i("MVM", "how about this")
                        _status.value = "Translating into ${displayLanguageList[transCounter + 1]}..."
                        toTranslator.translate(translation)
                            .addOnSuccessListener { translatedText ->
                                translation = translatedText
                                transCounter++
                                getTranslation()
                                if (transCounter == languageList.size - 1){
                                    _finalTrans.value = translatedText
                                    _status.value = "Save translation"
                                }
                                Log.i("Repo", "setting translation to $translation")
                            }
                            .addOnFailureListener {
                                Log.i("TransRepo", "problem?")
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

    }
}
