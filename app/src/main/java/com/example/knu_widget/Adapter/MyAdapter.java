package com.example.knu_widget.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.knu_widget.Classes.ClassTimeData;
import com.example.knu_widget.R;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ClassTimeData> sample;

    public MyAdapter(Context context, ArrayList<ClassTimeData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public ClassTimeData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View coverView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.classdata, null);

        TextView classDay = (TextView) view.findViewById(R.id.ClassDay);
        TextView className = (TextView) view.findViewById(R.id.ClassName);
        TextView classStartTime = (TextView) view.findViewById(R.id.ClassStartTime);
        TextView classEndTime = (TextView) view.findViewById(R.id.ClassEndTime);

        classDay.setText(sample.get(position).getDay());
        className.setText(sample.get(position).getClassname());
        classStartTime.setText(sample.get(position).getStartTime());
        classEndTime.setText(sample.get(position).getEndTime());

        return view;
    }
}
