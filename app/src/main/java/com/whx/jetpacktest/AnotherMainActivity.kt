package com.whx.jetpacktest

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

class AnotherMainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun Content() {
        FlowRow {
            Button(onClick = { }) {
                Text(text = "hh")
            }
        }
    }
}