package com.smartstudy.uskid.library.player;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaModel implements Parcelable {
    protected String mAudioUrl;
    protected String mVideoUrl;
    protected int mAudioState;
    protected int mVideoState;
    protected int mCurrentPosition;
    protected int mTotalTime;

    public String getmAudioUrl() {
        return mAudioUrl;
    }

    public void setmAudioUrl(String mAudioUrl) {
        this.mAudioUrl = mAudioUrl;
    }

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public void setmVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    public int getmAudioState() {
        return mAudioState;
    }

    public void setmVideoState(int mVideoState) {
        this.mVideoState = mVideoState;
    }

    public MediaModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAudioUrl);
        dest.writeString(this.mVideoUrl);
        dest.writeInt(this.mAudioState);
        dest.writeInt(this.mVideoState);
        dest.writeInt(this.mCurrentPosition);
        dest.writeInt(this.mTotalTime);
    }

    protected MediaModel(Parcel in) {
        this.mAudioUrl = in.readString();
        this.mVideoUrl = in.readString();
        this.mAudioState = in.readInt();
        this.mVideoState = in.readInt();
        this.mCurrentPosition = in.readInt();
        this.mTotalTime = in.readInt();
    }

}