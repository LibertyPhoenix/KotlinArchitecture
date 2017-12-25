package com.smartstudy.uskid.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.smartstudy.uskid.R
import com.smartstudy.uskid.ui.fragment.HomeFragment
import java.util.*
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView


/**
 * @author 赵珊珊
 * @date 2017/12/19
 */
class HomePagerAdapter(fm: FragmentManager?, val context: Context) : FragmentPagerAdapter(fm) {
    var titles = Arrays.asList("发现", "课前", "课后", "我")
    var icons = Arrays.asList(R.drawable.tab_coupon_selector, R.drawable.tab_search_selector, R.drawable.tab_nine_selector, R.drawable.tab_usercenter_selector)

    override fun getItem(position: Int): Fragment {
        return HomeFragment()
    }

    override fun getCount(): Int {
        return titles.size
    }

    fun getTabView(position: Int): View {
        val v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val tv = v.findViewById<TextView>(R.id.title)
        tv.text = titles[position]
        val img = v.findViewById<ImageView>(R.id.icon)
        img.setImageResource(icons[position])
        return v
    }

}