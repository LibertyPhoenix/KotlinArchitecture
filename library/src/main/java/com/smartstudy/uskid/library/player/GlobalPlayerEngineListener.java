package com.smartstudy.uskid.library.player;


import com.smartstudy.uskid.library.rxbus.RxBusHelper;

/**
 * @author 王宏杰
 * @date 2018/3/23
 */
public class GlobalPlayerEngineListener implements PlayerEngineListener {


    @Override
    public void onTrackStateOrProgressChanged(PlayerEngine.PlayState state, String mTrackPath, int currentPosition, int duration) {
        AudioStateEvent event = new AudioStateEvent(state, mTrackPath, currentPosition, duration);
        RxBusHelper.post(event);
    }
}
