package com.whx.jetpacktest.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R

class ComposeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        Column {
            HelloScreen()
            Spacer(modifier = Modifier.height(6.dp))
            MessageCard(msg = Message("Android", "Jetpack Compose"))
            Spacer(modifier = Modifier.height(6.dp))
            Button(onClick = {
                val intent = Intent(this@ComposeActivity, ListActivity::class.java)
                startActivity(intent)
            }) {
                Text(text = "to list")
            }
        }
    }
}

// 状态下降、事件上升的这种模式称为“单向数据流”。
// 在这种情况下，状态会从 HelloScreen 下降为 HelloContent，事件会从 HelloContent 上升为 HelloScreen。
// 通过遵循单向数据流，您可以将在界面中显示状态的可组合项与应用中存储和更改状态的部分解耦。
@Composable
fun HelloScreen() {
    var name by rememberSaveable {      // rememberSaveable 可以在重组后保持状态。此外，rememberSaveable 也可以在重新创建 activity 和进程后保持状态。
        mutableStateOf("")
    }
    HelloContent(name = name, onNameChange = { name = it })
}
@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(value = name, onValueChange = onNameChange, label = { Text(text = "Name") })
    }
}

data class Message(val author: String, val body: String)

@Preview
@Composable
fun SimpleText() {
    Text(text = "Hello world")
}

@Composable
fun MessageCard(msg: Message) {
    Row {
        Image(
            painter = painterResource(id = R.mipmap.m2),
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Green, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = msg.author,
                color = MaterialTheme.colors.secondaryVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.body)
        }
    }
}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(msg = Message("Android", "Jetpack Compose"))
}