package com.imooc.project.com.imooc.project.view;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imooc.project.R;
import com.imooc.project.com.imooc.project.model.Record;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */

public class RecordAdpater extends ArrayAdapter<Record> {

    private Context mContext;

    private List<Record> mDatas;
    private int mMinItemWidth;
    private int mMaxItemWidth;

    private LayoutInflater mInflater;

    public RecordAdpater(Context context, List<Record> datas) {
        super(context, -1, datas);
        mContext = context;
        mDatas = datas;
        WindowManager manager = (WindowManager) mContext.getSystemService(Service.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mMaxItemWidth = (int) (metrics.widthPixels*0.7f);
        mMinItemWidth = (int) (metrics.widthPixels*0.15f);
        mInflater = LayoutInflater.from(mContext);
    }

    private class ViewHolder {
        TextView seconds;
        View length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_recorder,parent,false);
            holder = new ViewHolder();
            holder.seconds = (TextView) convertView.findViewById(R.id.recorder_time);
            holder.length = convertView.findViewById(R.id.recorder_length);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.seconds.setText(Math.round(getItem(position).getTime())+"s");
        ViewGroup.LayoutParams layoutParams = holder.length.getLayoutParams();
        layoutParams.width = (int) (mMinItemWidth + mMaxItemWidth/60f*getItem(position).getTime());
        return convertView;
    }
}
