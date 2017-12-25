package com.smartstudy.uskid.library.imageloader

import android.widget.ImageView

/**
 * @author 赵珊珊
 * *
 * @date 2017/12/6
 */

class ImageLoaderConfig(builder: Builder) {
    val view: ImageView
    val uri: String
    val errorPlaceHolder: Int
    val defaultPlaceHolder: Int

    init {
        this.uri = builder.uri!!
        this.errorPlaceHolder = builder.errorPlaceHolder
        this.defaultPlaceHolder = builder.defaultPlaceHolder
        this.view = builder.view!!
    }

    class Builder {
        var uri: String? = null
        var errorPlaceHolder: Int = 0
        var defaultPlaceHolder: Int = 0
        var view: ImageView? = null

        init {
            this.uri = ""
            this.errorPlaceHolder = 0
            this.defaultPlaceHolder = 0
            this.view = null
        }

        fun url(uri: String): Builder {
            this.uri = uri
            return this
        }

        fun errorPlaceHolder(errorPlaceHolder: Int): Builder {
            this.errorPlaceHolder = errorPlaceHolder
            return this
        }

        fun defaultPlaceHolder(defaultPlaceHolder: Int): Builder {
            this.defaultPlaceHolder = defaultPlaceHolder
            return this
        }

        fun imgView(view: ImageView): Builder {
            this.view = view
            return this
        }


        fun bulid(): ImageLoaderConfig {
            return ImageLoaderConfig(this)
        }
    }
}
