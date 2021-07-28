package com.whx.jetpacktest.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.whx.jetpacktest.room.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Transaction
    @Query("SELECT * FROM user_table")
    fun getUsersWithPlaylists(): Flow<List<UserWithPlaylists>>

    @Transaction
    @Query("SELECT * FROM play_list")
    suspend fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Transaction
    @Query("SELECT * FROM Song")
    suspend fun getSongsWithPlaylists(): List<SongWithPlaylists>

    @Transaction
    @Query("SELECT * FROM user_table")
    suspend fun getUsersWithPlaylistsAndSongs(): List<UserWithPlaylistsAndSongs>

    @Insert
    suspend fun insertPlayList(playList: PlayList)

    @Insert
    suspend fun insertSong(song: Song)
}