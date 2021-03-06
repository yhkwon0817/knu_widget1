package com.example.knu_widget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.knu_widget.Adapter.ScheduleAdapter;
import com.example.knu_widget.Authentic.LogInActivity;
import com.example.knu_widget.Classes.Schedule;
import com.example.knu_widget.Classes.ScheduleItemList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kakao.sdk.user.UserApiClient;

import java.util.ArrayList;
import java.util.List;


public class GetData extends AppCompatActivity implements ScheduleAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
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
    private String day, curDate, Uid = null;
    private int animal;
    EditText name;
    TextView st_time, end_time;
    Button add, next, logout;
    ImageView start, end;
    private int st_hour = 0, st_minute = 0, end_hour = 0, end_minute = 0;

    public static final String SHARED_PREFS = "prefs";
    private SharedPreferences preferences;
    public static final String KEY_ANIMAL = "keyAnimal";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private FirebaseFirestore mstore = FirebaseFirestore.getInstance();
    private Schedule newSchedule;

    RecyclerView recyclerView;
    List<Schedule> schedules;
    ScheduleAdapter scheduleAdapter;
    ScheduleItemList scheduleItemList = ScheduleItemList.getInstance();
    Dialog delete_dialog;
    Schedule de_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        Uid = preferences.getString("userid", null);
        constructor();
        set_onClickListener();
        logInCheck();

        //recyclerView.setHasFixedSize(true);
        EventChangeListener();
        delete_dialog = new Dialog(this);
        delete_dialog.setContentView(R.layout.dialog_delete_schedule);
        delete_dialog.setCanceledOnTouchOutside(true);
        delete_dialog.findViewById(R.id.complete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mstore.collection(Uid).document(de_schedule.getWriteId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                EventChangeListener();
                                delete_dialog.dismiss();
                            }
                        });
            }
        });
        delete_dialog.findViewById(R.id.dialog_cancelbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_dialog.dismiss();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ScheduleAdapter(GetData.this, schedules);
        scheduleAdapter.setOnClickListener(this);
        recyclerView.setAdapter(scheduleAdapter);
        newSchedule = new Schedule();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate = String.valueOf(System.currentTimeMillis());
                newSchedule.setDay(spinner2.getSelectedItem().toString());
                newSchedule.setLecture(name.getText().toString());
                newSchedule.setStart(st_time.getText().toString());
                newSchedule.setEnd(end_time.getText().toString());
                newSchedule.setWriteId(curDate);

                if (newSchedule.getDay().equals("") || newSchedule.getLecture().equals("") || newSchedule.getStart().equals("") || newSchedule.getEnd().equals("")) {
                    Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                mstore.collection(Uid).document(curDate)
                        .set(newSchedule)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.e("###", "????????? ??????");
                                EventChangeListener();
                                name.setText("");
                            }
                        });

            }
        });//????????? ????????????

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(KEY_ANIMAL, reDirectFortune_url[animal]);
                editor.apply();

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void EventChangeListener() {
        schedules = new ArrayList<>();
        mstore.collection(Uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task != null) {
                    schedules.clear();
                    Log.e("###", "?????? ??????: " + task.getResult().getDocuments().size());
                    for (DocumentSnapshot snap : task.getResult().getDocuments()) {
                        schedules.add(snap.toObject(Schedule.class));
                    }
                    scheduleItemList.setScheduleList(schedules);
                    scheduleItemList.sort();
                    scheduleAdapter.setmSchedules(scheduleItemList.getScheduleList());
                    scheduleAdapter.notifyDataSetChanged();
                }
            }
        });
    }//recyclerview ??? ??????????????? ??????

    private void logInCheck() {
        if (Uid == null) {
            Log.e("###", "Uid null");
            UserApiClient.getInstance().loginWithKakaoAccount(GetData.this, (oAuthToken, throwable) -> {
                if (throwable != null) {
                    Log.e("###", "????????? ?????? : " + throwable);
                } else {
                    requestMe();
                }
                return null;
            });
        } else {
            Log.e("###", Uid);
        }
    }//????????? ????????? ??????????????? ?????? ???????????? ????????? ????????? ??? ?????????

    private void requestMe() {
        UserApiClient.getInstance().me(((user, throwable) -> {
            if (throwable != null) {
                Log.e("###", "????????? ?????? ?????? ??????" + throwable);
            } else {
                String Uid = String.valueOf(user.getId());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userid", Uid);
                editor.apply();
            }
            return null;
        }));
    }//????????? ????????? ???

    private void constructor() {
        name = findViewById(R.id.name);
        st_time = findViewById(R.id.st_time);
        end_time = findViewById(R.id.end_time);
        start = findViewById(R.id.btn_start);
        end = findViewById(R.id.btn_end);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        add = findViewById(R.id.btn_add_schedule);
        next = findViewById(R.id.btn_next_page);
        logout = findViewById(R.id.btn_logout);

        recyclerView = findViewById(R.id.schedule_list_recycle);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                animal = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });//??? ?????????
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });//?????? ?????????

    }//?????? ????????????

    private void set_onClickListener() {
        start.setOnClickListener(this);
        end.setOnClickListener(this);
        logout.setOnClickListener(this);
    }//?????? ????????? ??????

    @Override
    public void onClick(View view) {
        TimePickerDialog timePickerDialog;
        switch (view.getId()) {
            case R.id.btn_start:
                timePickerDialog =
                        new TimePickerDialog(GetData.this, android.R.style.Theme_Holo_Light_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                        if (minute < 10) {
                                            st_time.setText(hour + " : 0" + minute);
                                        } else {
                                            st_time.setText(hour + " : " + minute);
                                        }
                                    }
                                }, st_hour, st_minute, false);
                timePickerDialog.show();
                break;
            case R.id.btn_end:
                timePickerDialog =
                        new TimePickerDialog(GetData.this, android.R.style.Theme_Holo_Light_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                        if (minute < 10) {
                                            end_time.setText(hour + " : 0" + minute);
                                        } else {
                                            end_time.setText(hour + " : " + minute);
                                        }
                                    }
                                }, end_hour, end_minute, false);
                timePickerDialog.show();
                break;
            case R.id.btn_logout:
                UserApiClient.getInstance().unlink(throwable -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userid", null);
                    editor.apply();
                    Intent intent = new Intent(GetData.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                    return null;
                });
                break;
        }
    }//??????????????? -> switch ?????????

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void OnItemClick(View v, int position) {
        Log.e("###", "??? ????????? ?????? ?????????");
        de_schedule =scheduleItemList.getScheduleList().get(position);
        delete_dialog.show();
    }
}