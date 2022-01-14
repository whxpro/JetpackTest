package com.whx.jetpacktest.compose

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.android.parcel.Parcelize

// 存储状态的方式
// 添加到 Bundle 的所有数据类型都会自动保存。如果要保存无法添加到 Bundle 的内容，您有以下几种选择。

// 1. Parcelize
@Parcelize
data class CityP(val name: String, val country: String) : Parcelable
@Composable
fun CityScreenP() {
    val selectedCity = rememberSaveable {
        mutableStateOf(CityP("Beijing", "China"))
    }
}

// 2. MapSaver
data class CityM(val name: String, val country: String)
val CitySaver = run {
    val nameKey = "Name"
    val countryKey = "Country"
    mapSaver(
        save = { mapOf(nameKey to it.name, countryKey to it.country) },
        restore = { CityM(it[nameKey] as String, it[countryKey] as String) }
    )
}
@Composable
fun CityScreenM() {
    val selectedCity = rememberSaveable(stateSaver = CitySaver) {
        mutableStateOf(CityM("Beijing", "China"))
    }
}

// 3. ListSaver
data class CityL(val name: String, val country: String)
val CitySaverL = listSaver<CityL, Any>(
    save = { listOf(it.name, it.country) },
    restore = { CityL(it[0] as String, it[1] as String) }
)
@Composable
fun CityScreenL() {
    val selectedCity = rememberSaveable(stateSaver = CitySaverL) {
        mutableStateOf(CityL("Beijing", "China"))
    }
}