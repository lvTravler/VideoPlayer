package com.android.videoplayer.util;

import android.view.View;
import android.view.ViewGroup;

import com.android.videoplayer.R;

import androidx.annotation.NonNull;

/**
 * @Copyright (C)   open
 * @Package: com.open.ar_guide.util
 * @ClassName: ViewUtil
 * @Description: $DESC$
 * @Author: open_lvTravler
 * @CreateDate: 2019/7/18 15:33
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/7/18 15:33
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class ViewUtil {

    /**
     * 遍历所有控件并设置监听
     *
     * @param rootView
     * @param onClickListener
     * @see https://blog.csdn.net/zhonglixiaobin/article/details/85700055
     */
    public static void depthTravelView(@NonNull View rootView, @NonNull View.OnClickListener onClickListener) {
        if (rootView instanceof ViewGroup) {
            ViewGroup rootViewGroup = (ViewGroup) rootView;
            int childCount = rootViewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = rootViewGroup.getChildAt(i);
                if (child instanceof ViewGroup) {
                    int childId = child.getId();
                    if (childId != R.id.tb_act_title) {//TitleBar这里不设置，否则就会覆盖，是监听失效
                        depthTravelView(child, onClickListener);
                    }
                } else {
                    child.setOnClickListener(onClickListener);
                }
            }
        } else {
            rootView.setOnClickListener(onClickListener);
        }
    }


}
