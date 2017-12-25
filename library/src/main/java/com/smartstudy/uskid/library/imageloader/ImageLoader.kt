package com.smartstudy.uskid.library.imageloader

import android.content.Context

/**
 * @author 赵珊珊
 * @date 2017/12/6
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
