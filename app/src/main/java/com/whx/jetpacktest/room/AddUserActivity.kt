package com.whx.jetpacktest.room

import android.os.Bundle
import androidx.activity.viewModels
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.room.entity.Address
import com.whx.jetpacktest.room.entity.User
import com.whx.jetpacktest.room.repo.UserRepository
import com.whx.jetpacktest.room.vm.UserViewModel
import com.whx.jetpacktest.room.vm.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_room_add_user.*

class AddUserActivity : BaseActivity() {
    private val mDb by lazy { AppDatabase.getDatabase(this) }

    private val mUserViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(UserRepository(mDb.userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_user)

        add_user_btn.setOnClickListener {
            val name = et_user_name.text.toString()
            if (name.isNotBlank()) {
                val user = User(userName = name, age = et_user_age.text.toString().toIntOrNull() ?: 0, address = Address(et_user_address.text.toString(), "", 0))

                mUserViewModel.insert(user)
            }
        }
    }
}