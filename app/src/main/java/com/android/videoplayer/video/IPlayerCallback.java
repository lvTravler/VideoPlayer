package com.android.videoplayer.video;

/**
 * @Copyright (C)open
 * @Package:
 * @ClassName: IPlayerCallback
 * @Description: 播放状态回调
 * @Author: open_lvTravler
 * @CreateDate: 2019/8/12 20:28
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/8/12 20:28
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public interface IPlayerCallback {

    void onPrepared(int totalTime);

    void onError(String errorMsg);

    void onComplete();

    void onPlayStatusChange(int status);

    void onUpdateProgress(int progress);
}
