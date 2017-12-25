package com.smartstudy.uskid.library.imageloader

import android.content.Context

/**
 * @author 赵珊珊
 * @date 2017/12/6
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