package com.whx.jetpacktest.datastore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import androidx.lifecycle.lifecycleScope
import com.tencent.mmkv.MMKV
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.NBApplication
import com.whx.jetpacktest.databinding.ActivityDatastoreTestBinding
import com.whx.jetpacktest.protobuf.BeautyProto
import com.whx.jetpacktest.viewmodel.Meizi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.system.measureTimeMillis

class DataStoreTestActivity : BaseActivity() {

    private val prefDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val protoDataStore: DataStore<BeautyProto.Beauty> by dataStore(
        fileName = "proto_setting",
        serializer = BeautyPrefSerial
    )

    private val EXAMPLE_INT = intPreferencesKey("example")

    private lateinit var binding: ActivityDatastoreTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatastoreTestBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        binding.getPrefBtn.setOnClickListener {
            /*lifecycleScope.launch {
                DataStoreUtil.getString("test_str").collect {
                    pref_datastore_edit.setText(it)
                }
            }*/
            binding.prefDatastoreEdit.setText(DataStoreUtil.getStringSync("test_str"))
        }

        binding.setPrefBtn.setOnClickListener {
            lifecycleScope.launch {
                val text = binding.prefDatastoreEdit.text.toString()

                try {
                    DataStoreUtil.putString("test_str", text)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.getProtoBtn.setOnClickListener {
            lifecycleScope.launch {
                Log.e("----------", "before delay current thread: ${Thread.currentThread().name}")
                delay(5000)
                readProtoData().map { beauty ->
                    beauty.name
                }.collect {
                    Log.e("----------", "current thread: ${Thread.currentThread().name}")
                    binding.protoDatastoreEdit.setText(it)
                }
            }
        }

        binding.setProtoBtn.setOnClickListener {
            lifecycleScope.launch {
                binding.protoDatastoreEdit.text.takeIf { it.isNotBlank() }?.let {
                    setProtoData(Meizi(name = it.toString()))
                }
            }
        }

        binding.testSp.setOnClickListener {
            testSp()
        }

        binding.testDataStore.setOnClickListener {
            testDs()
        }

        binding.testMmkv.setOnClickListener {
            testMMKV()
        }
    }

    private suspend fun readPreferences(): Flow<Preferences> {
        return prefDataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
    }

    private suspend fun writePreferences(data: Int?) {
        prefDataStore.edit { setting ->
            setting[EXAMPLE_INT] = data ?: 0
        }
    }

    private fun readProtoData() = protoDataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(BeautyProto.Beauty.getDefaultInstance())
        } else {
            throw it
        }
    }

    private suspend fun setProtoData(m: Meizi) {
        protoDataStore.updateData { beaury ->
            beaury.toBuilder().setName(m.name).build()
        }
    }

    object BeautyPrefSerial : Serializer<BeautyProto.Beauty> {          // 创建序列化器
        override val defaultValue: BeautyProto.Beauty
            get() = BeautyProto.Beauty.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): BeautyProto.Beauty {
            try {
                return BeautyProto.Beauty.parseFrom(input)
            } catch (e: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", e)
            }
        }

        override suspend fun writeTo(t: BeautyProto.Beauty, output: OutputStream) {
            t.writeTo(output)
        }
    }

    private val mSp = NBApplication.getAppContext().getSharedPreferences("sp_test", MODE_PRIVATE)
    private fun testSp() {
        val time = measureTimeMillis {
            mSp.edit().putString("hhh", "s").apply()
            repeat(1000) {
                val os = mSp.getString("hhh", "")
                mSp.edit().putString("hhh", "${os}_$it").apply()
            }
        }
        binding.testSpTime.text = "$time ms"
    }

    private val testKey = stringPreferencesKey("test")
    private fun testDs() {
        lifecycleScope.launch {
            val time = measureTimeMillis {
                repeat(1000) {
                    var v = ""
                    prefDataStore.data.first {
                        v = it[testKey] ?: ""
                        true
                    }
                    prefDataStore.edit { pref ->
                        pref[testKey] = "${v}_$it"
                    }
                }
            }
            binding.testDsTime.text = "$time ms"
        }
    }

    private val kv = MMKV.defaultMMKV()
    private fun testMMKV() {
        val time = measureTimeMillis {
            kv?.encode("hhh", "s")
            repeat(1000) {
                val os = kv?.decodeString("hhh", "")
                kv?.encode("hhh", "${os}_$it")
            }
        }
        binding.testMmkvTime.text = "$time ms"
    }
}