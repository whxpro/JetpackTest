package com.whx.jetpacktest.room.entity

import androidx.room.*

// 一对一
data class UserAndLibrary(
    @Embedded val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "userOwnerId"
    )
    val library: Library
)

// 一对多
data class UserWithPlaylists(
    @Embedded val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "userCreatorId"
    )
    val playlists: List<PlayList>
)

@Entity(primaryKeys = ["play_id", "song_id"])
data class PlaylistSongCrossRef(
    @ColumnInfo(name = "play_id") val playlistId: Long,
    @ColumnInfo(name = "song_id") val songId: Long
)

data class PlaylistWithSongs(
    @Embedded val playlist: PlayList,
    @Relation(
        parentColumn = "play_id",
        entityColumn = "song_id",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)

data class SongWithPlaylists(
    @Embedded val song: Song,
    @Relation(
        parentColumn = "song_id",
        entityColumn = "play_id",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val playlists: List<PlayList>
)

data class UserWithPlaylistsAndSongs (
    @Embedded val user: User,
    @Relation(
        entity = PlayList::class,
        parentColumn = "user_id",
        entityColumn = "userCreatorId"
    )
    val playlists: List<PlaylistWithSongs>
)