package com.whx.jetpacktest.room.entity

import androidx.room.*
import java.util.*

data class Address(val city: String?, val street: String?, @ColumnInfo(name = "post_code") val postCode: Int)

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") var userId: Long = 0,
    @ColumnInfo(name = "user_name") var userName: String,
    var age: Int,
    var birthday: Date? = null,
    var avatar: String? = null,
    @Embedded var address: Address? = null
)

@Entity
data class Library(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "lib_id") var libraryId: Long = 0,
    val userOwnerId: Long
)

@Entity(
    tableName = "play_list",
    indices = [Index("play_id")]
)
data class PlayList(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "play_id") var playlistId: Long = 0,
    val userCreatorId: Long,
    var playlistName: String
)

@Entity(
    tableName = "song",
    indices = [Index("song_id")]
)
data class Song(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "song_id") var songId: Long = 0,
    val songName: String,
    val artist: String
)