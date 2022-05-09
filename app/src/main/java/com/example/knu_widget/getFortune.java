package com.example.knu_widget;

import android.appwidget.AppWidgetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class getFortune extends AsyncTask<String,String,String> {

    private RemoteViews views;
    private int widgetId;
    private AppWidgetManager appWidgetManager;

    public getFortune(RemoteViews views, int appWidgetId, AppWidgetManager appWidgetManager){
        this.views=views;
        this.widgetId=appWidgetId;
        this.appWidgetManager=appWidgetManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        try{
            Log.e("###", "Crawling Starts...");
            Document fortune_Doc = Jsoup.connect(url).get();
            Elements ele = fortune_Doc.getElementsByAttributeValue("class","text _cs_fortune_text");
            String todayFortune = "";
            for(Element e : ele){
                todayFortune = e.text();
                break;
            }
            Log.e("###", "Crawling Value - "+todayFortune);
            return todayFortune;
        }catch (Exception e){
            Log.e("###","getting Today Fortune failed"+e.getMessage());
            Log.e("###","" + url);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(isCancelled()){
            s=null;
        }
        views.setTextViewText(R.id.text_luck, s);
        appWidgetManager.updateAppWidget(widgetId, views);
    }
}
