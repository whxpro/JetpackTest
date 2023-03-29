package com.whx.jetpacktest.widget.imagepick

import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.databinding.CommonImageBinding

class PreviewActivity : BaseActivity() {

    companion object {
        const val PHOTO_PATH = "photo_url"
    }

    private lateinit var binding: CommonImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CommonImageBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
//        val layoutParams = image.layoutParams
//        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//        image.layoutParams = layoutParams
//        image.scaleType = ImageView.ScaleType.FIT_CENTER

        val url = intent.getStringExtra(PHOTO_PATH)

//        Glide.with(this)
//            .load(url)
//            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
//            .into(image)

//        Picasso.get()
//            .load(File(url))
//            .into(image)

//        val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://$url"))
//            .setLocalThumbnailPreviewsEnabled(true)
//            .build()
//
//        val controller = Fresco.newDraweeControllerBuilder()
//            .setImageRequest(request)
//            .setOldController(drawee_image.controller)
//            .build()
//
//        drawee_image.controller = controller

        Glide.with(this)
            .load(url)
            .thumbnail(0.18f)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.image)

        binding.previewContainer.setOnClickListener {
            onBackPressed()
        }
    }
}