package com.whx.jetpacktest.room.vm

import androidx.lifecycle.*
import com.whx.jetpacktest.room.entity.User
import com.whx.jetpacktest.room.entity.UserWithPlaylists
import com.whx.jetpacktest.room.repo.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository) : ViewModel() {


    val allUsers: LiveData<List<User>> = repo.allUsers.asLiveData()

    fun insert(user: User) = viewModelScope.launch {
        repo.insert(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repo.deleteUser(user)
    }
}

class UserViewModelFactory(private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}