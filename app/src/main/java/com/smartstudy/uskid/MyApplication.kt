package com.smartstudy.uskid

import android.app.Application

/**
 * @author 赵珊珊
 * @date 2017/12/19
 */
class MyApplication : Application() {

    var mContext: MyApplication? = null

    fun getInstance(): MyApplication {
        return mContext!!
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}