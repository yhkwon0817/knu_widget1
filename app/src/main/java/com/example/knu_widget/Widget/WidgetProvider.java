package com.example.knu_widget.Widget;

import static android.content.Context.MODE_PRIVATE;
import static com.example.knu_widget.GetData.KEY_ANIMAL;
import static com.example.knu_widget.GetData.SHARED_PREFS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.knu_widget.Crawling.getWeather;
import com.example.knu_widget.R;
import com.example.knu_widget.Crawling.getCoronaNumber;
import com.example.knu_widget.Crawling.getFortune;
import com.example.knu_widget.Crawling.getNewsHeadLine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        //날씨 부분
        //날씨 넣기 전에 월 일을 입력해 놓았다.
        Calendar cal = Calendar.getInstance();
        views.setTextViewText(R.id.time_widget_date_textview, cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        String currDateMonth, currDateDay;
        Date currTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat formatDay = new SimpleDateFormat("dd", Locale.getDefault());
        currDateMonth = formatMonth.format(currTime);
        currDateDay = formatDay.format(currTime);
        views.setTextViewText(R.id.text_month, currDateMonth);
        views.setTextViewText(R.id.text_day, currDateDay);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.e("###","upDateAppWidget executed");
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
        if(intent.getStringExtra("mode")!=null){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
            int[] appWidgets = manager.getAppWidgetIds(thisAppWidget);

            //날씨
            Calendar current = Calendar.getInstance();
            remoteViews.setTextViewText(R.id.time_widget_date_textview, current.get(Calendar.HOUR_OF_DAY)+":"+current.get(Calendar.MINUTE));
            String currDateMonth, currDateDay;
            Date currTime = Calendar.getInstance().getTime();
            SimpleDateFormat formatMonth = new SimpleDateFormat("MM", Locale.getDefault());
            SimpleDateFormat formatDay = new SimpleDateFormat("dd", Locale.getDefault());
            currDateMonth = formatMonth.format(currTime);
            currDateDay = formatDay.format(currTime);
            remoteViews.setTextViewText(R.id.text_month, currDateMonth);
            remoteViews.setTextViewText(R.id.text_day, currDateDay);

            //코로나 확진자 수
            String Corona_url ="https://search.naver.com/search.naver?where=nexearch&sm=tab_jum&query=%EC%BD%94%EB%A1%9C%EB%82%98+%EC%8B%A0%EA%B7%9C+%ED%99%95%EC%A7%84%EC%9E%90";
            new getCoronaNumber(remoteViews, appWidgets[0], manager).execute(Corona_url);

            //뉴스 헤드라인
            String News_url = "https://news.naver.com/main/ranking/popularDay.naver";
            new getNewsHeadLine(remoteViews, appWidgets[0], manager).execute(News_url);

            //오늘의 행운
            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            String Fortune_url = prefs.getString(KEY_ANIMAL , "Press me");
            new getFortune(remoteViews, appWidgets[0], manager).execute(Fortune_url);

            //오늘의 날씨
            String Weather_url = "https://weather.naver.com/";
            new getWeather(remoteViews, appWidgets[0], manager).execute(Weather_url);

            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, WidgetProvider.class), remoteViews);
        }else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.putExtra("mode", "time");

        //최신버전의 기기들에 호환
        PendingIntent pendingIntent;
        if( Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            pendingIntent=PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        }
        else{
            pendingIntent=PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        //주기적 업데이트
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 6000, pendingIntent);
        Log.e("###","onEnabled executed");
    }

    @Override
    public void onDisabled(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WidgetProvider.class);

        //최신버전의 기기들에 호환
        PendingIntent pendingIntent;
        if( Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            pendingIntent=PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        }
        else{
            pendingIntent=PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.e("###","onDisabled executed");
    }
}
