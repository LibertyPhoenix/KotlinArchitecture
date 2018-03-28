package com.smartstudy.uskid

import android.support.multidex.MultiDexApplication

/**
 * @author 王宏杰
 * @date 2018/3/23
 */
class RiseApp : MultiDexApplication() {

    var mContext: RiseApp? = null

    fun getInstance(): RiseApp {
        return mContext!!
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}