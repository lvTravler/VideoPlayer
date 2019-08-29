package com.android.videoplayer.video;

import androidx.annotation.NonNull;

/**
 * @Copyright (C)open
 * @Package:
 * @ClassName: IPlayer
 * @Description: 媒体功能总接口
 * @Author: open_lvTravler
 * @CreateDate: 2019/8/13 14:48
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/8/13 14:48
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public interface IPlayer {

    void play();

    void play(@NonNull String source);

    void start();

    void pause();

    void stop();

    void seekTo(int position);

    boolean isPlaying();

    long getCurrentStreamPosition();

    void setStatus(int status);

    int getStatus();

    float getSpeed();

    int getDuration();

    void setCallback(@NonNull IPlayerCallback playerCallback);

    void setOnMetadataUpdateListener();

    void setVolume(float leftVolume, float rightVolume);
}
