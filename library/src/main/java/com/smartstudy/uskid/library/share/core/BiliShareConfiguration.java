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

package com.smartstudy.uskid.library.share.core;

import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.smartstudy.uskid.library.R;
import com.smartstudy.uskid.library.share.core.handler.sina.SinaHandler;
import com.smartstudy.uskid.library.share.download.DefaultImageDownloader;
import com.smartstudy.uskid.library.share.download.IImageDownloader;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Jungly
 * @email jungly.ik@gmail.com
 * @since 2015/10/8
 */
public class BiliShareConfiguration implements Parcelable {

    String mImageCachePath;
    final int mDefaultShareImage;

    private String mSinaRedirectUrl;
    private String mSinaScope;

    final IImageDownloader mImageDownloader;
    final Executor mTaskExecutor;

    private BiliShareConfiguration(Builder builder) {
        mImageCachePath = builder.mImageCachePath;
        mDefaultShareImage = builder.mDefaultShareImage;
        mSinaRedirectUrl = builder.mSinaRedirectUrl;
        mSinaScope = builder.mSinaScope;
        mImageDownloader = builder.mImageLoader;
        mTaskExecutor = Executors.newCachedThreadPool();
    }

    public String getImageCachePath(Context context) {
        if (TextUtils.isEmpty(mImageCachePath)) {
            mImageCachePath = Builder.getDefaultImageCacheFile(context.getApplicationContext());
        }
        return mImageCachePath;
    }

    public int getDefaultShareImage() {
        return mDefaultShareImage;
    }

    public String getSinaRedirectUrl() {
        return mSinaRedirectUrl;
    }

    public String getSinaScope() {
        return mSinaScope;
    }

    public IImageDownloader getImageDownloader() {
        return mImageDownloader;
    }

    public Executor getTaskExecutor() {
        return mTaskExecutor;
    }

    public static class Builder {
        public static final String IMAGE_CACHE_FILE_NAME = "shareImage";

        private Context mContext;
        private String mImageCachePath;
        private int mDefaultShareImage = -1;

        private String mSinaRedirectUrl;
        private String mSinaScope;

        private IImageDownloader mImageLoader;

        public Builder(Context context) {
            mContext = context.getApplicationContext();
        }

        public Builder imageCachePath(String path) {
            mImageCachePath = path;
            return this;
        }

        public Builder defaultShareImage(int defaultImage) {
            mDefaultShareImage = defaultImage;
            return this;
        }

        public Builder sina(String redirectUrl, String mScope) {
            mSinaRedirectUrl = redirectUrl;
            mSinaScope = mScope;
            return this;
        }

        public Builder imageDownloader(IImageDownloader loader) {
            mImageLoader = loader;
            return this;
        }

        public BiliShareConfiguration build() {
            checkFields();
            return new BiliShareConfiguration(this);
        }

        private void checkFields() {
            File imageCacheFile = null;
            if (!TextUtils.isEmpty(mImageCachePath)) {
                imageCacheFile = new File(mImageCachePath);
                if (!imageCacheFile.isDirectory()) {
                    imageCacheFile = null;
                } else if (!imageCacheFile.exists() && !imageCacheFile.mkdirs()) {
                    imageCacheFile = null;
                }
            }
            if (imageCacheFile == null) {
                mImageCachePath = getDefaultImageCacheFile(mContext);
            }

            if (mImageLoader == null) {
                mImageLoader = new DefaultImageDownloader();
            }

            if (mDefaultShareImage == -1) {
                mDefaultShareImage = R.drawable.default_share_image;
            }

            if (TextUtils.isEmpty(mSinaRedirectUrl)) {
                mSinaRedirectUrl = SinaHandler.DEFAULT_REDIRECT_URL;
            }

            if (TextUtils.isEmpty(mSinaScope)) {
                mSinaScope = SinaHandler.DEFAULT_SCOPE;
            }
        }

        private static String getDefaultImageCacheFile(Context context) {
            String imageCachePath = null;
            File extCacheFile = context.getExternalCacheDir();
            if (extCacheFile == null) {
                extCacheFile = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
            if (extCacheFile != null) {
                imageCachePath = extCacheFile.getAbsolutePath() + File.separator + IMAGE_CACHE_FILE_NAME + File.separator;
                File imageCacheFile = new File(imageCachePath);
                imageCacheFile.mkdirs();
            }
            return imageCachePath;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageCachePath);
        dest.writeInt(this.mDefaultShareImage);
        dest.writeString(this.mSinaRedirectUrl);
        dest.writeString(this.mSinaScope);
    }

    protected BiliShareConfiguration(Parcel in) {
        this.mImageCachePath = in.readString();
        this.mDefaultShareImage = in.readInt();
        this.mSinaRedirectUrl = in.readString();
        this.mSinaScope = in.readString();
        this.mImageDownloader = new DefaultImageDownloader();
        this.mTaskExecutor = Executors.newCachedThreadPool();
    }

    public static final Creator<BiliShareConfiguration> CREATOR = new Creator<BiliShareConfiguration>() {
        public BiliShareConfiguration createFromParcel(Parcel source) {
            return new BiliShareConfiguration(source);
        }

        public BiliShareConfiguration[] newArray(int size) {
            return new BiliShareConfiguration[size];
        }
    };
}
