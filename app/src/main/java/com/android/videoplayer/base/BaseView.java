package com.android.videoplayer.base;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public interface BaseView {

    void showNetworkError(@Nullable Object type, int code, @StringRes int errorMsgRes);

    void showNetworkError(@Nullable Object type, int code, String errorMsg);

    void showNetworkError(int code, @StringRes int errorMsgRes);

    void showNetworkError(int code, String errorMsg);

    void showSuccess(String msg);

    void showSuccess(@StringRes int msgRes);

    void showError(String errorMsg);

    void showError(@StringRes int errorMsgRes);

    void showError(@Nullable Object type, int code, String errorMsg);

    void showError(@Nullable Object type, int code, @StringRes int errorMsgRes);

    void showError(int code, String errorMsg);

    void showError(int code, @StringRes int errorMsgRes);


    void showLoading(@Nullable Object type, String loadingMsg);

    void showLoading(@Nullable Object type, @StringRes int loadingMsgRes);

    void showLoading(@Nullable Object type);

    void showLoading(String loadingMsg);

    void showLoading(@StringRes int loadingMsgRes);

    void showLoading();


    void showTimeout(@Nullable Object type, String timeoutMsg);

    void showTimeout(@Nullable Object type, @StringRes int timeoutMsgRes);

    void showTimeout(@Nullable Object type);

    void showTimeout(String timeoutMsg);

    void showTimeout(@StringRes int timeoutMsgRes);

    void showTimeout();


    void showEmpty(@Nullable Object type, String emptyMsg);

    void showEmpty(@Nullable Object type, @StringRes int emptyMsgRes);

    void showEmpty(@Nullable Object type);

    void showEmpty(String emptyMsg);

    void showEmpty(@StringRes int emptyMsgRes);

    void showEmpty();


    void showComplete(@Nullable Object type);

    void showComplete();
}
