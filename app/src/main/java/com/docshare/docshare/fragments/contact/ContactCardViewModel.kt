package com.docshare.docshare.fragments.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.docshare.docshare.data.mock.MockUsers
import com.docshare.docshare.data.model.AppUser

class ContactCardViewModel : ViewModel() {
    private val _selectedUser = MutableLiveData<AppUser?>()
    val selectedUser: LiveData<AppUser?> = _selectedUser

    fun loadUser(userId: String) {
        _selectedUser.value = MockUsers.users.find { it.id == userId }
    }
}

