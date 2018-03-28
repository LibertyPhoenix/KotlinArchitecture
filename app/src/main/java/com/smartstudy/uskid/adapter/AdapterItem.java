package com.smartstudy.uskid.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * @author 王宏杰
 * @date 2018/3/23
 */

public interface AdapterItem<T> {
  @LayoutRes int getLayoutResId(int viewType);//获取布局文件的id

  void bindViews(final View root);//进行findViewById的操作

  void bindData(Context context, int position, T t, int viewType);//绑定数据
}