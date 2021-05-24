package com.example.mangletext.mangling

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mangletext.network.QoTD

class ManglingViewModel(inputQuote: String, author:String, app: Application) : AndroidViewModel(app) {
    private val _quotation = MutableLiveData<String>()
    private val _author = MutableLiveData<String>()

    val quotation: LiveData<String>
        get() = _quotation

    val author: LiveData<String>
        get() = _author

    init{
        _quotation.value = inputQuote
        _author.value = author
    }

}