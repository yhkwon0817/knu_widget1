package com.example.knu_widget;

import static android.location.LocationManager.GPS_PROVIDER;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button btn;
    String[] reDirectFortune_url = new String[12];
    String Fortune_url = "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EB%9D%A0%EB%B3%84%20%EC%9A%B4%EC%84%B8";

    String News_url = "https://news.naver.com/main/ranking/popularDay.naver";
    String URL = "https://m.search.naver.com/search.naver?sm=mtp_hty.top&where=m&query=%EC%84%9C%EC%9A%B8+%EB%82%A0%EC%94%A8";
    ArrayList<String> News_headlines = new ArrayList<>();

    String Corona_url ="https://search.naver.com/search.naver?where=nexearch&sm=top_sug.pre&fbm=1&acr=1&acq=%EC%8B%A0%EA%B7%9C%ED%99%95%EC%A7%84%EC%9E%90&qdt=0&ie=utf8&query=%EC%BD%94%EB%A1%9C%EB%82%98+%EC%8B%A0%EA%B7%9C+%ED%99%95%EC%A7%84%EC%9E%90";
    String Corona_num = "";

    String Weather_url ="https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    String weather_url ="";
    double longitude,latitude;
    String currDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text1);
        btn = findViewById(R.id.btn);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //위치계산
                if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) { ActivityCompat.requestPermissions( MainActivity.this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0 ); }
                else {
                    Location loc_Current = locationManager.getLastKnownLocation(GPS_PROVIDER);
                    String provider = loc_Current.getProvider();
                    longitude = loc_Current.getLongitude();
                    latitude = loc_Current.getAltitude();
                }
                //날짜계산
                Date currTime = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                currDate = format.format(currTime);

                new doinbackground().execute();
            }
        });

    }
    public class doinbackground extends AsyncTask<Void,Void,Void>
    {
        String str="";
        ProgressDialog dialog;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EB%9D%A0%EB%B3%84%20%EC%9A%B4%EC%84%B8").get();
                Elements Fortune_ele = doc.select("div.api_cs_wrap ul li dt a");
                int Fortune_eleSize = Fortune_ele.size();
                int num = 0;
                for(Element ele : Fortune_ele ){
                    reDirectFortune_url[num] = ele.absUrl("href");
                    num++;
                }

                //헤드라인 제목 긁어오기
                Document news_Doc = Jsoup.connect(News_url).get();
                Elements news_elements = news_Doc.getElementsByAttributeValue("class","rankingnews_list");
                num = 0;
                for(Element ele : news_elements){
                    Elements aElements = ele.select("a");
                    String article_url = aElements.attr("href");

                    Document subDoc = Jsoup.connect(article_url).get();
                    Elements sub_ele = subDoc.getElementsByAttributeValue("class","media_end_head_title");
                    News_headlines.add(sub_ele.text());
                    if(num++ == 3) break;
                }
                Log.e("###",News_headlines.toString());

                //코로나 확진자 수 긁어오기
                Document corona_Doc = Jsoup.connect(Corona_url).get();
                Elements ele = corona_Doc.getElementsByAttributeValue("class","info_num");
                for(Element e : ele){
                    Corona_num = e.text();
                    break;
                }


                Log.e("###","코로나 확진자수는?  "+Corona_num);

                String serviceKey = "e4l%2BmnqXJB8BUNJaCVmUAhrTGT8Fh%2FidDa7uScih%2FDUr1IaDRmdOGBQtGM2zThwStYcT7RUnMrDJwwZsd1QeEg%3D%3D";
                String nx = String.format("%.0f",latitude);
                String ny = String.format("%.0f",longitude);
                String baseDate = currDate;
                String baseTime = "0500";
                String type = "XML";

                Log.e("###",nx +" "+ ny +" "+ baseDate +" "+ baseTime +" "+ type);

                 weather_url = Weather_url +
                       "?serviceKey="+serviceKey+
                       "&pageNo="+"1"+
                       "&numOfRows="+"1000"+
                       "&dataType="+type+
                       "&base_date="+"20220411"+
                       "&base_time="+baseTime+
                       "&nx="+nx+
                       "&ny="+ny;

                 java.net.URL url = new URL(weather_url);
                 InputStream is = url.openStream();

                 XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                 XmlPullParser xpp = parserCreator.newPullParser();
                 xpp.setInput(new InputStreamReader(is,"UTF-8"));

                 String tag;
                 StringBuffer buffer = new StringBuffer();

                 xpp.next();
                 int eventType = xpp.getEventType();







            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            dialog.dismiss();
            textView.setText(str);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog= new ProgressDialog(MainActivity.this);
            dialog.setTitle("정보를 가져오는 중입니다.");
            dialog.setMessage("Please Wait Loading...");
            dialog.show();
        }
    }

}