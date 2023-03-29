package com.whx.jetpacktest.room

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.databinding.ActivityRoomAddMusicBinding
import com.whx.jetpacktest.room.entity.PlayList
import com.whx.jetpacktest.room.repo.MusicRepository
import com.whx.jetpacktest.room.vm.MusicViewModel
import com.whx.jetpacktest.room.vm.MusicViewModelFactory

class AddMusicActivity : BaseActivity() {
    private lateinit var binding: ActivityRoomAddMusicBinding
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
        binding = ActivityRoomAddMusicBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        mUserId = intent.getLongExtra(KEY_USER_ID, -1)

        binding.addMusicBtn.setOnClickListener {
            binding.etMusicName.text.toString().takeIf { it.isNotBlank() }
                ?.let { mMusicViewModel.insertSong(PlayList(userCreatorId = mUserId, playlistName = it)) }
        }
    }
}