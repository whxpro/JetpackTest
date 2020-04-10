package com.whx.jetpacktest

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.whx.jetpacktest.databinding.SimpleDatabindingActivity
import com.whx.jetpacktest.rx.RxTestActivity
import com.whx.jetpacktest.tmp.PhotosActivity
import com.whx.jetpacktest.tmp.cycle_viewpager.ViewpagerActivity
import com.whx.jetpacktest.viewmodel.ViewModelActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        to_mvvm.setOnClickListener {
            startActivity(Intent(this, SimpleDatabindingActivity::class.java))
        }

        to_rx.setOnClickListener {
            startActivity(Intent(this, RxTestActivity::class.java))
        }

        to_photos.setOnClickListener {
            startActivity(Intent(this, PhotosActivity::class.java))
        }

        to_vm.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }

        to_viewpager.setOnClickListener {
            startActivity(Intent(this, ViewpagerActivity::class.java))
        }

        add_shortcut.setOnClickListener {
            mHandler.postDelayed({
                Toast.makeText(this, "what the fuck", Toast.LENGTH_SHORT).show()
            }, 5000)
            add("hhh", "测试")
        }
    }

    override fun onPause() {
        Log.e("-------", "pause")
        super.onPause()
    }

    fun add(id: String, label: String) {
        val context = NBApplication.getAppContext()
        val supported: Boolean = ShortcutManagerCompat.isRequestPinShortcutSupported(context)

        if (supported) {
            val shortcutIntent = Intent()
            shortcutIntent.action = Intent.ACTION_VIEW
            // 此处由于组件化原因使用了硬编码的全限定名，如果MainActivity 修改名字或者位置可能存在风险

            shortcutIntent.component = ComponentName(context, "com.yy.biu.biz.main.MainActivity")
            shortcutIntent.putExtra("start_record_fromShortcut", true)

            val info: ShortcutInfoCompat = ShortcutInfoCompat.Builder(context, id)
                .setIcon(IconCompat.createWithResource(context, R.mipmap.ic_add_record_shortcut))
                .setShortLabel(label)
                .setIntent(shortcutIntent)
                .build()
            val callbackIntent = PendingIntent.getBroadcast(
                context, 233,
                Intent(context, CallbackReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            ShortcutManagerCompat.requestPinShortcut(
                context,
                info,
                callbackIntent.intentSender
            )
        } else {

            //            addShortcutLowOs(context);
        }
    }
    class CallbackReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent) {
            Toast.makeText(NBApplication.getAppContext(), "success", Toast.LENGTH_SHORT).show()
        }
    }
}