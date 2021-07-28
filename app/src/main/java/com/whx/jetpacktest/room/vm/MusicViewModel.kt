package com.whx.jetpacktest.room.vm

import androidx.lifecycle.*
import com.whx.jetpacktest.room.entity.PlayList
import com.whx.jetpacktest.room.entity.UserWithPlaylists
import com.whx.jetpacktest.room.repo.MusicRepository
import kotlinx.coroutines.launch

class MusicViewModel(private val repo: MusicRepository) : ViewModel() {

    val allUserWithPlaylists: LiveData<List<UserWithPlaylists>> = repo.allUserWithPlaylists.asLiveData()

    fun insertSong(playList: PlayList) {
        viewModelScope.launch {
            repo.insertPlaylist(playList)
        }
    }
}

class MusicViewModelFactory(private val repo: MusicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MusicViewModel(repo) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}