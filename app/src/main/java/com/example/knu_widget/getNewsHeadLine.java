package com.example.knu_widget;

import android.appwidget.AppWidgetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class getNewsHeadLine extends AsyncTask<String, String, String> {

    private RemoteViews views;
    private int widgetId;
    private AppWidgetManager appWidgetManager;

    public getNewsHeadLine(RemoteViews views, int appWidgetId, AppWidgetManager appWidgetManager){
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
            Document news_Doc = Jsoup.connect(url).get();
            Elements news_elements = news_Doc.getElementsByAttributeValue("class","rankingnews_list");
            int num = 0;
            ArrayList<String> News_headlines = new ArrayList<>();
            for(Element ele : news_elements){
                Elements aElements = ele.select("a");
                String article_url = aElements.attr("href");

                Document subDoc = Jsoup.connect(article_url).get();
                Elements sub_ele = subDoc.getElementsByAttributeValue("class","media_end_head_title");
                News_headlines.add(sub_ele.text());
                if(num++ == 3) break;
                return News_headlines.toString();
            }
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
        views.setTextViewText(R.id.text_news, s);
        appWidgetManager.updateAppWidget(widgetId, views);
    }
}
