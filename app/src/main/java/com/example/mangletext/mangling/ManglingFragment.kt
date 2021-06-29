package com.example.mangletext.mangling

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mangletext.R
import com.example.mangletext.databinding.ManglingFragmentBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*

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

        val source = args.from


        val navController = this.findNavController()


        if (source == "saved") {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val action =
                            ManglingFragmentDirections.actionManglingFragmentToSavedQuotesFragment(
                                null
                            )
                        navController.navigate(action)
                    }
                })
        }


        binding.lifecycleOwner = this

        val repository = TranslationRepository()

        //TODO: handle permissions?

        repository.langModels.observe(viewLifecycleOwner, Observer{
            if (it < 0){
                binding.tvInstructions.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black))
                binding.tvInstructions.text=getString(R.string.cant_download_models)
            }
            if (it < 5){
                binding.tvInstructions.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black))
                binding.tvInstructions.text = getString(R.string.download) + ": $it / 5"
            } else {
                binding.tvInstructions.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.gray))
                binding.tvInstructions.text = getString(R.string.translate_instr)
            }
        })


        val viewModelFactory =
            ManglingViewModelFactory(repository, quotation, author, outputQuote, requireNotNull(activity).application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ManglingViewModel::class.java)
        binding.viewModel = viewModel
        if (outputQuote.isNotEmpty()){
            repository.setTranslation(outputQuote)
           }



        binding.btnSave.setOnClickListener{
            if (binding.tvOrigText.text.isNullOrEmpty()){
                Toast.makeText(context, "Must enter text", Toast.LENGTH_SHORT).show()
            } else {
                val quoteToSave = viewModel.saveQAT()
                val action = ManglingFragmentDirections.actionManglingFragmentToSavedQuotesFragment(quoteToSave)
                navController.navigate(action)
            }
        }


        binding.btnTester.setOnClickListener{
            translate(viewModel)
            binding.viewMaster.transitionToEnd()
            binding.viewMaster.transitionToStart()

        }

        return binding.root

    }

    private fun translate(viewModel: ManglingViewModel) {
        var text = binding.tvOrigText.text
        if (text.isNotEmpty()) {
            viewModel.getTranslation()
        } else {
            Toast.makeText(context, "Must enter text", Toast.LENGTH_SHORT).show()
        }
    }


}
