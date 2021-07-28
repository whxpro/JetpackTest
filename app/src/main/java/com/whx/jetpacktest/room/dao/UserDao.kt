package com.whx.jetpacktest.room.dao

import androidx.room.*
import com.whx.jetpacktest.room.entity.User
import com.whx.jetpacktest.room.entity.UserAndLibrary
import com.whx.jetpacktest.room.entity.UserWithPlaylists
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table ORDER BY user_name ASC")
    fun getAllUsersAsc(): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE age > :minAge")
    suspend fun getUsersOlderThan(minAge: Int): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteUser(user: User): Int

    @Transaction
    @Query("SELECT * FROM user_table")
    suspend fun getUsersAndLibraries(): List<UserAndLibrary>

    @Query("SELECT * FROM user_table WHERE birthday BETWEEN :from AND :to")
    suspend fun findUsersBornBetween(from: Date, to: Date): List<User>
}