package com.android.videoplayer.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.android.videoplayer.Config;
import com.android.videoplayer.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * @Copyright (C)open
 * @Package: open
 * @ClassName: VideoMediaPlayer
 * @Description: 播放的真正实现者
 * @Author: open_lvTravler
 * @CreateDate: 2019/8/29 15:39
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/8/29 15:39
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class VideoMediaPlayer implements IPlayer, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private IPlayerCallback mPlayerCallback;
    private Context mContext;
    private Handler mHandler = new Handler();
    /**
     * 是否缓冲完成
     */
    private boolean isPrepared = false;
    /**
     * 播放状态
     */
    @PlayStatus
    private int mStatus;
    private Timer mTimer;

    public VideoMediaPlayer(Context context, SurfaceHolder surfaceHolder) {
        mContext = context;
        mSurfaceHolder = surfaceHolder;
        createMediaPlayer();
        setStatus(PlayStatus.NONE);
    }

    private void createMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
    }

    @Override
    public void play() {
        if (mMediaPlayer.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    @Override
    public void play(@NonNull String source) {
        try {
            mMediaPlayer.setDataSource(source);
            mMediaPlayer.prepareAsync();
            setStatus(PlayStatus.BUFFERING);
        } catch (IOException e) {
            setStatus(PlayStatus.ERROR);
            if (mPlayerCallback != null) {
                mPlayerCallback.onError(getString(R.string.play_error_prompt));
            }
        }
    }

    /**
     * 这儿需要注意一下，一般此方法会在Activity/Fragment的start()中调用，但是如果是开始初始化视频的话，当调用方法时视频还未缓冲好，就会报错
     */
    @Override
    public void start() {
        if (!mMediaPlayer.isPlaying() && isPrepared) {
            mMediaPlayer.start();
        }
        setStatus(PlayStatus.PLAYING);
        registerProgressListener();
    }

    @Override
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            setStatus(PlayStatus.PAUSED);
        }
        unRegisterProgress();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        setStatus(PlayStatus.STOPPED);
        unRegisterProgress();
    }

    @Override
    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
        setStatus(PlayStatus.BUFFERING);
        unRegisterProgress();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public long getCurrentStreamPosition() {
        return isPlaying() ? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public void setStatus(int status) {
        mStatus = status;
        if (mPlayerCallback != null) {
            mPlayerCallback.onPlayStatusChange(status);
        }
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void setCallback(@NonNull IPlayerCallback playerCallback) {
        mPlayerCallback = playerCallback;
    }

    @Override
    public void setOnMetadataUpdateListener() {

    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        setStatus(PlayStatus.ERROR);
        unRegisterProgress();
        if (mPlayerCallback != null) {
            mPlayerCallback.onError(getString(R.string.play_error_prompt));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (mPlayerCallback != null) {
            mPlayerCallback.onPrepared(getDuration());
        }
        mp.setDisplay(mSurfaceHolder);
        start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayerCallback != null) {
            mPlayerCallback.onComplete();
        }
        setStatus(PlayStatus.COMPLETE);
        unRegisterProgress();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        start();
    }

    private void registerProgressListener() {
        if (mPlayerCallback != null) {
            unRegisterProgress();
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPlayerCallback.onUpdateProgress((int) getCurrentStreamPosition());
                        }
                    });
                }
            }, 0, Config.MEDIA_MONITOR_INTERVAL);
        }
    }

    private void unRegisterProgress() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private String getString(@StringRes int res) {
        return mContext.getString(res);
    }
}
