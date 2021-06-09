package com.example.mangletext.savedquotes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mangletext.databinding.SavedQuotesFragmentBinding
import com.example.mangletext.mangling.ManglingFragmentDirections

class SavedQuotesFragment : Fragment() {

    val args: SavedQuotesFragmentArgs by navArgs()




    //TODO: add deletion ability
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = QATDB.getInstance(application).qATDao
        val viewModelFactory = SavedQuotesViewModelFactory(dataSource)
        val viewModel = ViewModelProvider(
            this, viewModelFactory).get(SavedQuotesViewModel::class.java)

        val binding = SavedQuotesFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel



        // if brought here by "save quote," save quote
        val quoteToSave = args.quoteToSave
        if (quoteToSave != null){
            Log.i("SavedQuotesFragment", "saving a thing")
            viewModel.save(quoteToSave)
        }

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.savedQuotesRecview.addItemDecoration(itemDecoration)
        viewModel.savedQuotes.observe(viewLifecycleOwner, Observer {
            binding.savedQuotesRecview.adapter = QATRecyclerViewAdapter(viewModel.savedQuotes.value!!,
                {
                    val action = SavedQuotesFragmentDirections.actionSavedQuotesFragmentToManglingFragment(
                        it.quotation, it.author, it.translation
                    )
                    this.findNavController().navigate(action)
                }

            )
        })



        return binding.root
    }


}