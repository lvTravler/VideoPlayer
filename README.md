>视频播放在Android开发中也比较常见，Android API中提供了VideoView来实现视频播放，但是扩展性不太高，尤其是UI方面，而使用MediaPlayer+SurfaceView+MediaController则可实现多种多样的、扩展性高的视频播放器，但是有利就有弊，使用后者开发量相对而言比较大一点、比较复杂一点。下面就使用MediaPlayer+SurfaceView+自定义MediaController实现一个自定义视频播放器。

功能实现：
1. 视频加载播放
2. 视频播放。暂停
3. 视频快进、快退
4. 保留扩展性接口，如切换下一个等
5. 其他

效果图： 

![VideoPlayer.png](https://github.com/lvTravler/VideoPlayer/blob/master/app/image/VideoPlayerShow.png)

视频播放器结构图：

![结构图.png](https://upload-images.jianshu.io/upload_images/10301290-eb56c2cdef8be9fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- IPlayer作为播放器的总行为接口，定义play、start、pause、stop、seekTo等行为
```
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
```
- VideoMediaPlayer实现了IPlayer，使用Android原生MediaPlayer负责具体播放功能实现
```
public class VideoMediaPlayer implements IPlayer, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private IPlayerCallback mPlayerCallback;
    private Context mContext;
    private Disposable mProgressSubscribe;
    /**
     * 是否缓冲完成
     */
    private boolean isPrepared = false;
    /**
     * 播放状态
     */
    @PlayStatus
    private int mStatus;

    public VideoMediaPlayer(Context context, SurfaceHolder surfaceHolder) {
        mContext = context;
        mSurfaceHolder = surfaceHolder;
        createMediaPlayer();
        setStatus(PlayStatus.NONE);
    }

    private void createMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
    }

    @Override
    public void play() {
        if (mMediaPlayer.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    @Override
    public void play(@NonNull String source) {
        try {
            mMediaPlayer.setDataSource(source);
            mMediaPlayer.prepareAsync();
            setStatus(PlayStatus.BUFFERING);
        } catch (IOException e) {
            setStatus(PlayStatus.ERROR);
            if (mPlayerCallback != null) {
                mPlayerCallback.onError(getString(R.string.play_error_prompt));
            }
        }
    }

    /**
     * 这儿需要注意一下，一般此方法会在Activity/Fragment的start()中调用，但是如果是开始初始化视频的话，当调用方法时视频还未缓冲好，就会报错
     */
    @Override
    public void start() {
        if (!mMediaPlayer.isPlaying() && isPrepared) {
            mMediaPlayer.start();
            setStatus(PlayStatus.PLAYING);
        }
        registerProgressListener();
    }

    @Override
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            setStatus(PlayStatus.PAUSED);
        }
        unRegisterProgress();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        setStatus(PlayStatus.STOPPED);
        unRegisterProgress();
    }

    @Override
    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
        setStatus(PlayStatus.BUFFERING);
        unRegisterProgress();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public long getCurrentStreamPosition() {
        return isPlaying() ? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public void setStatus(int status) {
        mStatus = status;
        if (mPlayerCallback != null) {
            mPlayerCallback.onPlayStatusChange(status);
        }
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void setCallback(@NonNull IPlayerCallback playerCallback) {
        mPlayerCallback = playerCallback;
    }

    @Override
    public void setOnMetadataUpdateListener() {

    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        setStatus(PlayStatus.ERROR);
        unRegisterProgress();
        if (mPlayerCallback != null) {
            mPlayerCallback.onError(getString(R.string.play_error_prompt));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (mPlayerCallback != null) {
            mPlayerCallback.onPrepared(getDuration());
        }
        mp.setDisplay(mSurfaceHolder);
        start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayerCallback != null) {
            mPlayerCallback.onComplete();
        }
        setStatus(PlayStatus.COMPLETE);
        unRegisterProgress();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        start();
    }

    private void registerProgressListener() {
        if (mPlayerCallback != null) {
            unRegisterProgress();
            mProgressSubscribe = Flowable.interval(Config.MEDIA_MONITOR_INTERVAL, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mPlayerCallback.onUpdateProgress((int) getCurrentStreamPosition());
                        }
                    });
        }
    }

    private void unRegisterProgress() {
        if (mProgressSubscribe != null && !mProgressSubscribe.isDisposed()) {
            mProgressSubscribe.dispose();
        }
    }

    private String getString(@StringRes int res) {
        return mContext.getString(res);
    }
}
```
- VideoPlayerFactory负责创建IPalyer实现，这里也就是VideoMediaPlayer
```
public class VideoPlayerFactory {

    public static IPlayer create(Context context, SurfaceHolder surfaceHolder) {
        return new VideoMediaPlayer(context, surfaceHolder);
    }
}
```
>使用上述三者有什么好处呢？就是可以将播放行为和具体实现实现解耦，当具体播放功能实现由MediaPlayer更换为其他方式时，只需要实现IPlayer，更换VideoPlayerFactory中返回值即可，原来逻辑一点儿也不用变，这儿体现了面向对象依赖倒置原则、开闭原则、单一原则、里式替换原则[面向对象几大原则](https://www.jianshu.com/p/394beab73648)
- VideoPlayerManager实现了IPlayer接口，使用IPlayer的具体实现即VideoMediaPlayer来统一管理各播放器行为（start、pause等）
```
public class VideoPlayerManager implements IPlayer {

    private IPlayer mPlayer;

    public VideoPlayerManager(@NonNull IPlayer player) {
        mPlayer = player;
    }

    @Override
    public void play() {
        mPlayer.play();
    }

    @Override
    public void play(@NonNull String source) {
        mPlayer.play(source);
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void stop() {
        mPlayer.stop();
    }

    @Override
    public void seekTo(int position) {
        mPlayer.seekTo(position);
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public long getCurrentStreamPosition() {
        return mPlayer.getCurrentStreamPosition();
    }

    @Override
    public void setStatus(int status) {

    }

    @Override
    public int getStatus() {
        return mPlayer.getStatus();
    }

    @Override
    public float getSpeed() {
        return mPlayer.getSpeed();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public void setCallback(@NonNull IPlayerCallback playerCallback) {
        mPlayer.setCallback(playerCallback);
    }

    @Override
    public void setOnMetadataUpdateListener() {

    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mPlayer.setVolume(leftVolume, rightVolume);
    }
}
```
- IPlaylerCallback对VideoMediaPlayer播放状态进行回调，例如播放准备完毕、播放错误、播放进度、播放完成等
```
public interface IPlayerCallback {

    void onPrepared(int totalTime);

    void onError(String errorMsg);

    void onComplete();

    void onPlayStatusChange(int status);

    void onUpdateProgress(int progress);
}
```
- PlayStatus中定义了各种播放状态，例如播放中、暂停、停止、缓冲、完成等状态
```
public @interface PlayStatus {

    int NONE = 0x000000001;

    int BUFFERING = 0x000000002;

    int PLAYING = 0x000000003;

    int PAUSED = 0x000000004;

    int STOPPED = 0x000000005;

    int ERROR = 0x000000006;

    int COMPLETE = 0x000000007;

}
```
- VideoPlayerActivity/Fragment包含了播放器UI界面和各种控制事件，实现了IPlayerCallback接口，用来接收播放状态以用来更新UI播放控件
```
/**
 * Video播放器
 */
public class VideoPlayerActivity extends RootActivity implements IPlayerCallback {

    public static final String VIDEO_SOURCE = "https://vd2.bdstatic.com/mda-igwdmz0nv8ah95xv/mda-igwdmz0nv8ah95xv.mp4?playlist=%5B%22hd%22%2C%22sc%22%5D&auth_key=1567068990-0-0-003a15194906c2b947cf493eec963a7f&bcevod_channel=searchbox_feed&pd=bjh";

    @BindView(R.id.sl_act_video_player_show)
    SurfaceView mSlActVideoPlayerShow;
    @BindView(R.id.tv_item_master_bottom_audio_progress_time)
    TextView mTvItemMasterBottomAudioProgressTime;
    @BindView(R.id.sb_item_master_bottom_audio_progress)
    AppCompatSeekBar mSbItemMasterBottomAudioProgress;
    @BindView(R.id.tv_item_master_bottom_audio_all_time)
    TextView mTvItemMasterBottomAudioAllTime;
    @BindView(R.id.img_item_master_skip_previous)
    ImageView mImgItemMasterSkipPrevious;
    @BindView(R.id.img_item_master_bottom_play_or_pause)
    ImageView mImgItemMasterBottomPlayOrPause;
    @BindView(R.id.img_item_master_skip_next)
    ImageView mImgItemMasterSkipNext;
    private VideoPlayerManager mPlayerManager;
    private SurfaceHolder mHolder;
    private boolean isPlaying = false;

    @Nullable
    @Override
    protected TitleBar setTitleBar() {
        return null;
    }

    @Override
    protected int setStatusBar() {
        return Type.StatusBar.TRANSPARENT;
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_video_player;
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
```
- SurfaceView和ControllerLayout是VideoPlayerActivity/Fragment的布局实现
>以上各司其职，共同构成了一个视频播放器，有些地方可能没解释详细，但是需要注意的点在代码中都进行了注释，有不明白的地方或其他观点请留言交流，共同进步！

后记：
>此视频播放器实现了基本功能，读者可在次基础上扩展实现其他更多功能。


