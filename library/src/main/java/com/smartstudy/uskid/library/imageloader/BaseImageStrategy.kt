package com.smartstudy.uskid.library.imageloader

import android.content.Context

/**
 * @author 王宏杰
 * @date 2018/3/23
 */
interface BaseImageStrategy {
    /**
     * 加载图片
     */
    fun loadImageFromUri(context: Context, config: ImageLoaderConfig)

    /**
     * 加载圆形图片
     */
    fun loadCircleImageFromUrl(context: Context, config: ImageLoaderConfig)

    /**
     * 加载Gif图片
     */
    fun loadGifFromUri(context: Context, config: ImageLoaderConfig)

}