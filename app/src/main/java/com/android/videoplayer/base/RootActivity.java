package com.android.videoplayer.base;


import com.android.videoplayer.R;

import androidx.annotation.Nullable;

/**
 * Activity状态类
 */
public abstract class RootActivity extends BaseActivity {
    @Override
    public void showNetworkError(@Nullable Object type, int code, int errorMsgRes) {
        showNetworkError(type, code, getString(errorMsgRes));
    }

    @Override
    public void showNetworkError(@Nullable Object type, int code, String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void showNetworkError(int code, int errorMsgRes) {
        showNetworkError(null, code, errorMsgRes);
    }

    @Override
    public void showNetworkError(int code, String errorMsg) {
        showNetworkError(null, code, errorMsg);
    }

    @Override
    public void showSuccess(String msg) {
        showSuccessDialog(msg);
    }

    @Override
    public void showSuccess(int msgRes) {
        showSuccess(getString(msgRes));
    }

    @Override
    public void showError(String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void showError(int errorMsgRes) {
        showError(getString(errorMsgRes));
    }

    @Override
    public void showError(@Nullable Object type, int code, String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void showError(@Nullable Object type, int code, int errorMsgRes) {
        showError(type, code, getString(errorMsgRes));
    }

    @Override
    public void showError(int code, String errorMsg) {
        showError(null, code, errorMsg);
    }

    @Override
    public void showError(int code, int errorMsgRes) {
        showError(null, code, errorMsgRes);
    }

    @Override
    public void showLoading(@Nullable Object type, String loadingMsg) {
        showProgressDialog(loadingMsg);
    }

    @Override
    public void showLoading(@Nullable Object type, int loadingMsgRes) {
        showLoading(type, getString(loadingMsgRes));
    }

    @Override
    public void showLoading(@Nullable Object type) {
        showLoading(type, R.string.request_state_loading);
    }

    @Override
    public void showLoading(String loadingMsg) {
        showLoading(null, loadingMsg);
    }

    @Override
    public void showLoading(int loadingMsgRes) {
        showLoading(null, loadingMsgRes);
    }

    @Override
    public void showLoading() {
        showLoading(R.string.request_state_loading);
    }

    @Override
    public void showTimeout(@Nullable Object type, String timeoutMsg) {
        showErrorDialog(timeoutMsg);
    }

    @Override
    public void showTimeout(@Nullable Object type, int timeoutMsgRes) {
        showTimeout(type, getString(timeoutMsgRes));
    }

    @Override
    public void showTimeout(@Nullable Object type) {
        showTimeout(type, R.string.request_state_timeout);
    }

    @Override
    public void showTimeout(String timeoutMsg) {
        showTimeout(null, timeoutMsg);
    }

    @Override
    public void showTimeout(int timeoutMsgRes) {
        showTimeout(null, timeoutMsgRes);
    }

    @Override
    public void showTimeout() {
        showTimeout(R.string.request_state_timeout);
    }

    @Override
    public void showEmpty(@Nullable Object type, String emptyMsgMsg) {
        showEmptyDialog(emptyMsgMsg);
    }

    @Override
    public void showEmpty(@Nullable Object type, int emptyMsgRes) {
        showEmpty(type, getString(emptyMsgRes));
    }

    @Override
    public void showEmpty(@Nullable Object type) {
        showEmpty(type, R.string.request_state_data_empty);
    }

    @Override
    public void showEmpty(String emptyMsgMsg) {
        showEmpty(null, emptyMsgMsg);
    }

    @Override
    public void showEmpty(int emptyMsgRes) {
        showEmpty(null, emptyMsgRes);
    }

    @Override
    public void showEmpty() {
        showEmpty(R.string.request_state_data_empty);
    }

    @Override
    public void showComplete(@Nullable Object type) {
        dismissProgressDialog();
    }

    @Override
    public void showComplete() {
        showComplete(null);
    }
}
