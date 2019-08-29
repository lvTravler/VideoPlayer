package com.android.videoplayer.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.android.dialog.utils.Utils;
import com.android.dialog.widgets.StatusDialog;
import com.android.videoplayer.R;
import com.android.videoplayer.util.ViewUtil;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView, View.OnClickListener {
    public Handler mMainHandler = new Handler();
    private StatusDialog mProgressBar;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutRes());
        init();
        register();
        initView();
        setListener();
        onCreatedRequest();
    }


    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        onStartRequest();
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        dismissDialog();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        unRegister();
        super.onDestroy();
    }

    protected void register() {

    }

    protected void unRegister() {

    }

    protected void init() {

    }

    protected Handler getMainHandler() {
        return mMainHandler;
    }


    /**
     * 获取最顶部而且可见的控件
     * 注意：这里的top不包括状态栏的填充View
     *
     * @return
     */
    @Nullable
    private View getTopVisibleView() {
        View rootView = getRootView();
        if (rootView instanceof ViewGroup) {
            ViewGroup rootViewGroup = (ViewGroup) rootView;
            int childCount = rootViewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = rootViewGroup.getChildAt(i);
                if (isTopVisibleView(childAt)) {//去除顶部填充View
                    return childAt;
                }
            }
        } else {
            return isTopVisibleView(rootView) ? rootView : null;
        }
        return null;
    }

    private boolean isTopVisibleView(View childAt) {
        int childAtId = childAt.getId();
        return childAt.getVisibility() == View.VISIBLE && childAtId != R.id.statusbarutil_translucent_view
                && childAtId != R.id.statusbarutil_translucent_imageview;
    }

    /**
     * 比较懒，就直接遍历所有View监听了
     */
    @CallSuper
    protected void setListener() {
        if (isAutoSetOnClickListener()) {
            ViewUtil.depthTravelView(getRootView(), this);
        }
    }

    /**
     * 是否自动设置所有View的Click事件
     *
     * @return
     */
    protected boolean isAutoSetOnClickListener() {
        return true;
    }

    /**
     * TODO 这就要求所有的布局都使用ConstraintLayout
     *
     * @return
     */
    protected final View getRootView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    protected void removeStatusBar(ConstraintLayout rootView) {
        View view = rootView.getViewById(R.id.tb_act_title);//删除已经存在的TitleBar
        if (view != null) {
            rootView.removeView(view);
        }
    }

    protected void onStartRequest() {

    }

    protected void onCreatedRequest() {

    }

    @LayoutRes
    protected abstract int setLayoutRes();

    @CallSuper
    protected void initView() {
    }

    protected void showProgressDialog(String msg) {
        mProgressBar = StatusDialog.with(this)
                .setPrompt(msg)
                .setType(StatusDialog.Type.PROGRESS)
                .setCancelable(false)
                .show();
    }

    protected void showProgressDialog(@StringRes int msgRes) {
        showProgressDialog(getString(msgRes));
    }

    protected void dismissProgressDialog() {
        if (mProgressBar != null) {
            mProgressBar.dismiss();
        }
    }


    protected void showEmptyDialog(String msg) {
        showDialog(StatusDialog.Type.ERROR, msg);
    }

    protected void showEmptyDialog(@StringRes int msgRes) {
        showEmptyDialog(getString(msgRes));
    }

    protected void showErrorDialog(String msg) {
        showDialog(StatusDialog.Type.ERROR, msg);
    }

    protected void showErrorDialog(@StringRes int msgRes) {
        showErrorDialog(getString(msgRes));
    }

    protected void showSuccessDialog(String msg) {
        showDialog(StatusDialog.Type.SUCCESS, msg);
    }


    protected void showSuccessDialog(@StringRes int msgRes) {
        showSuccessDialog(getString(msgRes));
    }

    protected void showDialog(int type, String msg) {
        StatusDialog.with(getActivity())
                .setCancelable(false)
                .setPrompt(msg)
                .setType(type)
                .show();
    }

    protected void dismissDialog() {

    }

    protected Drawable getDrawableRes(@DrawableRes int drawableRes) {
        return getResources().getDrawable(drawableRes);
    }

    protected int getColorRes(@ColorRes int colorRes) {
        return getResources().getColor(colorRes);
    }

    protected AppCompatActivity getActivity() {
        return this;
    }

    protected Context getContext() {
        return this;
    }

    protected boolean isNonEmpty(String content) {
        return Utils.isNonEmpty(content);
    }

    @Override
    public void onClick(View v) {

    }
}
