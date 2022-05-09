package com.example.knu_widget;

import android.appwidget.AppWidgetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class getCoronaNumber extends AsyncTask<String, String, String> {

    private RemoteViews views;
    private int widgetId;
    private AppWidgetManager appWidgetManager;

    public getCoronaNumber(RemoteViews views, int appWidgetId, AppWidgetManager appWidgetManager){
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
            Document corona_Doc = Jsoup.connect(url).get();
            Elements ele = corona_Doc.getElementsByAttributeValue("class","info_num");
            String Corona_num = "";
            for(Element e : ele){
                Corona_num = e.text();
                break;
            }
            return Corona_num;
        }catch (Exception e){
            Log.e("###","getting Corona number failed"+e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(isCancelled()){
            s=null;
        }
        views.setTextViewText(R.id.text_corona, s);
        appWidgetManager.updateAppWidget(widgetId, views);
    }
}
