package com.smartstudy.uskid.ui.base.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.MenuItem
import butterknife.OnClick
import com.smartstudy.uskid.R
import com.smartstudy.uskid.library.kotlinExt.setupActionBar
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author 赵珊珊
 * *
 * @date 2017/12/19
 */

abstract class BaseToolbarActivity : BaseActivity() {
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    @OnClick(R.id.iv_close)
    fun close() {
        finish()
    }
}