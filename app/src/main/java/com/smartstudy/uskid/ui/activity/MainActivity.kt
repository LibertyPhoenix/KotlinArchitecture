package com.smartstudy.uskid.ui.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.KeyEvent
import android.widget.Toast
import com.smartstudy.uskid.R
import com.smartstudy.uskid.adapter.HomePagerAdapter
import com.smartstudy.uskid.ui.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    var mExitTime: Long = 0
    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    fun initView() {
        val adapter = HomePagerAdapter(supportFragmentManager, this)
        with(view_pager) {
            setAdapter(adapter)
            offscreenPageLimit = 4
        }
        with(tablayout) {
            setupWithViewPager(view_pager)
            clearOnTabSelectedListeners()
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    view_pager.currentItem = tab.position
                }

            })
        }
        for (i in 0..tablayout.tabCount - 1) {
            val tab = tablayout.getTabAt(i)
            if (tab != null) {
                tab!!.customView = adapter.getTabView(i)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
