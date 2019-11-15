package com.example.emergencymap.ui.License

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SendViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "강릉원주대학교 컴퓨터공학과 Achieve"
    }
    val text: LiveData<String> = _text
}