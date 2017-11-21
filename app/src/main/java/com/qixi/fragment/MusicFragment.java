package com.qixi.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qixi.R;
import com.qixi.adapter.MusicAdapter;
import com.qixi.dto.MusicDto;
import com.qixi.pingbao.PingBaoService;
import com.qixi.util.MusicFileUtil;

/**
 * Created by 83642 on 2016/8/10.
 */
public class MusicFragment extends DialogFragment implements AdapterView.OnItemClickListener{

    private ListView mlistview;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.main);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);
        mlistview = (ListView)dialog.findViewById(R.id.mlistview);
        mlistview.setAdapter(new MusicAdapter(MusicFileUtil.getInstace().getList(),getActivity()));
        mlistview.setOnItemClickListener(this);
        return dialog;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MusicAdapter adapter = (MusicAdapter) adapterView.getAdapter();
        MusicDto mus = (MusicDto) adapter.getItem(i);
        Intent intent = new Intent();
        intent.putExtra("path",mus.getPath());
        intent.setAction(PingBaoService.FILE_MUSIC);
        getActivity().sendBroadcast(intent);
        dismiss();
    }
}
