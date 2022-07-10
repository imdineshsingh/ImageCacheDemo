package com.example.imagecachedemo

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule


@GlideModule
class MyGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        //CHANGE SIZE OF DISK CACHE
        val diskCacheSizeBytes = /*20000*/1024 * 1024 * 100 // 100 MB
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes.toLong()))
        //CHANGE CACHE FOLDER
        //val diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(context, "cacheFolderTest", diskCacheSizeBytes.toLong())
        )
        //
    }
}
