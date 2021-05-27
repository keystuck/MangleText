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

    private var transCounter = 0;


    val languageContext = newSingleThreadContext("languageContext")
    private val _finalTrans = MutableLiveData<String>()
    val finalTrans: LiveData<String>
        get() = _finalTrans

    private lateinit var translation: String

    suspend fun startTranslating(startingText: String) = runBlocking{
        translation = startingText
        withContext(languageContext){
            repeat(languageList.size - 1){
                getTranslation()
            }
        }

            _finalTrans.value = translation

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
                        toTranslator.translate(translation)
                            .addOnSuccessListener { translatedText ->
                                translation = translatedText
                                Log.i("Repo", "setting translation to $translation")
                            }
                            .addOnFailureListener {
                                Log.i("TransRepo", "problem?")
                            }
                    }
                    .addOnFailureListener {
                        Log.i("TransRepo", "problem")
                    }
            } catch (e: Exception) {
                Log.i("TransRepo", "caught a diff exception: ${e.message}")
            }
        }
        transCounter++;
    }
}
