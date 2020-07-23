package com.whx.jetpacktest.widget.imagepick

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recycler.*

class PhotosActivity : BaseActivity() {

    private var adapter: PhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        adapter = PhotoAdapter()
        recycler_view.layoutManager = GridLayoutManager(this, 3)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(object : RecyclerView.ItemDecoration() {
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

        if (checkPermissionsGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            loadPics()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 233)
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

    private fun permissionGranted(permissions: Array<out String>,
                                  grantResults: IntArray): Boolean {
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

                context.startActivity(Intent(context, PreviewActivity::class.java).apply {
                    putExtra(PreviewActivity.PHOTO_PATH, url)
                })
            }
        }
    }
}