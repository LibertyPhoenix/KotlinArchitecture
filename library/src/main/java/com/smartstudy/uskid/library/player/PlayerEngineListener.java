
package com.smartstudy.uskid.library.player;

/**
 * 监听播放状态
 * @author 赵珊珊
 */
public interface PlayerEngineListener {

    void onTrackStateOrProgressChanged(PlayerEngine.PlayState state, String url, int currentPosition, int duration);

}
