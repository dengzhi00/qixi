package com.qixi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.qixi.adapter.MusicAdapter;
import com.qixi.dto.MusicDto;
import com.qixi.fragment.MusicFragment;
import com.qixi.pingbao.PingBaoService;
import com.qixi.util.MusicFileUtil;
import com.qixi.view.CoverView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    private LinearLayout relativelayout_music_bd;

    private CoverView mcover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MusicFileUtil.getInstace().init(this);
        intent = new Intent(this,PingBaoService.class);
        startService(intent);
        relativelayout_music_bd = (LinearLayout) findViewById(R.id.relativelayout_music_bd);
        relativelayout_music_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicFragment musicFragment = new MusicFragment();
                musicFragment.show(getSupportFragmentManager(),"musicFragment");
            }
        });

        mcover = (CoverView) findViewById(R.id.mcover);
        mcover.setCallbacks(new CoverView.Callbacks() {
            @Override
            public void onStopAnimation() {
            }
        });
        mcover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mcover.isRunning()){

                    mcover.stop();
                }else{
                    mcover.start();
                }
                Intent intent = new Intent();
                intent.setAction(PingBaoService.MDIA_STOP);
                sendBroadcast(intent);
            }
        });
        mcover.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(intent);
    }
}
