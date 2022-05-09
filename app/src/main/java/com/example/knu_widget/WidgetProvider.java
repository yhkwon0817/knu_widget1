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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        String currDate;

        Calendar cal = Calendar.getInstance();
        views.setTextViewText(R.id.time_widget_date_textview, cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));

        Date currTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        currDate = format.format(currTime);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("###","onUpdate executed");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getStringExtra("mode")!=null){
            Calendar current = Calendar.getInstance();
            AppWidgetManager manager = AppWidgetManager.getInstance(context);

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
            int[] appWidgets = manager.getAppWidgetIds(thisAppWidget);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.time_widget_date_textview, current.get(Calendar.HOUR_OF_DAY)+":"+current.get(Calendar.MINUTE));
            Log.e("###","onReceived executed time: "+ current.get(Calendar.HOUR_OF_DAY)+":"+current.get(Calendar.MINUTE));

            String Corona_url ="https://search.naver.com/search.naver?where=nexearch&sm=top_sug.pre&fbm=1&acr=1&acq=%EC%8B%A0%EA%B7%9C%ED%99%95%EC%A7%84%EC%9E%90&qdt=0&ie=utf8&query=%EC%BD%94%EB%A1%9C%EB%82%98+%EC%8B%A0%EA%B7%9C+%ED%99%95%EC%A7%84%EC%9E%90";
            new getCoronaNumber(remoteViews, appWidgets[0], manager).execute(Corona_url);
            String News_url = "https://news.naver.com/main/ranking/popularDay.naver";
            new getNewsHeadLine(remoteViews, appWidgets[0], manager).execute(News_url);

            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, WidgetProvider.class), remoteViews);
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
