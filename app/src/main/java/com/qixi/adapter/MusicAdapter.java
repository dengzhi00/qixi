package com.qixi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qixi.R;
import com.qixi.dto.MusicDto;

import java.util.List;

/**
 * Created by 83642 on 2016/8/10.
 */
public class MusicAdapter extends BaseAdapter{
    private List<MusicDto> list;

    private Context context;

    public MusicAdapter(List<MusicDto> list , Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(null == list)
        return 0;
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(null == view){
            view = LayoutInflater.from(context).inflate(R.layout.adapter_music,viewGroup,false);
            holder = new ViewHolder();
            holder.ada_textview = (TextView) view.findViewById(R.id.ada_textview);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.ada_textview.setText(list.get(i).getName());
        return view;
    }

    class ViewHolder{
        private TextView ada_textview;
    }
}
