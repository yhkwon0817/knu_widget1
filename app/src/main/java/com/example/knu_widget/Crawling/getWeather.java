package com.example.knu_widget.Crawling;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.knu_widget.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class getWeather extends AsyncTask<String, String, String> {

    private RemoteViews views;
    private int widgetId;
    private AppWidgetManager appWidgetManager;

    public getWeather(RemoteViews views, int appWidgetId, AppWidgetManager appWidgetManager) {
        this.views = views;
        this.widgetId = appWidgetId;
        this.appWidgetManager = appWidgetManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        try {
            Document weather_Doc = Jsoup.connect(url).get();
            Log.e("###", "날씨 시작");
            Elements ele = weather_Doc.getElementsByAttributeValue("class","current");
            String current_temp = "";
            for (Element e : ele) {
                current_temp = e.text();
                break;
            }
            Log.e("###", "날씨 성공" + current_temp);
            return current_temp;
        } catch (Exception e) {
            Log.e("###", "getting Corona number failed " + url + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (isCancelled()) {
            s = null;
        }
        views.setTextViewText(R.id.text_weather, s);
        appWidgetManager.updateAppWidget(widgetId, views);
    }
}