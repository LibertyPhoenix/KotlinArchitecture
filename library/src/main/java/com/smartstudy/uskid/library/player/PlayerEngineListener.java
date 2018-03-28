
package com.smartstudy.uskid.library.player;

/**
 * 监听播放状态
 *
 * @author 王宏杰
 * @date 2018/3/23
 */
public interface PlayerEngineListener {

    void onTrackStateOrProgressChanged(PlayerEngine.PlayState state, String url, int currentPosition, int duration);

}
