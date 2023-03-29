package com.whx.jetpacktest.widget.imagepick

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.databinding.ActivityRecyclerBinding
import com.whx.jetpacktest.widget.compress.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class PhotosActivity : BaseActivity() {
    private lateinit var binding: ActivityRecyclerBinding
    private var adapter: PhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        adapter = PhotoAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = 10
                outRect.top = 10
            }
        })

        if (checkPermissionsGranted(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            loadPics()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                233
            )
        }
    }

    private fun loadPics() {
        val disposable = ImageBucketLoader.getAllImagesObservable(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter?.addDatas(it)
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
    }

    private fun permissionGranted(
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        if (permissions.size == grantResults.size) {
            for (gr in grantResults) {
                if (gr != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 233 && permissionGranted(permissions, grantResults)) {
            loadPics()
        } else {
            Toast.makeText(this, "have no permission", Toast.LENGTH_SHORT).show()
        }
    }
}

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val datas: MutableList<String> = arrayListOf()

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.common_image, parent, false)
        return PhotoViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        if (position in datas.indices) {
            holder.bindView(datas[position])
        }
    }

    fun addDatas(data: List<String>) {
        datas.clear()
        datas.addAll(data)

        notifyDataSetChanged()
    }

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image)
        private val context = view.context

        fun bindView(url: String) {
            Glide.with(context)
                .load(url)
                .into(image)

            itemView.setOnClickListener {
//                Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
//                Glide.with(context)
//                    .load(url)
//                    .thumbnail(0.25f)
//                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
//                    .preload()

                Log.e("--------", "origin path: $url, file length: ${File(url).length() / 1024}kb")
                val files = Compressor.with(itemView.context).setTargetPath(getPath()).load(url).get()
                val compressFile = files.first()
                Log.e("--------", compressFile.absolutePath + ", file length: ${compressFile.length() / 1024}kb")

                context.startActivity(Intent(context, PreviewActivity::class.java).apply {
                    putExtra(PreviewActivity.PHOTO_PATH, compressFile.absolutePath)
                })
            }
        }

        private fun getPath(): String {
            val path = context.externalCacheDir?.absolutePath + "/Prac/image/"
            val file = File(path)
            return if (file.mkdirs()) {
                path
            } else path
        }
    }
}