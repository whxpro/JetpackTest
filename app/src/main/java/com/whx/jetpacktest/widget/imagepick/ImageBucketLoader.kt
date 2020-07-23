package com.whx.jetpacktest.widget.imagepick

import android.provider.MediaStore
import android.text.TextUtils
import com.whx.jetpacktest.NBApplication
import io.reactivex.Maybe
import io.reactivex.MaybeOnSubscribe
import java.io.File

object ImageBucketLoader {
    private fun getPictureBucket(): MutableMap<String, MutableList<String>> {

        val imageBucketMap = mutableMapOf<String, MutableList<String>>()
        val allPhotos = mutableListOf<String>()
        imageBucketMap["All Photos"] = allPhotos

        val externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val cursor =
            NBApplication.getAppContext().contentResolver.query(externalContentUri, projection, null, null, null)
        if (cursor != null) {
            val bucketNameIndex = cursor.getColumnIndex(projection[0])
            val imagePathIndex = cursor.getColumnIndex(projection[1])

            while (cursor.moveToNext()) {
                val bucketName = cursor.getString(bucketNameIndex)
                val imagePath = cursor.getString(imagePathIndex)
                if (!TextUtils.isEmpty(bucketName) && !TextUtils.isEmpty(imagePath)) {
                    val imageFile = File(imagePath)
                    if (imageFile.exists()) {
                        if (!allPhotos.contains(imagePath)) {
                            allPhotos.add(imagePath)
                        }

                        if (imageBucketMap[bucketName] == null) {
                            imageBucketMap[bucketName] = mutableListOf()
                        }

                        if (!imageBucketMap[bucketName]!!.contains(imagePath)) {
                            imageBucketMap[bucketName]!!.add(imagePath)
                        }
                    }
                }
            }

            cursor.close()
        }

        return imageBucketMap
    }

    private fun getAllPictures(
        filterJpgAndPng: Boolean = false,
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): MutableList<String> {
        val allPhotos = mutableListOf<String>()

        val externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            NBApplication.getAppContext().contentResolver.query(externalContentUri, projection, selection,
                selectionArgs, MediaStore.Images.Media.DATE_MODIFIED + " desc")
        if (cursor != null) {
            val imagePathIndex = cursor.getColumnIndex(projection[0])
            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(imagePathIndex)
                if (!TextUtils.isEmpty(imagePath)) {
                    val imageFile = File(imagePath)
                    val filterJpgAndPngConditionPass = if (filterJpgAndPng) (
                            imagePath.endsWith("jpg", true) || imagePath.endsWith("png", true))
                    else true

                    if (imageFile.exists() && filterJpgAndPngConditionPass) {
                        if (!allPhotos.contains(imagePath)) {
                            allPhotos.add(imagePath)
                        }
                    }
                }
            }

            cursor.close()
        }

        return allPhotos
    }

    fun getImageBucketObservable(): Maybe<MutableList<ImageBucket>> {
        return Maybe.create(MaybeOnSubscribe<MutableList<ImageBucket>> {
            try {
                val bucketMap =
                    getPictureBucket()
                val bucketList = mutableListOf<ImageBucket>()

                //Map转List方便后续RecyclerView使用
                bucketMap.forEach {
                    val imageBucket =
                        ImageBucket(
                            it.key,
                            it.value
                        )
                    bucketList.add(imageBucket)
                }

                it.onSuccess(bucketList)
            } catch (e: Exception) {
                e.printStackTrace()
                it.onError(Throwable("Loading images error"))
            }
        })
    }

    fun getAllImagesObservable(
        filterJpgAndPng: Boolean,
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): Maybe<MutableList<String>> {
        return Maybe.create {
            try {
                val allImages =
                    getAllPictures(
                        filterJpgAndPng,
                        selection,
                        selectionArgs
                    )
                it.onSuccess(allImages)
                it.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                it.onError(Throwable("Loading images error"))
            }
        }
    }
}