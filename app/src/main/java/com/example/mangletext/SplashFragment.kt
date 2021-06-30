package com.example.mangletext

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


class SplashFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val permissionGranted = (
//                PackageManager.PERMISSION_GRANTED ==
//                        ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.INTERNET)
//                )

        val viewModel: SplashViewModel by viewModels()

        viewModel.urlToUse.observe(viewLifecycleOwner, Observer {
            Log.i("SplashFragment", "photo url is $it")
        })

        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        val layoutView = view.findViewById<View>(R.id.splash_screen)
        layoutView.setOnClickListener {
            val action = SplashFragmentDirections.actionSplashFragmentToQuoteSelectionFragment()
            this.findNavController().navigate(action) }


        // Inflate the layout for this fragment
        return view
    }


}