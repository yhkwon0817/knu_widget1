package com.example.knu_widget.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_widget.Classes.Schedule;
import com.example.knu_widget.Classes.ScheduleItemList;
import com.example.knu_widget.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private Context mContext;
    private List<Schedule> mSchedules;
    public OnItemClickListener mListener = null;

    public ScheduleAdapter(Context context, List<Schedule> schedules) {
        this.mContext = context;
        this.mSchedules = schedules;
    }

    @NonNull
    @Override
    public ScheduleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_schedule, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.MyViewHolder holder, int position) {
        Schedule schedule = mSchedules.get(position);

        holder.day.setText(schedule.getDay());
        holder.lecture.setText(schedule.getLecture());
        holder.start.setText(schedule.getStart());
        holder.end.setText(schedule.getEnd());
    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView day, lecture, start, end;
        ImageView delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.post_day);
            lecture = itemView.findViewById(R.id.post_lecture);
            start = itemView.findViewById(R.id.post_start);
            end = itemView.findViewById(R.id.post_end);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if(mListener!= null){
                            mListener.OnItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setmSchedules(List<Schedule> mSchedules) {
        this.mSchedules = mSchedules;
    }
}
