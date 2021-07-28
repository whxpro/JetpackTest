package com.whx.jetpacktest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.whx.jetpacktest.NBApplication
import com.whx.jetpacktest.room.dao.MusicDao
import com.whx.jetpacktest.room.dao.UserDao
import com.whx.jetpacktest.room.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [User::class, Library::class, PlayList::class, Song::class, PlaylistSongCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun musicDao(): MusicDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val ins = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                    .addCallback(DatabaseCallback(NBApplication.getApp().appScope))
                    .build()
                INSTANCE = ins
                ins
            }
        }
    }

    private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val userDao = database.userDao()
                    userDao.deleteAll()

                    val mz = User( userName = "meizi", age = 18, avatar = "http://ww1.sinaimg.cn/large/0065oQSqly1fswhaqvnobj30sg14hka0.jpg")
                    userDao.insert(mz)
                }
            }
        }
    }
}