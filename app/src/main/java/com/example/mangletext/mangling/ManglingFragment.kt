package com.example.mangletext.mangling

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.mangletext.databinding.ManglingFragmentBinding
import com.google.api.services.translate.Translate
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class ManglingFragment : Fragment() {

    val args: ManglingFragmentArgs by navArgs()
    private lateinit var binding: ManglingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ManglingFragmentBinding.inflate(inflater)

        val quotation = args.text
        val author = args.author

        binding.lifecycleOwner = this

        val repository = TranslationRepository()

        val viewModelFactory =
            ManglingViewModelFactory(repository, quotation, author, requireNotNull(activity).application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ManglingViewModel::class.java)
        binding.viewModel = viewModel

        binding.btnTranslate.setOnClickListener {
            var text = binding.tvOrigText.text
            if (text.isNotEmpty()) {
                Log.i("ManglingFragment", "starting now")
                viewModel.getTranslation()
            } else {
                Toast.makeText(context, "Must enter text", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root

    }
//private fun getTranslateService(){
//    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//    StrictMode.setThreadPolicy(policy)
//
//    try{
//        resources.openRawResource(R.raw.credentials).use { `is` ->
//            val myCredentials = GoogleCredentials.fromStream(`is`)
//            val translateOptions =
//                TranslateOptions.newBuilder().setCredentials(myCredentials).build()
//            translate = (TranslateOptions.DefaultTranslateFactory().create(translateOptions)
//                ?: null) as Translate
//
//            val originalText = binding.tvOrigText.text.toString()
//            if (translate != null) {
//                val translation = translate.translate(
//                    originalText,
//                    Translate.TranslateOptions.targetLanguage("tr"),
//                    Translate.TranslateOption.mode("base")
//                )
//
//                binding.translatedText1.text = translation.translatedText
//
//            }
//        }
//    } catch (e: IOException){
//        e.printStackTrace()
//    }
//}

//    private fun translate(text: String) {
//        Log.i("ManglingFragment", "about to start working")
//        var okToTranslate1 = false
//        var translation1 = ""
//        var translation2 = ""
//        var okToTranslate2 = false
//        val options = TranslatorOptions.Builder()
//            .setSourceLanguage(TranslateLanguage.ENGLISH)
//            .setTargetLanguage(TranslateLanguage.CZECH)
//            .build()

//        val options2 = TranslatorOptions.Builder()
//            .setSourceLanguage(TranslateLanguage.CZECH)
//            .setTargetLanguage(TranslateLanguage.ENGLISH)
//            .build()
//
//
//        val englishCzechTranslator = Translation.getClient(options)
//        lifecycle.addObserver(englishCzechTranslator)
//
//        val czechEnglishTranslator = Translation.getClient(options2)
//        lifecycle.addObserver(czechEnglishTranslator)

        //TODO do we need to suspend?
        //make sure we have the language model
//        var conditions = DownloadConditions.Builder()
//            .requireWifi()
//            .build()
//        Log.i("ManglingFragment", "about to download the first trans")
//        englishCzechTranslator.downloadModelIfNeeded(conditions)
//            .addOnSuccessListener {
//                Log.i("ManglingFragment", "succeeded here")
//                okToTranslate1 = true
//
//                englishCzechTranslator.translate(text)
//                    .addOnSuccessListener { translatedText ->
//                        translation1 = translatedText
//                        Log.i("ManglingFragment", "ok with $translation1")
//
//                        czechEnglishTranslator.downloadModelIfNeeded(conditions)
//                            .addOnSuccessListener {
//
//                                okToTranslate2 = true
//                                czechEnglishTranslator.translate(translation1)
//                                    .addOnSuccessListener { translatedText ->
//                                        translation2 = translatedText
//                                        binding.translatedText1.text = translation2
//                                        Log.i("ManglingFragment", "so far okay")
//                                    }
//                                    .addOnFailureListener {
//                                        //TODO: toast?
//                                    }
//
//                                Log.i("ManglingFragment", "and here")}
//                            .addOnFailureListener { exception ->
//
//                            }
//
//                    }
//                    .addOnFailureListener { exception ->
//
//                    }
//
//
//
//            }
//            .addOnFailureListener { exception ->
//                Log.i("ManglingFragment", "failed to download")
//            }
//        Log.i("ManglingFragment", "did we get here?")

//        if (okToTranslate1) {
//            englishCzechTranslator.translate(text)
//                .addOnSuccessListener { translatedText ->
//                    translation1 = translatedText
//                    Log.i("ManglingFragment", "ok with $translation1")
//
//                }
//                .addOnFailureListener { exception ->
//
//                }
//        }
//        if (!translation1.isEmpty()) {
//            czechEnglishTranslator.translate(translation1)
//                .addOnSuccessListener { translatedText ->
//                    translation2 = translatedText
//                    Log.i("ManglingFragment", "so far okay")
//                }
//                .addOnFailureListener {
//                    //TODO: toast?
//                }
//        }

//    }
}
