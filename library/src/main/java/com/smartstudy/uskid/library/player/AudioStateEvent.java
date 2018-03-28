package com.smartstudy.uskid.library.player;


/**
 * @author 王宏杰
 * @date 2018/3/23
 */
public class AudioStateEvent {

    public AudioStateEvent(PlayerEngine.PlayState mPlayState, String url, int currentPosition,
                           int duration) {
        this.mPlayState = mPlayState;
        this.url = url;
        this.currentPosition = currentPosition;
        this.duration = duration;
    }

    private PlayerEngine.PlayState mPlayState;

    private String url;

    private int currentPosition;


    public void setPlayState(PlayerEngine.PlayState mPlayState) {
        this.mPlayState = mPlayState;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private int duration;


    public PlayerEngine.PlayState getPlayState() {
        return mPlayState;
    }

    public String getUrl() {
        return url;
    }
}
