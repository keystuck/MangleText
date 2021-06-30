package com.example.mangletext

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mangletext.databinding.QuoteSelectionFragmentBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

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

        var quotation = ""
        var author = ""
        var accessedDate = ""


        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(Date())



        val permissionGranted = (
                PackageManager.PERMISSION_GRANTED ==
                        ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.INTERNET)
                )

        if (!permissionGranted){
            Toast.makeText(context, getString(R.string.no_internet_warning), Toast.LENGTH_SHORT).show()
            //set accessed date to current date to force use of backup string if not cached
            accessedDate = date
        }

        if (prefs!!.contains(getString(R.string.cached_date))){

                val cached_date = prefs.getString(getString(R.string.cached_date), "0")

                if (prefs.contains(getString(R.string.cached_quote))){
                    quotation = prefs.getString(getString(R.string.cached_quote), "quote missing") ?: ""
                    author = prefs.getString(getString(R.string.cached_author), "author missing") ?: ""
                    accessedDate = prefs.getString("cached_date", "date missing") ?: ""
                }
        }


        binding.lifecycleOwner = this
        val viewModelFactory = QuoteSelectionViewModelFactory(
                quotation,
                author,
                accessedDate,
                requireNotNull(activity).application
        )

        val viewModel =
                ViewModelProvider(this, viewModelFactory).get(QuoteSelectionViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.i("QuoteSelectionFragment", "QOTD status done? ${QoTDStatus.DONE == it}")
            if (it == QoTDStatus.DONE){
                binding.loadingImage.visibility = View.GONE
            }
        })


        viewModel.dateForCache.observe(viewLifecycleOwner, androidx.lifecycle.Observer { date ->
            if(date.isNotEmpty()){
                cacheNewData(
                    viewModel.quotation.value!!.quote,
                    viewModel.quotation.value!!.author,
                    prefs,
                    date
                )
            }
        })
        

        binding.btnTakeMe.setOnClickListener(View.OnClickListener {
            if (binding.tvYourOwnText.text.isEmpty()){
                quotation = binding.tvQotdText.text.toString()
                author = binding.authorName.text.toString()
            }
            else {
                quotation = binding.tvYourOwnText.text.toString()
                author = "You"

            }
            val action = QuoteSelectionFragmentDirections.actionQuoteSelectionFragmentToManglingFragment(
                quotation, author, "", "selection")
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
