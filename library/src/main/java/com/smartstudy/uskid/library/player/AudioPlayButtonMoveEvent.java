package com.smartstudy.uskid.library.player;

/**
 *
 * @author 赵珊珊
 * @date 15/1/20
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
