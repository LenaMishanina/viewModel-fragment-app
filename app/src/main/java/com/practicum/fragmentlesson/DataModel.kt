package com.practicum.fragmentlesson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel : ViewModel() {
    //by lazy - создает message только один раз,
    // при создании нового объекта DataModel данные в message можно обновить,
    // но они не создадутся заново
    val messageForActivity: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val messageForFragment1: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val messageForFragment2: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}