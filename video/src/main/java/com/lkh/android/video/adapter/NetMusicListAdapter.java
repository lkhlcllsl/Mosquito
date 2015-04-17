package com.lkh.android.video.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by kk on 2015/2/1.
 */
public class NetMusicListAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mFileNames;

    public NetMusicListAdapter(Context context, String[] mFileNames){
        this.mContext = context;
        this.mFileNames = mFileNames;
    }

    @Override
    public int getCount() {
        return mFileNames.length;
    }

    @Override
    public Object getItem(int position) {
        return mFileNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (convertView == null){
            textView = new TextView(mContext);
            convertView = textView;
            convertView.setTag(textView);
        }else {
            textView = (TextView) convertView.getTag();
        }

        textView.setText(mFileNames[position]);
        return convertView;
    }
}
