package com.whx.jetpacktest.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.whx.jetpacktest.BaseActivity

class ComposeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleText()
        }
    }

    @Preview
    @Composable
    fun SimpleText() {
        Text(text = "Hello world")
    }
}