package com.smartstudy.uskid.library.imageloader

import android.content.Context

/**
 * @author 王宏杰
 * @date 2018/3/23
 *
 * 图片加载的工具类
 */

class ImageLoader(internal var context: Context, internal var config: ImageLoaderConfig) {
    var imageLoaderStrategy: BaseImageStrategy = GlideImageLoaderStrategy()
        set(value) {
            imageLoaderStrategy = value
        }

    fun loadImage() {
        imageLoaderStrategy.loadImageFromUri(context, config)
    }

    fun loadGifImage() {
        imageLoaderStrategy.loadGifFromUri(context, config)
    }

    fun loadCircleImage() {
        imageLoaderStrategy.loadCircleImageFromUrl(context, config)
    }
}
