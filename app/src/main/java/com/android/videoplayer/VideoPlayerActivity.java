package com.android.videoplayer;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.videoplayer.base.RootActivity;
import com.android.videoplayer.util.MediaUtil;
import com.android.videoplayer.video.IPlayer;
import com.android.videoplayer.video.IPlayerCallback;
import com.android.videoplayer.video.PlayStatus;
import com.android.videoplayer.video.VideoPlayerFactory;
import com.android.videoplayer.video.VideoPlayerManager;

import androidx.appcompat.widget.AppCompatSeekBar;

public class VideoPlayerActivity extends RootActivity implements IPlayerCallback {

    public static final String VIDEO_SOURCE = "https://vd2.bdstatic.com/mda-igwdmz0nv8ah95xv/mda-igwdmz0nv8ah95xv.mp4?playlist=%5B%22hd%22%2C%22sc%22%5D&auth_key=1567068990-0-0-003a15194906c2b947cf493eec963a7f&bcevod_channel=searchbox_feed&pd=bjh";

    SurfaceView mSlActVideoPlayerShow;
    TextView mTvItemMasterBottomAudioProgressTime;
    AppCompatSeekBar mSbItemMasterBottomAudioProgress;
    TextView mTvItemMasterBottomAudioAllTime;
    ImageView mImgItemMasterSkipPrevious;
    ImageView mImgItemMasterBottomPlayOrPause;
    ImageView mImgItemMasterSkipNext;

    private VideoPlayerManager mPlayerManager;
    private SurfaceHolder mHolder;


    @Override
    protected int setLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_item_master_bottom_play_or_pause:
                mPlayerManager.play();
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();

        mSlActVideoPlayerShow = findViewById(R.id.sl_act_video_player_show);
        mTvItemMasterBottomAudioProgressTime = findViewById(R.id.tv_item_master_bottom_audio_progress_time);
        mSbItemMasterBottomAudioProgress = findViewById(R.id.sb_item_master_bottom_audio_progress);
        mTvItemMasterBottomAudioAllTime = findViewById(R.id.tv_item_master_bottom_audio_all_time);
        mImgItemMasterSkipPrevious = findViewById(R.id.img_item_master_skip_previous);
        mImgItemMasterBottomPlayOrPause = findViewById(R.id.img_item_master_bottom_play_or_pause);
        mImgItemMasterSkipNext = findViewById(R.id.img_item_master_skip_next);

        mImgItemMasterSkipPrevious.setVisibility(View.GONE);
        mImgItemMasterSkipNext.setVisibility(View.GONE);
        mHolder = mSlActVideoPlayerShow.getHolder();
        mPlayerManager = new VideoPlayerManager(getPlayer());
        mPlayerManager.setCallback(this);
        mPlayerManager.play(VIDEO_SOURCE);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mSbItemMasterBottomAudioProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayerManager.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerManager.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerManager.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            mPlayerManager.stop();
        }
    }

    @Override
    public void onPrepared(int totalTime) {
        mSbItemMasterBottomAudioProgress.setMax(totalTime);
        mTvItemMasterBottomAudioAllTime.setText(MediaUtil.convertToStrProgress(totalTime));
    }

    @Override
    public void onError(String errorMsg) {
        showError(errorMsg);
    }

    @Override
    public void onComplete() {
        showSuccess(R.string.play_complete);
    }

    @Override
    public void onPlayStatusChange(int status) {
        if (status == PlayStatus.PLAYING) {
            mImgItemMasterBottomPlayOrPause.setImageResource(R.mipmap.icon_master_audio_pause);
        } else {
            mImgItemMasterBottomPlayOrPause.setImageResource(R.mipmap.icon_master_audio_play);
        }

        if (status == PlayStatus.COMPLETE) {//特殊情况下播放完毕和SeekBar进度稍微差点，这儿就给设置完毕了
            mSbItemMasterBottomAudioProgress.setProgress(mSbItemMasterBottomAudioProgress.getMax());
        }
    }

    @Override
    public void onUpdateProgress(int progress) {
        mSbItemMasterBottomAudioProgress.setProgress(progress);
        mTvItemMasterBottomAudioProgressTime.setText(MediaUtil.convertToStrProgress(progress));
    }

    private IPlayer getPlayer() {
        return VideoPlayerFactory.create(this, mHolder);
    }
}
