package com.qixi.pingbao;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.qixi.R;

import java.io.IOException;

/**
 * Created by 83642 on 2016/8/8.
 */
public class PingBaoService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    private KeyguardManager km;

    public static final String ZHIWEN_ACTION = "com.zhiwen.coms";

    public static final String GIF_ACTION = "com.gif.coms";

    public static final String ON_ACTION = "com.on.coms";

    public static final String MDIA_STOP = "com.mstop.coms";

    public static final String FILE_MUSIC = "com.file.coms";

    private MediaPlayer mediaPlayer;

    private boolean isPath = true;

    private boolean isPrepar = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startSuoping();
        km = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
        initPlayer(R.raw.qh);
    }

    private void startSuoping() {
        IntentFilter intentFilter = new IntentFilter();
        //闭屏广播
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        //锁屏解锁广播
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        //亮屏广播
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);

        intentFilter.addAction(MDIA_STOP);
        intentFilter.addAction(FILE_MUSIC);
        registerReceiver(mScreenOffReceiver, intentFilter);
    }

    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {//开启屏保
                Intent intent1 = new Intent();
                intent1.setAction(GIF_ACTION);
                context.sendBroadcast(intent1);

                Intent mLockIntent = new Intent(context, PingbaoActivity.class);
                mLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(mLockIntent);
                if(null != mediaPlayer){
                    if(mediaPlayer.isPlaying())
                        pause();
                }
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {//解锁
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (km.isKeyguardSecure()) {
                        Intent intent1 = new Intent();
                        intent1.setAction(ZHIWEN_ACTION);
                        context.sendBroadcast(intent1);

                        if(null != mediaPlayer){
                            if(mediaPlayer.isPlaying())
                                pause();
                        }
                    }
                }

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Intent intent1 = new Intent();
                intent1.setAction(ON_ACTION);
                context.sendBroadcast(intent1);
                if(isPrepar)
                    start();
            } else if(intent.getAction().equals(MDIA_STOP)){
                if(mediaPlayer.isPlaying())
                    pause();
                else
                    start();
            } else if(intent.getAction().equals(FILE_MUSIC)){
                String path = intent.getStringExtra("path");
                initPlayer(path);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenOffReceiver);
        exit();
    }

    /**
     * 播放 音频
     *
     * @param url
     */
    public void initPlayer(String url) {
        try {
            if (!isPath) {
                mediaPlayer.reset();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(true);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnBufferingUpdateListener(this);
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setVolume(1.0f, 1.0f);
            } else {
                if (null != mediaPlayer)
                    mediaPlayer.reset();
                if (null == mediaPlayer) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnBufferingUpdateListener(this);
                }
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setVolume(1.0f, 1.0f);
            }
            isPath = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放 音频
     *
     * @param url
     */
    public void initPlayer(int url) {
        isPath = false;
        isPrepar = false;
        if(null != mediaPlayer)
            mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, url);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setLooping(true);
    }


    public void pause() {
        mediaPlayer.pause();
    }

    public void start(){
        mediaPlayer.start();
    }

    /**
     * 清缓存
     */
    public void exit() {
        if (null != mediaPlayer) {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
         mediaPlayer.start();
        isPrepar = true;
    }
}
