package com.android.videoplayer.util;

/**
 * @Copyright (C)seengene
 * @Package: open
 * @ClassName: MediaUtil
 * @Description: $DESC$
 * @Author: seengene_lvTravler
 * @CreateDate: 2019/8/13 17:11
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/8/13 17:11
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MediaUtil {
    /**
     * 将进度转换为00'00''
     *
     * @param progress 毫秒
     * @return
     */
    public static String convertToStrProgress(long progress) {
        int second = (int) (progress / 1000 / 60);
        int millisecond = (int) (progress - second * 60 * 1000) / 1000;
        return second + "'" + (millisecond < 10 ? "0" + millisecond : millisecond) + "\"";
    }
}
