package com.smartstudy.uskid.library.imageloader

import android.content.Context
import com.bumptech.glide.load.resource.bitmap.CircleCrop

/**
 * @author 王宏杰
 * @date 2018/3/23
 */
class GlideImageLoaderStrategy : BaseImageStrategy {
    override fun loadImageFromUri(context: Context, config: ImageLoaderConfig) {
        GlideApp.with(context)
                .load(config.uri)
                .placeholder(config.defaultPlaceHolder)
                .error(config.errorPlaceHolder)
                .into(config.view)
    }

    override fun loadCircleImageFromUrl(context: Context, config: ImageLoaderConfig) {
        GlideApp.with(context)
                .load(config.uri)
                .transform(CircleCrop())
                .placeholder(config.defaultPlaceHolder)
                .error(config.errorPlaceHolder)
                .into(config.view)
    }

    override fun loadGifFromUri(context: Context, config: ImageLoaderConfig) {
        GlideApp.with(context)
                .asGif()
                .load(config.uri)
                .placeholder(config.defaultPlaceHolder)
                .error(config.errorPlaceHolder)
                .into(config.view)
    }
}