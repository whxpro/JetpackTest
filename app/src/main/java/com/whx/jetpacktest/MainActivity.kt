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
import android.view.View
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.whx.jetpacktest.compose.ComposeActivity
import com.whx.jetpacktest.coroutines.CoroTestActivity
import com.whx.jetpacktest.databinding.SimpleDatabindingActivity
import com.whx.jetpacktest.datastore.DataStoreTestActivity
import com.whx.jetpacktest.nav.NavHostActivity
import com.whx.jetpacktest.rx.RxTestActivity
import com.whx.jetpacktest.utils.MarketTool
import com.whx.jetpacktest.widget.imagepick.PhotosActivity
import com.whx.jetpacktest.widget.RemoteViewTest
import com.whx.jetpacktest.widget.refresh.TestRefreshActivity
import com.whx.jetpacktest.widget.cycle_viewpager.ViewpagerActivity
import com.whx.jetpacktest.viewmodel.ViewModelActivity
import com.whx.jetpacktest.widget.WidgetTestActivity
import com.whx.jetpacktest.widget.coord.CoordTestActivity
import com.whx.jetpacktest.widget.lottie.LottieTestActivity
import com.whx.jetpacktest.widget.statusbar.ActivityBlue
import com.whx.jetpacktest.workmanager.WorkTestActivity
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

        send_notification.setOnClickListener {
            RemoteViewTest().initNotification(this)
        }

        to_refresh.setOnClickListener {
            startActivity(Intent(this, TestRefreshActivity::class.java))
        }

        navigation.setOnClickListener {
            startActivity(Intent(this, NavHostActivity::class.java))
        }

        to_coroutine.setOnClickListener {
            startActivity(Intent(this, CoroTestActivity::class.java))
        }

        to_widget.setOnClickListener {
            startActivity(Intent(this, WidgetTestActivity::class.java))
        }

        to_market.setOnClickListener {
            MarketTool.startToMarket(this)
        }

        to_statusbar.setOnClickListener {
            startActivity(Intent(this, ActivityBlue::class.java))
        }

        to_lottie.setOnClickListener {
            startActivity(Intent(this, LottieTestActivity::class.java))
        }

        toWork.setOnClickListener {
            startActivity(Intent(this, WorkTestActivity::class.java))
        }

        toDataStore.setOnClickListener {
            startActivity(Intent(this, DataStoreTestActivity::class.java))
        }

        toCoord.setOnClickListener {
            startActivity(Intent(this, CoordTestActivity::class.java))
        }

        toCompose.setOnClickListener {
            startActivity(Intent(this, ComposeActivity::class.java))
        }

        to_widget.viewTreeObserver.addOnPreDrawListener {
            guide_view?.let {
                it.setClipCenter(to_widget.x + to_widget.width / 2, to_widget.y + to_widget.height / 2)
                it.setArea(to_widget.width + 30f, to_widget.height + 30f)
                it.setCornerRadius(20f)
                it.update()
            }
            true
        }
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
