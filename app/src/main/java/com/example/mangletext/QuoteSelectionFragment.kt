package com.example.mangletext

import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mangletext.databinding.QuoteSelectionFragmentBinding
import java.text.SimpleDateFormat
import java.util.*

class QuoteSelectionFragment : Fragment() {

    private lateinit var binding: QuoteSelectionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = QuoteSelectionFragmentBinding.inflate(inflater)

        var prefs = activity?.getSharedPreferences(
            getString(R.string.pref_file), Context.MODE_PRIVATE
        )



        var use_cached = false
        var quotation = ""
        var author = ""
        var accessedDate = ""

        if (prefs!!.contains(getString(R.string.cached_date))){

                val cached_date = prefs.getString(getString(R.string.cached_date), "0")
//            Log.i("QuoteSelectionFragment", "retrieved date $date compared to $cached_date")
            //TODO: isn't caching??
            //if the cached date is the current (or test) date, retrieve cached data
                if (prefs.contains(getString(R.string.cached_quote))){
//                            use_cached = true

                    quotation = prefs.getString(getString(R.string.cached_quote), "quote missing") ?: ""
                    author = prefs.getString(getString(R.string.cached_author), "author missing") ?: ""
                    accessedDate = prefs.getString("cached_date", "date missing") ?: ""
//  
//                    if (prefs.contains("lastAccessed")){
//                        Log.i("QuoteSelectionFragment",
//                        prefs.getString("lastAccessed", "no value").toString())
//                    }

                }
        } else {
            Log.i("QuoteSelectionFragment", "nothing in prefs")
        }

        binding.lifecycleOwner = this
            Log.i("QSVM", "quotation $quotation and author $author")
            val viewModelFactory = QuoteSelectionViewModelFactory(
                quotation,
                author,
                accessedDate,
                requireNotNull(activity).application
            )

        val viewModel =
                ViewModelProvider(this, viewModelFactory).get(QuoteSelectionViewModel::class.java)
        binding.viewModel = viewModel


//        //TODO: checks before the quotation is retrieved. Move into viewModel?
//        if (viewModel.quotation.value != null){
//            Log.i("QSVM", "not null so was cached? $use_cached is quotation empty? ${quotation.isEmpty()}")
//           if (!use_cached || quotation.isEmpty()) {
//               Log.i("QSVM", "caching data with date $date")
//                cacheNewData(
//                    viewModel.quotation.value!!.quote ?: "",
//                    viewModel.quotation.value!!.author ?: "",
//                    prefs,
//                    date
//                )
//            }
//
//        }

        viewModel.dateForCache.observe(viewLifecycleOwner, androidx.lifecycle.Observer { date ->
            if(date.isNotEmpty()){
                cacheNewData(
                    viewModel.quotation.value!!.quote,
                    viewModel.quotation.value!!.author,
                    prefs,
                    date
                )
                Log.i("QuoteSelectionFragment", "caching new quote: ${viewModel.quotation.value}")
            }
        })
        

        binding.btnTakeMe.setOnClickListener(View.OnClickListener {
            if (binding.tvYourOwnText.text.isEmpty()){
                Log.i("QuoteSelectionFragment", "empty so translate my friend here")
                quotation = binding.tvQotdText.text.toString()
                author = binding.authorName.text.toString()
            }
            else {
                quotation = binding.tvYourOwnText.text.toString()
                author = "You"
                Log.i("QuoteSelectionFragment", "I want to use my words")

            }
            val action = QuoteSelectionFragmentDirections.actionQuoteSelectionFragmentToManglingFragment(quotation, author, "")
            this.findNavController().navigate(action)

        })

        binding.btnSeeList.setOnClickListener{
            val action = QuoteSelectionFragmentDirections.actionQuoteSelectionFragmentToSavedQuotesFragment( null)
            this.findNavController().navigate(action)
        }


        return binding.root
    }

    private fun cacheNewData(quote: String, author: String, prefs: SharedPreferences, date: String) {

        var timeStored = System.currentTimeMillis()

        with(prefs.edit()) {
            putString(
                getString(R.string.cached_quote),
                quote
            )
            putString(
                getString(R.string.cached_author),
                author
            )
            putString(
                getString(R.string.cached_date),
                date
            )
            putString(
                "lastAccessed",
                timeStored.toString()
            )

            apply()
        }

    }

}
