package com.whx.jetpacktest.room

import android.os.Bundle
import androidx.activity.viewModels
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.room.entity.PlayList
import com.whx.jetpacktest.room.repo.MusicRepository
import com.whx.jetpacktest.room.vm.MusicViewModel
import com.whx.jetpacktest.room.vm.MusicViewModelFactory
import kotlinx.android.synthetic.main.activity_room_add_music.*

class AddMusicActivity : BaseActivity() {
    private val mDb by lazy { AppDatabase.getDatabase(this) }

    private var mUserId: Long = -1

    companion object {
        const val KEY_USER_ID = "user_id"
    }

    private val mMusicViewModel by viewModels<MusicViewModel> {
        MusicViewModelFactory(MusicRepository(mDb.musicDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_music)

        mUserId = intent.getLongExtra(KEY_USER_ID, -1)

        add_music_btn.setOnClickListener {
            et_music_name.text.toString().takeIf { it.isNotBlank() }?.let { mMusicViewModel.insertSong(PlayList(userCreatorId = mUserId, playlistName = it)) }
        }
    }
}