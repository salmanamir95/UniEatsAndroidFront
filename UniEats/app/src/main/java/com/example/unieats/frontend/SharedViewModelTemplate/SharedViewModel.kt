package com.example.unieats.frontend.SharedViewModelTemplate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel<T>: ViewModel() {
    public var data = MutableLiveData<T>()
}