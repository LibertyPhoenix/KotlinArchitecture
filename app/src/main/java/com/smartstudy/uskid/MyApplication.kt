package com.smartstudy.uskid

import android.support.multidex.MultiDexApplication

/**
 * @author 赵珊珊
 * @date 2017/12/19
 */
class MyApplication : MultiDexApplication() {

    var mContext: MyApplication? = null

    fun getInstance(): MyApplication {
        return mContext!!
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}