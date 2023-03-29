package com.whx.jetpacktest.room

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.databinding.ActivityRoomTestBinding
import com.whx.jetpacktest.room.entity.User
import com.whx.jetpacktest.room.entity.UserWithPlaylists
import com.whx.jetpacktest.room.repo.MusicRepository
import com.whx.jetpacktest.room.repo.UserRepository
import com.whx.jetpacktest.room.vm.MusicViewModel
import com.whx.jetpacktest.room.vm.MusicViewModelFactory
import com.whx.jetpacktest.room.vm.UserViewModel
import com.whx.jetpacktest.room.vm.UserViewModelFactory

class RoomTestActivity : BaseActivity(), ItemClickListener {
    private val mDb by lazy { AppDatabase.getDatabase(this) }

    private val mUserViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(UserRepository(mDb.userDao()))
    }

    private val mMusicViewModel by viewModels<MusicViewModel> {
        MusicViewModelFactory(MusicRepository(mDb.musicDao()))
    }

    private val mAdapter by lazy { UserAdapter(this) }

    private lateinit var binding: ActivityRoomTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomTestBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initRecyclerView()

        mMusicViewModel.allUserWithPlaylists.observe(this) { users ->
            users?.let {
                mAdapter.submitList(it)
                if (it.size > 2) {
                    delayDeleteLast(it.last().user)
                }
            }
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.user_list)
        recyclerView.adapter = mAdapter
    }

    private fun delayDeleteLast(user: User) {
        Handler(Looper.getMainLooper()).postDelayed({
            mUserViewModel.deleteUser(user)
        }, 3000)
    }

    override fun onItemClick(item: UserWithPlaylists, position: Int) {
        startActivity(Intent(this, AddMusicActivity::class.java).apply {
            putExtra(AddMusicActivity.KEY_USER_ID, item.user.userId)
        })
    }
}