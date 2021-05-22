package com.example.mangletext

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer

class QuoteSelectionFragment : Fragment() {

    private val viewModel: QuoteSelectionViewModel by lazy {
        ViewModelProvider(this).get(QuoteSelectionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.quotation.observe(viewLifecycleOwner, Observer { it->
            if (it != null){
                var qotdView = view?.findViewById<TextView>(R.id.tv_qotd_text)
                qotdView?.text = it.quote
                Log.i("QuoteSelectionFragment", "got new quote...?")
            }
        })

        return inflater.inflate(R.layout.quote_selection_fragment, container, false)

    }


}