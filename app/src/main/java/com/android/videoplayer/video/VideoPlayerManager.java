package com.android.videoplayer.video;

import androidx.annotation.NonNull;

/**
 * @Copyright (C)open
 * @Package: open
 * @ClassName: VideoPlayerManager
 * @Description: 视频播放管理类
 * @Author: open_lvTravler
 * @CreateDate: 2019/8/29 15:29
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/8/29 15:29
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class VideoPlayerManager implements IPlayer {

    private IPlayer mPlayer;

    public VideoPlayerManager(@NonNull IPlayer player) {
        mPlayer = player;
    }

    @Override
    public void play() {
        mPlayer.play();
    }

    @Override
    public void play(@NonNull String source) {
        mPlayer.play(source);
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void stop() {
        mPlayer.stop();
    }

    @Override
    public void seekTo(int position) {
        mPlayer.seekTo(position);
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public long getCurrentStreamPosition() {
        return mPlayer.getCurrentStreamPosition();
    }

    @Override
    public void setStatus(int status) {

    }

    @Override
    public int getStatus() {
        return mPlayer.getStatus();
    }

    @Override
    public float getSpeed() {
        return mPlayer.getSpeed();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public void setCallback(@NonNull IPlayerCallback playerCallback) {
        mPlayer.setCallback(playerCallback);
    }

    @Override
    public void setOnMetadataUpdateListener() {

    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mPlayer.setVolume(leftVolume, rightVolume);
    }
}
