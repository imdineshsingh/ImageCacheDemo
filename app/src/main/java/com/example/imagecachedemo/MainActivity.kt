package com.example.imagecachedemo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File


data class Images(
    val url: List<String>
)

class MainActivity : AppCompatActivity() {
    private val imagesList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imagesList.add("https://i.picsum.photos/id/94/536/354.jpg?hmac=fwizvI2s7stn4NAlaH4D6nuFp2Lk-MrIVW2PF57OGPY")
        imagesList.add("https://i.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U")
        imagesList.add("https://picsum.photos/seed/picsum/200/300")
        imagesList.add("https://picsum.photos/200/300?grayscale")
        imagesList.add("https://picsum.photos/200/300/?blur")
        imagesList.add("https://picsum.photos/id/870/200/300?grayscale&blur=2")
        imagesList.add("https://picsum.photos/200/300/?blur")
        imagesList.add("https://i.picsum.photos/id/639/200/300.jpg?grayscale&hmac=KMuRoj7RSaUZP8ZlphEPi-ZQYzqk1EiQSq547TPV24c")
        imagesList.add("https://image.crictracker.com/wp-content/uploads/2022/03/MS-Dhoni-3.jpg")
        imagesList.add("https://images.hindustantimes.com/img/2022/07/08/550x309/dhoni-six-another-new-getty_1657267099301_1657267107916.jpg")
        val images = Images(imagesList)
        //PreferenceModel.put(images, "images")

        initControl()
        //findViewById<RecyclerView>(R.id.rvImages).adapter = MyAdapter(imagesList)


        checkCache()
        removeCache()


         usingGlide()
        // withPicasso()
    }


    private fun checkCache(){
        findViewById<Button>(R.id.btnDone).setOnClickListener {
            val CACHE_PATH = cacheDir.absolutePath + "/cacheFolderTest/";
            if (File(CACHE_PATH).exists()) {
                val files = File(CACHE_PATH).listFiles()
                Log.d("TAG", "onCreate: ${files.size}")
                files.forEach { file ->
                    Log.d("TAG", "onCreate FilePath: $file")
                }
            }else{
                Log.d("TAG", "onCreate Path not exist")
            }
        }
    }

    private fun removeCache() {
        findViewById<Button>(R.id.btnClearCache).setOnClickListener {
            GlideApp.get(this).clearMemory()
            AsyncTask.execute {
                GlideApp.get(this@MainActivity).clearDiskCache()
            }

        }
    }

    private fun usingGlide() {
        GlideApp.with(this)
            .load(/*GlideUrlCustomCacheKey(*/"https://thumbs.dreamstime.com/b/scenic-view-moraine-lake-mountain-range-sunset-landscape-canadian-rocky-mountains-49666349.jpg"/*)*/)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            .signature(ObjectKey("dinesh-"+System.currentTimeMillis()))
            //.apply(RequestOptions.signatureOf(ObjectKey("dinesh-"+System.currentTimeMillis())))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    runOnUiThread {
                        findViewById<ImageView>(R.id.ivImage).setImageDrawable(resource)
                        Log.d("TAG", "onResourceReady: $dataSource")
                        Log.d("TAG", "onResourceReady: $model")
                    }
                    return true
                }

            }).submit()



    }

    private fun withPicasso() {
        Picasso
            .get()
            .load("https://i.picsum.photos/id/94/536/354.jpg?hmac=fwizvI2s7stn4NAlaH4D6nuFp2Lk-MrIVW2PF57OGPY")
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    Log.d("TAG", "onBitmapLoaded: $from")
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

            })
    }

    private fun initControl() {
    }

    private class MyAdapter(val dataList: List<String>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var image: ImageView = itemView.findViewById(R.id.image)
            fun bind(imageUrl: String) {
                GlideApp.with(image.context)
                    .load(imageUrl)
                    .signature(ObjectKey("dinesh"+System.currentTimeMillis()))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {

                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            /*runOnUiThread {
                                findViewById<ImageView>(R.id.ivImage).setImageDrawable(resource)

                                Log.d("TAG", "onResourceReady: $isFirstResource")

                            }*/
                            return true
                        }

                    }).into(image)


            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val image = dataList[position]
            (holder as MyViewHolder).bind(image)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

    }
}
/*

FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/journal
2022-07-10 00:00:22.845 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/d3b48ff91a33142cf1714924ab7bc2b728587713b53f7b6d7e4af46df48eaec5.0
2022-07-10 00:00:22.845 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/d66ae3fb9663a23b8fc50c09026483928d9f13e408b0ba163173cd7c246ae6e9.0
2022-07-10 00:00:22.845 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/2d46a280654383f616097fd48e4be623a9f68a3fd32365ece7b969bdf0144110.0
2022-07-10 00:00:22.845 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/8ee6c010f81b068d7db18817b07bdb9e3e7a1d14c0934f3cd6e1cb89f827aff7.0
2022-07-10 00:00:22.845 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/05982167c1caf4772f7f0a45ab2828ac4b4870ec8f2a6561d9e5724de0358f21.0
2022-07-10 00:00:22.846 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/3330b580225bc8cafae27d0783eb5c725694bf773d9a4de984fc26a6b02a8b87.0
2022-07-10 00:00:22.846 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/cc4915fa58ff682994a98fa4ef58572d5e7b455ab8ea70155b5d9432a251c7af.0
2022-07-10 00:00:22.846 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/c9eaf13b4c78297b23a6f3d5b0c42e92c489e09123642da31f5b35b1b3914186.0
2022-07-10 00:00:22.846 13679-13679/com.example.imagecachedemo D/TAG: onCreate FilePath: /data/user/0/com.example.imagecachedemo/cache/cacheFolderTest/1e6cc5ec9598e7c478544db8131d83fb11315f6bd56f339c61b625fecb7770f0.0*/
