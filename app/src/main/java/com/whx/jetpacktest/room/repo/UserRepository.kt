package com.whx.jetpacktest.room.repo

import androidx.annotation.WorkerThread
import com.whx.jetpacktest.room.dao.UserDao
import com.whx.jetpacktest.room.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsersAsc()

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}