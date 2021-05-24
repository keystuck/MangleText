package com.example.mangletext.mangling

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.mangletext.R
import com.example.mangletext.databinding.ManglingFragmentBinding
import com.google.api.services.translate.Translate
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.TranslateOptions
import java.io.IOException

class ManglingFragment : Fragment() {

    val args: ManglingFragmentArgs by navArgs()
    private lateinit var translate: Translate
    private lateinit var binding: ManglingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ManglingFragmentBinding.inflate(inflater)

        val quotation = args.text
        val author = args.author

        binding.lifecycleOwner = this

        val viewModelFactory = ManglingViewModelFactory(quotation, author, requireNotNull(activity).application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(ManglingViewModel::class.java)

        binding.btnTranslate.setOnClickListener(
            getTranslateService()
        )



        return binding.root
    }

private fun getTranslateService(){
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    try{
        resources.openRawResource(R.raw.credentials).use { `is` ->
            val myCredentials = GoogleCredentials.fromStream(`is`)
            val translateOptions =
                TranslateOptions.newBuilder().setCredentials(myCredentials).build()
            translate = (TranslateOptions.DefaultTranslateFactory().create(translateOptions)
                ?: null) as Translate

            val originalText = binding.tvOrigText.text.toString()
            if (translate != null) {
                val translation = translate.translate(
                    originalText,
                    Translate.TranslateOptions.targetLanguage("tr"),
                    Translate.TranslateOption.mode("base")
                )

                binding.translatedText1.text = translation.translatedText

            }
        }
    } catch (e: IOException){
        e.printStackTrace()
    }
}

    private fun translate(){


    }
}