package com.whx.jetpacktest

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowInsetsControllerCompat

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setStatusBarColor()
    }

    protected open fun isLightTheme(): Boolean = false

    protected open fun setStatusBarColor() {
        val decor = window.decorView
        WindowInsetsControllerCompat(window, decor).isAppearanceLightStatusBars = isLightTheme()
    }

    fun checkPermissionsGranted(vararg perms: String): Boolean {
        for (perm in perms) {
            if (ActivityCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}