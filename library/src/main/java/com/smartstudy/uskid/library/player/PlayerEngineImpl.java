

package com.smartstudy.uskid.library.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceView;

import com.orhanobut.logger.Logger;
import com.smartstudy.uskid.library.rxbus.RxBusHelper;
import com.smartstudy.uskid.library.utils.ConstUtils;
import com.smartstudy.uskid.library.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * 音视频播放
 *
 * @author 王宏杰
 * @date 2018/3/23
 */
public class PlayerEngineImpl implements PlayerEngine, Downloader.DownloadListener {

    private static final String DIR_PATH = Environment.getExternalStorageDirectory()
            + "/" + ConstUtils.APP_ROOT_DIR; // 下载目录
    private static String sdcardirPath;
    private static PlayerEngineImpl instance;
    private final Handler mHandler;
    public PlayState mPlayState = PlayState.STOPPED;//播放状态
    private String mTrackPath;//音频URL
    public MediaPlayer mMediaPlayer;
    private PlayOrder mPlayOrder;
    private Downloader mDownloader;
    private PlayerEngineListener mPlayerEngineListener;
    private final Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (mPlayerEngineListener != null) {
                if (mMediaPlayer != null)
                    mPlayerEngineListener.onTrackStateOrProgressChanged(
                            mPlayState, mTrackPath, getCurrentPosition(), getDuration()
                    );
                mHandler.postDelayed(this, 1000);
            }
        }
    };
    private List<MediaModel> mTrackList;
    private int mPosition;

    private int mDownloadPosition;

    private String mCurrentDownloderUrl;
    private boolean isAutoPlayPrevious = false;
    private Context mContext;

    private PlayerEngineImpl(Context context) {
        mHandler = new Handler();
        mDownloader = new Downloader();
        mDownloader.setDownloadListener(this);
        mContext = context;
        sdcardirPath = FileUtils.getStoragePath(mContext, true) + "/Android/data/" + mContext.getPackageName() + "/";
    }

    public static PlayerEngineImpl getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerEngineImpl(context);
            instance.setListener(new GlobalPlayerEngineListener());
        }
        return instance;
    }

    public PlayState getPlayState() {
        return mPlayState;
    }

    @Override
    public void pause() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        if (mMediaPlayer != null) {
            // 正在准备
            if (mPlayState == PlayState.PREPARING) {
                return;
            }
            if (mPlayState == PlayState.PLAYING) {
                mMediaPlayer.pause();
                mPlayState = PlayState.PAUSE;
                Logger.e("暂停");
                if (mPlayerEngineListener != null)
                    mPlayerEngineListener.onTrackStateOrProgressChanged(mPlayState, mTrackPath, getCurrentPosition(), getDuration());
                return;
            }
        }
    }

    public void play(String trackPath) {
        Logger.d("play方法调用play方法");
        play(trackPath, null);
    }

    public void setPlayOrder(PlayOrder playOrder) {
        mPlayOrder = playOrder;
    }

    public void playList(int position) {
        mPosition = position;
        mDownloadPosition = position;
        if (mTrackList != null) {
            play(mTrackList.get(mPosition).getmAudioUrl());
        }

    }


    public void play(String trackPath, SurfaceView surfaceView, int currentPosition) {

        if (!TextUtils.isEmpty(trackPath)) {


            Logger.d("播放文件路径" + trackPath);


            isAutoPlayPrevious = false;

            if (mMediaPlayer == null) {
                mMediaPlayer = build(trackPath, surfaceView, currentPosition);
            }
            //切换
            if (!trackPath.equals(mTrackPath)) {
                cleanUp();
                mMediaPlayer = build(trackPath, surfaceView, currentPosition);
            }
            //build错误
            if (mMediaPlayer == null)
                return;

            //准备完成或者暂停，开始播放
            if (mPlayState == PlayState.PREPARED || mPlayState == PlayState.PAUSE) {
                if (surfaceView != null) {
                    mMediaPlayer.setDisplay(surfaceView.getHolder());
                }

                mMediaPlayer.start();
                if (currentPosition > 0) {
                    mMediaPlayer.seekTo(currentPosition);
                }
                mPlayState = PlayState.PLAYING;

                if (mPlayerEngineListener != null)
                    mPlayerEngineListener.onTrackStateOrProgressChanged(mPlayState, mTrackPath, getCurrentPosition(), getDuration());
                mHandler.postDelayed(mUpdateTimeTask, 10);
            } else if (mPlayState == PlayState.PLAYING) {
                pause();
            }
        }

    }

    public void play(String trackPath, SurfaceView surfaceView) {
        play(trackPath, surfaceView, 0);
    }

    /**
     *
     */
    public void restartPlay() {
        if (mPlayState == PlayState.PAUSE) {
            mMediaPlayer.start();
            mPlayState = PlayState.PLAYING;
            // mHandler.postDelayed(mUpdateTimeTask, 10);
        }
    }

    private void download(String url) {
        mDownloader.download(url);
        mCurrentDownloderUrl = url;
    }

    @Override
    public void stop() {
        Logger.d("stop");
        cleanUp();
    }

    private void cleanUp() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        if (mMediaPlayer != null) {
            try {
                if (mPlayState != PlayState.PREPARING) {
                    mMediaPlayer.stop();
                } else {
                    mMediaPlayer.reset();
                }
                mPlayState = PlayState.STOPPED;

                if (mPlayerEngineListener != null)
                    mPlayerEngineListener.onTrackStateOrProgressChanged(mPlayState, mTrackPath, 0, 0);
            } catch (IllegalStateException e) {

            } finally {

                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }


    }

    private MediaPlayer build(final String trackPath, final SurfaceView surfaceView, final int currentPosition) {

        final MediaPlayer mediaPlayer = new MediaPlayer();
        mTrackPath = trackPath;
        String path = trackPath;

        String fileName = trackPath.substring(trackPath.lastIndexOf("/") + 1);
        File file = new File(DIR_PATH, fileName);
        if (!file.exists()) {
            //如果文件不存在,去sdcard2上找
            file = new File(sdcardirPath, fileName);
        }
        if (file.exists()) {
            path = file.getAbsolutePath();
        } else {
            //下载
            if (!mDownloader.isDownloading()) {
                download(trackPath);
            }

        }
        try {
            mediaPlayer.setDataSource(path);
            if (surfaceView != null) {
                mediaPlayer.setDisplay(surfaceView.getHolder());
            }
            mediaPlayer.setOnCompletionListener(
                    mp -> {
                        mPlayState = PlayState.COMPLETE;
                        if (mPlayerEngineListener != null)
                            mPlayerEngineListener.onTrackStateOrProgressChanged(mPlayState, mTrackPath, getDuration(), getDuration());
                        stop();

                        if (mPlayOrder == PlayOrder.NEXT) {
                            playNext();
                        } else if (mPlayOrder == PlayOrder.PREVIOUR) {
                            playPrevious();
                        }


                    }
            );
            mediaPlayer.setOnPreparedListener(
                    mp -> {
                        mPlayState = PlayState.PREPARED;
                        Logger.d("准备完成，开始播放了");
                        if (mPlayerEngineListener != null)
                            mPlayerEngineListener.onTrackStateOrProgressChanged(mPlayState, mTrackPath, 0, 0);
                        play(mTrackPath, surfaceView, currentPosition);
                    }
            );
            mediaPlayer
                    .setOnBufferingUpdateListener(
                            (mp, percent) -> {
                            }
                    );

            mediaPlayer.setOnErrorListener(
                    (mp, what, extra) -> {

                        if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                            mPlayState = PlayState.ERROR;
                            if (mPlayerEngineListener != null)
                                mPlayerEngineListener.onTrackStateOrProgressChanged(mPlayState, mTrackPath, 0, 0);

                            return true;
                        }
                        return false;
                    }
            );
            mPlayState = PlayState.PREPARING;
            if (mPlayerEngineListener != null)
                mPlayerEngineListener.onTrackStateOrProgressChanged(mPlayState, mTrackPath, 0, 0);
            mediaPlayer.prepareAsync();

            return mediaPlayer;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void playNext() {
        if (mTrackList != null) {
            if (mPosition < mTrackList.size() - 1) {
                mPosition++;
                RxBusHelper.post(new AudioPlayButtonMoveEvent(mPosition));
                play(mTrackList.get(mPosition).getmAudioUrl());
            } else {
                stop();
            }
        }
    }

    private void playPrevious() {
        Logger.e("执行播放下一个的方法");
        if (mTrackList != null) {
            if (mPosition > 0) {
                mPosition--;
                RxBusHelper.post(new AudioPlayButtonMoveEvent(mPosition));
                Logger.e("播放下一个" + mPosition);
                play(mTrackList.get(mPosition).getmAudioUrl());
            } else {
                Logger.e("没有新的音频，如果有新的将会自动播放下一个" + mPosition);
                isAutoPlayPrevious = true;
                stop();
            }
        }

    }

    @Override
    public void setListener(PlayerEngineListener playerEngineListener) {
        mPlayerEngineListener = playerEngineListener;
    }


    @Override
    public void seekTo(int progress) {
        if (mMediaPlayer != null)
            mMediaPlayer.seekTo(progress);
    }

    public String getUrl() {
        return mTrackPath;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getDuration() {
        try {
            if (mMediaPlayer != null) {
                return mMediaPlayer.getDuration();
            } else {
                return 0;
            }
        } catch (Exception e) {
            Logger.e("getDuration error");
            return 0;
        }


    }

    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer == null ? false : mMediaPlayer.isPlaying();
    }


    private void downloadNext() {
        if (mTrackList != null) {
            if (mDownloadPosition < mTrackList.size() - 1) {
                mDownloadPosition++;
                download(mTrackList.get(mDownloadPosition).getmAudioUrl());
            }
        }
    }

    @Override
    public void downloadFinish() {
        downloadNext();
    }

    public void setTrackList(List<MediaModel> trackList) {
        if (trackList != null) {
            mTrackList = trackList;
            //更新position
            if (mTrackList == null) {
                return;
            }
            for (int i = 0; i < mTrackList.size(); i++) {
                if ((mTrackList.get(i).getmAudioUrl() != null && mTrackList.get(i).getmAudioUrl().equals(mTrackPath))) {
                    //自动播放第一个 直播教室是倒序
                    if (isAutoPlayPrevious && mPosition != i) {
                        playList(0);
                    }
                    mPosition = i;
                    Logger.e("更新position" + mPosition);
                }

                if (mTrackList.get(i).equals(mCurrentDownloderUrl)) {
                    mDownloadPosition = i;
                }
            }
        }
    }
}
