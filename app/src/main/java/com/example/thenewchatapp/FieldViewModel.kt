package com.example.thenewchatapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FieldViewModel : ViewModel() {
    private val _titles = MutableLiveData<MutableMap<String, String>>(mutableMapOf())
    val titles: LiveData<MutableMap<String, String>> = _titles

    private val _contents = MutableLiveData<MutableMap<String, String>>(mutableMapOf())
    val contents: LiveData<MutableMap<String, String>> = _contents

    // 앱이 처음 켜지고 MainFragment가 시작될 때 단 한 번만 호출해 주세요.
    fun initTitles(fieldKeys: List<String>) {
        val map = _titles.value ?: mutableMapOf()
        if (map.isEmpty()) {
            fieldKeys.forEach { key ->
                map[key] = key  // 기본값으로 자기 자신(필드명) 세팅
            }
            _titles.value = map
        }
    }

    fun setTitle(fieldKey: String, newTitle: String) {
        val map = _titles.value ?: mutableMapOf()
        map[fieldKey] = newTitle
        _titles.value = map
    }
    fun setContent(fieldKey: String, newContent: String) {
        val map = _contents.value ?: mutableMapOf()
        map[fieldKey] = newContent
        _contents.value = map
    }
    fun getTitle(fieldKey: String): String =
        _titles.value?.get(fieldKey) ?: ""
    fun getContent(fieldKey: String): String =
        _contents.value?.get(fieldKey) ?: ""
}
