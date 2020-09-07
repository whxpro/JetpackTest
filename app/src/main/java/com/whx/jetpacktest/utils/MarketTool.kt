package com.whx.jetpacktest.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import java.lang.Exception
import java.util.*

object MarketTool {

    private const val TAG = "MarketTool"
    private const val schemaUrl = "market://details?id="

    fun startToMarket(context: Context, packageName: String = context.packageName, marketPackageName: String = ""): Boolean {
        val deviceBrand = Build.BRAND.toUpperCase(Locale.US)
        if (deviceBrand.isBlank()) {
            Log.e(TAG, "can't get device brand")
            return false
        }
        try {
            val mktPackageName =
                if (marketPackageName.isNotBlank()) marketPackageName else getMarketByBrand(
                    deviceBrand
                )
            if (mktPackageName.isNotBlank()) {
                openMarket(context, packageName, mktPackageName)
                return true
            } else {
                ThirdMarket.values().forEach {
                    if (isAppInstalled(it.marketPackageName, context)) {
                        openMarket(context, packageName, it.marketPackageName)
                        return true
                    }
                }
            }
        } catch (ae: ActivityNotFoundException) {
            Log.e(TAG, "要跳转的应用市场不存在!")
        } catch (e: Exception) {
            Log.e(TAG, "其他错误：${e.message}")
        }
        return false
    }

    private fun openMarket(context: Context, packageName: String, marketPackageName: String) {
        val uri = Uri.parse(schemaUrl + packageName)
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage(marketPackageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun getMarketByBrand(brandName: String): String {
        BrandMarket.values().forEach {
            if (it.brandName == brandName) {
                return it.marketPackageName
            }
        }
        return ""
    }

    private fun isAppInstalled(packageName: String, context: Context): Boolean {
        if (packageName.isBlank()) {
            return false
        }
        return try {
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            packageInfo != null
        } catch (e: Exception) {
            false
        }
    }

    private enum class BrandMarket(val brandName: String, val marketPackageName: String) {
        HUAWEI("HUAWEI", "com.huawei.appmarket"),
        HONOR("HONOR", "com.huawei.appmarket"),
        OPPO("OPPO", "com.oppo.market"),
        MEIZU("MEIZU", "com.meizu.mstore"),
        VIVO("VIVO", "com.bbk.appstore"),
        XIAOMI("XIAOMI", "com.xiaomi.market"),
        LENOVO("LENOVO", "com.lenovo.leos.appstore"),
        ZTE("ZTE", "zte.com.market"),
        XIAOLAJIAO("XIAOLAJIAO", "com.zhuoyi.market"),      // 小辣椒，对应卓易
        QH360("360", "com.qihoo.appstore"),
        NIUBIA("NUBIA", "com.nubia.neostore"),
        ONE_PLUS("ONEPLUS", "com.oppo.market"),
        MEITU("MEITU", "com.android.mobile.appstore"),
        SONY("SONY", "com.android.vending"),                // 索尼，这里写的是Google商店
        HTC("HTC", ""),                                     // 未知应用商店包名
        ZUK("ZUK", ""),
        GOOGLE("GOOGLE", "com.android.vending")
    }
    private enum class ThirdMarket(val marketPackageName: String) {
        BAIDU("com.baidu.appsearch"),                   // 百度市场
        TENCENT("com.tencent.android.qqdownloader"),    // 应用宝
        PPZHUSHOU("com.pp.assistant"),                  // pp助手
        ANZHI("com.goapk.market"),                      // 安智市场
        WANDOUJIA("com.wandoujia.phonenix2")            // 豌豆荚
    }
}