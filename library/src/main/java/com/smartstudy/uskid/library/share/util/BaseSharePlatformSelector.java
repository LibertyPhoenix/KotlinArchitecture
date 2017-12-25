/*
 * Copyright (C) 2015 Bilibili <jungly.ik@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartstudy.uskid.library.share.util;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.widget.AdapterView;

import com.smartstudy.uskid.library.R;
import com.smartstudy.uskid.library.share.core.SocializeMedia;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Jungly
 * @email jungly.ik@gmail.com
 * @since 2016/1/4.
 */
public abstract class BaseSharePlatformSelector {

    private Activity mContext;
    private OnShareSelectorDismissListener mDismissListener;
    private AdapterView.OnItemClickListener mItemClickListener;

    public final static int All = -1;
    public final static int SINA = 0;
    public final static int WEIXIN = 1;
    public final static int WEIXIN_MONMENT = 2;
    public final static int QQ = 3;
    public final static int QZONE = 4;
    public final static int GENERIC = 5;
    public final static int COPY = 6;

    //指定该Annotation描述的对象，只能使用这两个常量
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({All,SINA, WEIXIN, WEIXIN_MONMENT, QQ, QZONE, GENERIC, COPY})
    public @interface Type {
    }

    private static ShareTarget[] shareTargets = {
            new ShareTarget(SocializeMedia.WEIXIN_MONMENT, R.string.bili_socialize_text_weixin_circle_key, R.drawable.bili_socialize_wxcircle),
            new ShareTarget(SocializeMedia.WEIXIN, R.string.bili_socialize_text_weixin_key, R.drawable.bili_socialize_wechat),
            new ShareTarget(SocializeMedia.QQ, R.string.bili_socialize_text_qq_key, R.drawable.bili_socialize_qq_on),
            new ShareTarget(SocializeMedia.QZONE, R.string.bili_socialize_text_qq_zone_key, R.drawable.bili_socialize_qzone_on),
//            new ShareTarget(SocializeMedia.SINA, R.string.bili_socialize_text_sina_key, R.drawable.bili_socialize_sina_on),
//            new ShareTarget(SocializeMedia.COPY, R.string.bili_socialize_text_copy_url, R.drawable.bili_socialize_copy_url),
//            new ShareTarget(SocializeMedia.GENERIC, R.string.bili_share_sdk_others, R.drawable.bili_socialize_sms_on)
    };

    public static ShareTarget[] getShareTargets() {
        return shareTargets;
    }

    public static ShareTarget getShareTarget(@Type int index) {
        return shareTargets[index];
    }
    public static @Type int getShareTargetIndex( int index) {
        switch (index){
            case SINA:
                return SINA;
            case WEIXIN:
                return WEIXIN;
            case WEIXIN_MONMENT:
                return WEIXIN_MONMENT;
            case QQ:
                return QQ;
            case QZONE:
                return QZONE;
            case GENERIC:
                return GENERIC;
            case COPY:
                return COPY;
            default: return All;
        }
    }

    public BaseSharePlatformSelector(Activity context,OnShareSelectorDismissListener dismissListener){
        this(context,dismissListener,null);
    }
    public BaseSharePlatformSelector(Activity context, OnShareSelectorDismissListener dismissListener, AdapterView.OnItemClickListener itemClickListener) {
        mContext = context;
        mDismissListener = dismissListener;
        mItemClickListener = itemClickListener;
    }

    public abstract void show();

    public abstract void dismiss();

    public void release() {
        mContext = null;
        mDismissListener = null;
        mItemClickListener = null;
    }


    public Activity getContext() {
        return mContext;
    }

    public AdapterView.OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public OnShareSelectorDismissListener getDismissListener() {
        return mDismissListener;
    }

    public static class ShareTarget {
        public int titleId;
        public int iconId;
        public SocializeMedia media;

        public ShareTarget(SocializeMedia media, int titleId, int iconId) {
            this.media = media;
            this.iconId = iconId;
            this.titleId = titleId;
        }
    }

    public interface OnShareSelectorDismissListener {
        void onDismiss();
    }

}
