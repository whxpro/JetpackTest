package com.whx.jetpacktest.databinding

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.*
import com.whx.jetpacktest.R
import java.util.*

class SimpleDatabindingActivity : AppCompatActivity() {

    private lateinit var activityDatabinding: ActivitySimpleDatabindingBinding
    private lateinit var goods: ObservableGoods
    private lateinit var map: ObservableMap<String, String>
    private lateinit var list: ObservableList<String>
    private var user: User? = null

    private var viewStub: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_simple_databinding)

        user = User("meizi", "123456", "http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg")
        activityDatabinding.userInfo = user

        goods = ObservableGoods("meizi", "http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg", 2.33f)
        activityDatabinding.observableGoods = goods

        map = ObservableArrayMap()
        list = ObservableArrayList()

        map["name"] = "whx"
        map["age"] = "18"
        activityDatabinding.map = map

        list.add("hhh")
        list.add("233")
        activityDatabinding.list = list

        activityDatabinding.index = 0
        activityDatabinding.key = "name"
        activityDatabinding.eventHanlder = EventHandler()

        activityDatabinding.observableGoods = goods

//        goods.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
//            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//                when (propertyId) {
//                    BR.name -> Toast.makeText(this@SimpleDatabindingActivity, "name ${sender.toString()}", Toast.LENGTH_SHORT).show()
//                    BR.detail -> Toast.makeText(this@SimpleDatabindingActivity, "detail ${sender.toString()}", Toast.LENGTH_SHORT).show()
//                    BR._all -> Toast.makeText(this@SimpleDatabindingActivity, "all ${sender.toString()}", Toast.LENGTH_SHORT).show()
//                    else -> Toast.makeText(this@SimpleDatabindingActivity, "unknown", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

        activityDatabinding.imageUrl = "http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg"

        activityDatabinding.paddingBig = true

        viewStub = activityDatabinding.viewStub.viewStub?.inflate()
        activityDatabinding.viewStub.setOnInflateListener { stub, inflated ->
            Toast.makeText(this, stub.toString() + inflated.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    inner class EventHandler {
        fun changeName() {
            map["name"] = "tsm" + Random().nextFloat()*100
//            goods.name.set("mm" + Random().nextFloat()*100)
//            goods.setPrice(Random().nextFloat()*100)
        }
        fun changeDetail() {
            list.add("666")
            activityDatabinding.index = Random().nextInt() % list.size
//            goods.detail.set("hhhhhhhhhhhhhhhhhh ${Random().nextFloat()*100}")
//            goods.setPrice(Random().nextFloat()*100)
        }

        fun afterTextChange(s: Editable) {
            Toast.makeText(this@SimpleDatabindingActivity, s, Toast.LENGTH_SHORT).show()
        }

        fun showViewStub() {
            if (viewStub?.visibility != View.VISIBLE) {
                viewStub?.visibility = View.VISIBLE
            } else {
                viewStub?.visibility = View.GONE
            }
        }

        fun login() {
            Toast.makeText(this@SimpleDatabindingActivity, "user: {${user?.name}, ${user?.passwd}}", Toast.LENGTH_SHORT).show()
        }
    }
}