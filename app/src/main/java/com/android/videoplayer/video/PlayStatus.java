package com.android.videoplayer.video;

/**
 * @Copyright (C)open
 * @Package: open
 * @ClassName: PlayStatus
 * @Description: 播放状态
 * @Author: open_lvTravler
 * @CreateDate: 2019/8/29 17:16
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/8/29 17:16
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public @interface PlayStatus {

    int NONE = 0x000000001;

    int BUFFERING = 0x000000002;

    int PLAYING = 0x000000003;

    int PAUSED = 0x000000004;

    int STOPPED = 0x000000005;

    int ERROR = 0x000000006;

    int COMPLETE = 0x000000007;

}
