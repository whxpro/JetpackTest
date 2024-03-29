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
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.lifecycleScope
import com.whx.jetpacktest.compose.ComposeActivity
import com.whx.jetpacktest.coroutines.CoroTestActivity
import com.whx.jetpacktest.databinding.ActivityMainBinding
import com.whx.jetpacktest.databinding.SimpleDatabindingActivity
import com.whx.jetpacktest.datastore.DataStoreTestActivity
import com.whx.jetpacktest.mvi.MviActivity
import com.whx.jetpacktest.nav.NavHostActivity
import com.whx.jetpacktest.room.RoomTestActivity
import com.whx.jetpacktest.rx.RxTestActivity
import com.whx.jetpacktest.utils.MarketTool
import com.whx.jetpacktest.viewmodel.ViewModelActivity
import com.whx.jetpacktest.widget.RemoteViewTest
import com.whx.jetpacktest.widget.WidgetTestActivity
import com.whx.jetpacktest.widget.compress.Compressor
import com.whx.jetpacktest.widget.coord.CoordTestActivity
import com.whx.jetpacktest.widget.cycle_viewpager.ViewpagerActivity
import com.whx.jetpacktest.widget.lottie.LottieTestActivity
import com.whx.jetpacktest.widget.refresh.TestRefreshActivity
import com.whx.jetpacktest.widget.statusbar.ActivityBlue
import com.whx.jetpacktest.workmanager.WorkTestActivity
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toMvvm.setOnClickListener {
            startActivity(Intent(this, SimpleDatabindingActivity::class.java))
        }

        binding.toRx.setOnClickListener {
            startActivity(Intent(this, RxTestActivity::class.java))
        }

        binding.toPhotos.setOnClickListener {
//            startActivity(Intent(this, PhotosActivity::class.java))
            val dir = File("/storage/emulated/0/DCIM/Camera/Prac/Pictures")
            lifecycleScope.launch {
                Compressor.with(this@MainActivity).load(dir.listFiles().toList())
                    .setTargetPath("/storage/emulated/0/DCIM/Camera/Prac/image").get()
                Toast.makeText(this@MainActivity, "compress success", Toast.LENGTH_SHORT).show()
            }
        }

        binding.toVm.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }

        binding.toViewpager.setOnClickListener {
            startActivity(Intent(this, ViewpagerActivity::class.java))
        }

        binding.addShortcut.setOnClickListener {
            mHandler.postDelayed({
                Toast.makeText(this, "what the fuck", Toast.LENGTH_SHORT).show()
            }, 5000)
            add("hhh", "测试")
        }

        binding.sendNotification.setOnClickListener {
            RemoteViewTest().initNotification(this)
        }

        binding.toRefresh.setOnClickListener {
            startActivity(Intent(this, TestRefreshActivity::class.java))
        }

        binding.navigation.setOnClickListener {
            startActivity(Intent(this, NavHostActivity::class.java))
        }

        binding.toCoroutine.setOnClickListener {
            startActivity(Intent(this, CoroTestActivity::class.java))
        }

        binding.toWidget.setOnClickListener {
            startActivity(Intent(this, WidgetTestActivity::class.java))
        }

        binding.toMarket.setOnClickListener {
            MarketTool.startToMarket(this)
        }

        binding.toStatusbar.setOnClickListener {
            startActivity(Intent(this, ActivityBlue::class.java))
        }

        binding.toLottie.setOnClickListener {
            startActivity(Intent(this, LottieTestActivity::class.java))
        }

        binding.toWork.setOnClickListener {
            startActivity(Intent(this, WorkTestActivity::class.java))
        }

        binding.toDataStore.setOnClickListener {
            startActivity(Intent(this, DataStoreTestActivity::class.java))
        }

        binding.toCoord.setOnClickListener {
            startActivity(Intent(this, CoordTestActivity::class.java))
        }

        binding.toCompose.setOnClickListener {
            startActivity(Intent(this, ComposeActivity::class.java))
        }

        binding.toRoom.setOnClickListener {
            startActivity(Intent(this, RoomTestActivity::class.java))
        }

        binding.toTemp.setOnClickListener {
            startActivity(Intent(this, TmpActivity::class.java))
        }

        binding.toMvi.setOnClickListener {
            startActivity(Intent(this, MviActivity::class.java))
        }

        /*to_widget.viewTreeObserver.addOnPreDrawListener {
            guide_view?.let {
                it.setClipCenter(to_widget.x + to_widget.width / 2, to_widget.y + to_widget.height / 2)
                it.setArea(to_widget.width + 30f, to_widget.height + 30f)
                it.setCornerRadius(20f)
                it.update()
            }
            true
        }*/
    }

    override fun onPostResume() {
        super.onPostResume()
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
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
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
            intent: Intent
        ) {
            Toast.makeText(NBApplication.getAppContext(), "success", Toast.LENGTH_SHORT).show()
        }
    }
}
