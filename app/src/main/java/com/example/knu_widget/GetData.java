package com.example.knu_widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.knu_widget.Adapter.MyAdapter;
import com.example.knu_widget.Authentic.LogInActivity;
import com.example.knu_widget.Classes.ClassTimeData;
import com.example.knu_widget.Classes.Schedule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.sdk.user.UserApiClient;

import java.util.ArrayList;
import java.util.List;


public class GetData extends AppCompatActivity {
    String[] reDirectFortune_url = {"https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EC%A5%90%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EC%86%8C%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%ED%98%B8%EB%9E%91%EC%9D%B4%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%ED%86%A0%EB%81%BC%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EC%9A%A9%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EB%B1%80%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EB%A7%90%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EC%96%91%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EC%9B%90%EC%88%AD%EC%9D%B4%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EB%8B%AD%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EA%B0%9C%EB%9D%A0%20%EC%9A%B4%EC%84%B8",
            "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EB%8F%BC%EC%A7%80%EB%9D%A0%20%EC%9A%B4%EC%84%B8"};

    private Spinner spinner1, spinner2;
    ArrayList<ClassTimeData> classInfo;
    EditText name, st_time, end_time;
    ArrayAdapter<String> arrayAdapter, dayAdapter;
    String selected_info1;
    Button add, delete, next, logout;
    String str_day, str_name, str_start, str_end;

    public static final String SHARED_PREFS = "prefs";
    private SharedPreferences preferences;
    public static final String KEY_ANIMAL = "keyAnimal";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private FirebaseFirestore mstore = FirebaseFirestore.getInstance();


    private String curDate;
    private Schedule newSchedule;
    private String Uid = null;
    private RecyclerView mPostRecyclerView;
    private List<Schedule> mDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        classInfo = new ArrayList<ClassTimeData>();
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        add = findViewById(R.id.btn_add_schedule);
        delete = findViewById(R.id.btn_delete_schedule);
        next = findViewById(R.id.btn_next_page);
        logout = findViewById(R.id.btn_logout);
        name = findViewById(R.id.name);
        st_time = findViewById(R.id.st_time);
        end_time = findViewById(R.id.end_time);


        ListView listView = (ListView) findViewById(R.id.listivew);
        MyAdapter myAdapter = new MyAdapter(this, classInfo);
        ArrayList arrayList = new ArrayList<>();
        ArrayList day = new ArrayList<>();
        arrayList.add("쥐띠");
        arrayList.add("소띠");
        arrayList.add("호랑이띠");
        arrayList.add("토끼띠");
        arrayList.add("용띠");
        arrayList.add("뱀띠");
        arrayList.add("말띠");
        arrayList.add("양띠");
        arrayList.add("원숭이띠");
        arrayList.add("닭띠");
        arrayList.add("개띠");
        arrayList.add("돼지띠");
        day.add("월요일");
        day.add("화요일");
        day.add("수요일");
        day.add("목요일");
        day.add("금요일");

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        Uid = preferences.getString("userid", null);

        if (Uid == null) {
            Log.e("###", "Uid null");
            UserApiClient.getInstance().loginWithKakaoAccount(GetData.this, (oAuthToken, throwable) -> {
                if (throwable != null) {
                    Log.e("###", "로그인 실패 : " + throwable);
                } else {
                    requestMe();
                }
                return null;
            });
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().unlink(throwable -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userid", null);
                    editor.apply();
                    Intent intent = new Intent(GetData.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                    return null;
                });
            }
        });

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setAdapter(arrayAdapter);
        dayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, day);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(dayAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_info1 = spinner1.getSelectedItem().toString();
                //Log.e("###", String.valueOf(arrayList.indexOf(selected_info1)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "띠를 골라 주십시오", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setAdapter(myAdapter);

        newSchedule = new Schedule();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classInfo.add(new ClassTimeData(spinner2.getSelectedItem().toString(), name.getText().toString(), st_time.getText().toString(), end_time.getText().toString()));
                listView.setAdapter(myAdapter);

                curDate = String.valueOf(System.currentTimeMillis());
                newSchedule.setDay(spinner2.getSelectedItem().toString());
                newSchedule.setLecture(name.getText().toString());
                newSchedule.setStart(st_time.getText().toString());
                newSchedule.setEnd(end_time.getText().toString());

                mstore.collection(Uid).document(curDate)
                        .set(newSchedule)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.e("###", "업로드 성공");
                            }
                        });

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classInfo.remove(classInfo.size() - 1);
                listView.setAdapter(myAdapter);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(KEY_ANIMAL, reDirectFortune_url[arrayList.indexOf(selected_info1)]);
                editor.apply();

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);

                Log.e("###", reDirectFortune_url[arrayList.indexOf(selected_info1)]);
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });

    }

    private void requestMe() {
        UserApiClient.getInstance().me(((user, throwable) -> {
            if(throwable!=null){
                Log.e("###", "사용자 정보 요청 실패"+throwable);
            }else{
                String Uid = String.valueOf(user.getId());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userid", Uid);
                editor.apply();
            }
            return null;
        }));
    }
}