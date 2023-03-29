package com.whx.jetpacktest.room

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.databinding.ActivityRoomAddUserBinding
import com.whx.jetpacktest.room.entity.Address
import com.whx.jetpacktest.room.entity.User
import com.whx.jetpacktest.room.repo.UserRepository
import com.whx.jetpacktest.room.vm.UserViewModel
import com.whx.jetpacktest.room.vm.UserViewModelFactory

class AddUserActivity : BaseActivity() {
    private lateinit var binding: ActivityRoomAddUserBinding

    private val mDb by lazy { AppDatabase.getDatabase(this) }

    private val mUserViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(UserRepository(mDb.userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomAddUserBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.addUserBtn.setOnClickListener {
            val name = binding.etUserName.text.toString()
            if (name.isNotBlank()) {
                val user = User(
                    userName = name,
                    age = binding.etUserAge.text.toString().toIntOrNull() ?: 0,
                    address = Address(binding.etUserAddress.text.toString(), "", 0)
                )

                mUserViewModel.insert(user)
            }
        }
    }
}