package com.qixi.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.qixi.dto.MusicDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 83642 on 2016/8/10.
 */
public class MusicFileUtil {

    private static MusicFileUtil musicFileUtil;

    private List<MusicDto> list;

    public synchronized static MusicFileUtil getInstace(){
        if(null == musicFileUtil){
            musicFileUtil = new MusicFileUtil();
        }
        return musicFileUtil;
    }

    private MusicFileUtil(){
        list = new ArrayList<>();
    }

    public void init(Context context){
        list.addAll(queryFiles(context));
    }

    public List<MusicDto> getList() {
        return list;
    }

    /**
     * 查询SD卡里可以上传的文档
     */
    public  List<MusicDto> queryFiles(Context context){
        List<MusicDto> list = new ArrayList<>();
        String[] projection = new String[] { MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " like ?",
                new String[]{"%.mp3"},
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                int idindex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int sizeindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                do {
                    MusicDto musicDto = new MusicDto();
                    String id = cursor.getString(idindex);
                    String path = cursor.getString(dataindex);
                    String size = cursor.getString(sizeindex);

                    int dot=path.lastIndexOf("/");
                    String name=path.substring(dot+1);
                    musicDto.setPath(path);
                    musicDto.setSize(size);
                    musicDto.setName(name);
                    Log.e("test",path);
                    list.add(musicDto);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

}
