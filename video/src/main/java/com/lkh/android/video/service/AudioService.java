package com.lkh.android.video.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * 后台播放音乐
 */
public class AudioService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private AudioBinder mBinder = new AudioBinder();
    private MediaPlayer mMediaPlayer;
    /**
     * 歌曲目录地址列表
     */
    private String[] mPaths = new String[0];
    /**
     * 当前播放的第几首
     */
    private int mCurrent;
    /**
     * 音量0-1之间
     */
    private float mVolume;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mVolume = 0.8f;
        mMediaPlayer.setVolume(mVolume, mVolume);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();//释放资源
            mMediaPlayer = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 播放完成调用
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    /**
     * 准备完成调用
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }

    /**
     * 加载歌曲目录
     * @param paths
     */
    public void load(String[] paths, int position){
        mPaths = paths;
        mCurrent = position;
    }

    /**
     * 上一首
     */
    public void previous(){

        mCurrent--;
        if (mCurrent < 0){
            mCurrent = mPaths.length-1;
        }
        if (mCurrent < 0){
            mCurrent = 0;
        }
        startPlay();
    }

    /**
     * 下一首
     */
    public void next(){
        mCurrent++;
        if (mCurrent >= mPaths.length){
            mCurrent = 0;//循环
        }
        startPlay();
    }


    /**
     * 开始播放
     */
    public void startPlay(){
        if (mCurrent<0 || mCurrent>=mPaths.length){
            return;
        }
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }

        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            //http://139.227.43.143:8080/strut/DownLoadFileServlet?path=E:&curfile=a
            Log.i("path=", mPaths[mCurrent]);
            mMediaPlayer.setDataSource(mPaths[mCurrent]);

//            mMediaPlayer.setDataSource("http://139.227.43.143:8080/strut/DownLoadFileServlet?path=E:&curfile=a");
            //异步准备播放，准备好了直接调onPrepared()方法去播放
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停或恢复播放
     * 如果正在播放则暂停
     * 如果是暂停状态则恢复播放
     */
    public void pauseOrStart(){
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }else {
            mMediaPlayer.start();
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay(){
        mMediaPlayer.stop();
    }

    public void addVolume(){
        mVolume = mVolume + 0.1f;
        if (mVolume > 1.0f){
            mVolume = 1.0f;
        }
        mMediaPlayer.setVolume(mVolume, mVolume);
    }

    public void subVolume(){
        mVolume = mVolume - 0.1f;
        if (mVolume < 0.0f){
            mVolume = 0.0f;
        }
        mMediaPlayer.setVolume(mVolume, mVolume);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    /**
     * 绑定service用
     */
    public class AudioBinder extends Binder {
        /**
         * 获取绑定的Service
         * @return 被绑定的service
         */
        public Service getService(){
            return AudioService.this;
        }
    }
}
