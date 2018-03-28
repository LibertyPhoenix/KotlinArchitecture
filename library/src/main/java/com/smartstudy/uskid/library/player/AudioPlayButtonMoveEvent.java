package com.smartstudy.uskid.library.player;

/**
 * @author 王宏杰
 * @date 2018/3/23
 */
public class AudioPlayButtonMoveEvent {

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;

    public AudioPlayButtonMoveEvent(int position) {
        this.position = position;
    }
}
