package com.android.videoplayer.video;

import android.content.Context;
import android.view.SurfaceHolder;

/**
 * @Copyright (C)open
 * @Package: open
 * @ClassName: VideoPlayerFactory，若需要使用其他播放实现方式，直接在这里替换，其他地方不用动
 * @Description: $DESC$
 * @Author: open_lvTravler
 * @CreateDate: 2019/8/29 15:40
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/8/29 15:40
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class VideoPlayerFactory {

    public static IPlayer create(Context context, SurfaceHolder surfaceHolder) {
        return new VideoMediaPlayer(context, surfaceHolder);
    }
}
