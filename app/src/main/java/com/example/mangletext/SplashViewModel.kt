package com.example.mangletext

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangletext.network.PhotoApi
import com.example.mangletext.network.PhotoApiService
import com.example.mangletext.network.QuoteApi
import kotlinx.coroutines.launch
import java.lang.Exception

enum class PhotoStatus {LOADING, ERROR, DONE}
class SplashViewModel: ViewModel() {
    private val _status= MutableLiveData<PhotoStatus>()
    val status: LiveData<PhotoStatus>
        get() = _status

    private val _urlToUse = MutableLiveData<String>()
    val urlToUse: LiveData<String>
        get() = _urlToUse


    init {
        getPhoto()
    }


    //TODO: remove access key
    private fun getPhoto(){
        viewModelScope.launch {
            _status.value = PhotoStatus.LOADING
            try {
                var photoURLS = PhotoApi.retrofitService.getPhoto("GygT99X_hc_qH4CwknIeF0JSv0Jor61qBgpIJOI3PmY","-McNLBYjS8w").urls
                var photoInfo = photoURLS.small
                Log.i("SplashViewModel", "url for photo: $photoInfo")
                _urlToUse.value = photoInfo
                _status.value = PhotoStatus.DONE
            } catch (e: Exception){
                _status.value = PhotoStatus.ERROR
                Log.i("SplashViewModel", "problem ${e.message}")
            }

        }
    }
}