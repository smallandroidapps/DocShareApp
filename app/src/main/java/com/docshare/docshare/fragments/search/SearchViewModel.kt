package com.docshare.docshare.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.docshare.docshare.data.mock.MockUsers
import com.docshare.docshare.data.model.AppUser

class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _searchResults = MutableLiveData<List<AppUser>>(emptyList())
    val searchResults: LiveData<List<AppUser>> = _searchResults

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
        val q = query.trim()
        if (q.length < 2) {
            _searchResults.value = emptyList()
            return
        }
        val lower = q.lowercase()
        _searchResults.value = MockUsers.users.filter { it.name.lowercase().contains(lower) || it.phone.contains(q) }
    }
}

