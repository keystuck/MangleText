package com.example.mangletext.mangling

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        val outputQuote = args.output

        val navController = this.findNavController()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
         object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val action = ManglingFragmentDirections.actionManglingFragmentToSavedQuotesFragment(null)
                navController.navigate(action)
            }
        })


        binding.lifecycleOwner = this

        val repository = TranslationRepository()



        val viewModelFactory =
            ManglingViewModelFactory(repository, quotation, author, outputQuote, requireNotNull(activity).application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ManglingViewModel::class.java)
        binding.viewModel = viewModel

        if (!outputQuote.isEmpty()){
            repository.setTranslation(outputQuote)
            binding.btnTranslate.visibility = Button.GONE
           } else {
               binding.btnTranslate.visibility = Button.VISIBLE
        }



        binding.btnTranslate.setOnClickListener {
            //if there's nothing in "translated text", translate
            if (binding.translatedText1.text.isEmpty()) {
                var text = binding.tvOrigText.text
                if (text.isNotEmpty()) {
                    Log.i("ManglingFragment", "starting now")
                    viewModel.getTranslation()
                } else {
                    Toast.makeText(context, "Must enter text", Toast.LENGTH_SHORT).show()
                }
            }
            //if there is something there, save it instead
            else {
                val quoteToSave = viewModel.saveQAT()
                val action = ManglingFragmentDirections.actionManglingFragmentToSavedQuotesFragment(quoteToSave)
                navController.navigate(action)

            }
        }

        return binding.root

    }



}
