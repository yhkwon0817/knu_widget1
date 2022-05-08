package com.example.knu_widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("###","onUpdate executed");
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            Calendar cal = Calendar.getInstance();
            remoteViews.setTextViewText(R.id.time_widget_date_textview, cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.time_widget_date_textview, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getStringExtra("mode")!=null){
            Calendar current = Calendar.getInstance();
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.time_widget_date_textview, current.get(Calendar.HOUR_OF_DAY)+":"+current.get(Calendar.MINUTE));

            Log.e("###","onReceived executed time: "+ current.get(Calendar.HOUR_OF_DAY)+":"+current.get(Calendar.MINUTE));

            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(new ComponentName(context, WidgetProvider.class),remoteViews);
        }else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.putExtra("mode", "time");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);
        Log.e("###","onEnabled executed");
    }

    @Override
    public void onDisabled(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WidgetProvider.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.e("###","onDisabled executed");
    }
}
