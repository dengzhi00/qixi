package com.qixi.pingbao;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.qixi.R;
import com.qixi.util.SharUtil;
import com.qixi.view.CoverView;
import com.qixi.view.HuaPingView;
import com.qixi.view.InsetsPercentRelativeLayout;

/**
 * Created by 83642 on 2016/8/8.
 */
public class PingbaoActivity extends Activity  {

    public static final int MSG_LAUNCH_HOME = 100;

    private HuaPingView huapingview;

    private CoverView coverView;

    private InsetsPercentRelativeLayout relativeLayout;

    private static int[] GIF_JPG = {R.mipmap.qixim1 , R.mipmap.qixim2, R.mipmap.qixim3,
            R.mipmap.qixim4};



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_LAUNCH_HOME)
                finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_suoping);

        huapingview = (HuaPingView) findViewById(R.id.huapingview);
        huapingview.setMainHandler(handler);
        coverView = huapingview.getCoverView();
        relativeLayout = huapingview.getInsetsPercentRelativeLayout();
        coverView.setCallbacks(new CoverView.Callbacks() {
            @Override
            public void onStopAnimation() {

            }
        });
        coverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
        getBrodCast();
    }

    private void getBrodCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PingBaoService.ZHIWEN_ACTION);
        intentFilter.addAction(PingBaoService.GIF_ACTION);
        intentFilter.addAction(PingBaoService.ON_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        stop();
    }

    private void stop(){
        if(coverView.isRunning()){
            coverView.stop();
        }else{
            coverView.start();
        }
        Intent intent = new Intent();
        intent.setAction(PingBaoService.MDIA_STOP);
        sendBroadcast(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action){
                case PingBaoService.ZHIWEN_ACTION:
                    coverView.stop();
                    finish();
                    break;
                case  PingBaoService.GIF_ACTION:
                    coverView.stop();
                    int index = SharUtil.getGif(context);
                    relativeLayout.setBackgroundResource(GIF_JPG[index]);
                    if (index == GIF_JPG.length - 1) {
                        SharUtil.recevieGif(0, context);
                    } else {
                        SharUtil.recevieGif(index + 1, context);
                    }
                    break;
                case PingBaoService.ON_ACTION:
                    coverView.start();
                    break;
            }
        }
    };

}
