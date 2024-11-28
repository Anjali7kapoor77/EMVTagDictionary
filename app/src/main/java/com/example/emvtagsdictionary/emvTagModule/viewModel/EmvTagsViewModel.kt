package com.example.emvtagsdictionary.emvTagModule.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emvtagsdictionary.emvTagModule.model.EMVTag
import com.google.gson.Gson
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EMVTagViewModel : ViewModel() {

    private val _emvTagList = MutableStateFlow<List<EMVTag>>(emptyList())
    val emvTagList: StateFlow<List<EMVTag>> get() = _emvTagList

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _expandedTagIndex = MutableStateFlow(-1)
    val expandedTagIndex: StateFlow<Int> get() = _expandedTagIndex

    fun loadEMVTags(context: Context, fileName: String) {
        viewModelScope.launch {
            val listType = object : TypeToken<List<EMVTag>>() {}
            val emvTags = parseJsonFile(context, fileName, listType)
            _emvTagList.value = emvTags ?: emptyList()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateExpandedIndex(index: Int) {
        _expandedTagIndex.value = if (_expandedTagIndex.value == index) -1 else index
    }

    private fun <T> parseJsonFile(context: Context, fileName: String, typeToken: TypeToken<T>): T? {
        return try {
            val inputStream = context.assets.open(fileName)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson(jsonString, typeToken.type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
