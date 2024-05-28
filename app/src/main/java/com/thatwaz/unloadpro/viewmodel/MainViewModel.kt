package com.thatwaz.unloadpro.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _initialCartonCount = MutableLiveData(0)
    val initialCartonCount: MutableLiveData<Int> =_initialCartonCount

    fun updateInitialCartonCount(count: Int) {
        _initialCartonCount.value = count
        Log.i("MainViewModel", "Carton count updated to ${initialCartonCount.value}")
    }
}


