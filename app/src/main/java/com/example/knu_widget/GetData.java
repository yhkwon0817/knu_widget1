package com.example.knu_widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public static final String KEY_ANIMAL = "keyAnimal";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private SessionCallBack mSessionCallback = new SessionCallBack();
    private FirebaseFirestore mstore = FirebaseFirestore.getInstance();
    private String curDate;
    private String Uid = null;

    private Schedule newSchedule;
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

        //로그인 되어있지 않으면 로그인 액티비티로
        if(!Session.getCurrentSession().isOpened()){
            Intent intent = new Intent(GetData.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Uid = String.valueOf(result.getId());
                }
            });
        }

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
        updateData();


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
                SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
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

        //카카오톡 로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Session.getCurrentSession().checkAndImplicitOpen()) {
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            super.onSessionClosed(errorResult);
                            Log.e("###", "onSessionClosed: " + errorResult.getErrorMessage());

                        }

                        @Override
                        public void onCompleteLogout() {
                            if (mSessionCallback != null) {
                                Log.e("###", "onCompleteLogout:logout ");
                                Session.getCurrentSession().removeCallback(mSessionCallback);
                                Intent intent = new Intent(GetData.this, LogInActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
                }
            }
        });
    }

    public void updateData(){

    }
}