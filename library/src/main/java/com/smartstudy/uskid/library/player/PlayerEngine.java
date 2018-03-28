
package com.smartstudy.uskid.library.player;

/**
 * 控制播放状态
 *
 * @author 王宏杰
 * @date 2018/3/23
 */
public interface PlayerEngine {
    //状态
    public enum PlayState {
        PREPARING, PREPARED, PLAYING, PAUSE, STOPPED, COMPLETE, ERROR
    }

    //播放顺序
    public enum PlayOrder {
        PREVIOUR, NEXT, NONE
    }

    // 停止
    public void stop();

    // 暂停
    public void pause();

    //
    public void seekTo(int progress);

    // 设置监听位置
    public void setListener(PlayerEngineListener playerEngineListener);

}
