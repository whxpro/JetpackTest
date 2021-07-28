package com.whx.jetpacktest.room.repo

import com.whx.jetpacktest.room.dao.MusicDao
import com.whx.jetpacktest.room.entity.PlayList
import kotlinx.coroutines.flow.Flow

class MusicRepository(private val musicDao: MusicDao) {

    val allUserWithPlaylists = musicDao.getUsersWithPlaylists()

    suspend fun insertPlaylist(playList: PlayList) {
        musicDao.insertPlayList(playList)
    }
}