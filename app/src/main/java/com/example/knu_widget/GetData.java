package com.example.knu_widget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.select.Evaluator;

import java.util.ArrayList;



public class GetData extends AppCompatActivity {

    private Spinner spinner1,spinner2;
    ArrayList<ClassTimeData> classInfo;
    EditText name,st_time,end_time;
    ArrayAdapter<String> arrayAdapter,dayAdapter;
    String selected_info1;
    Button btn1,btn2,btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        classInfo = new ArrayList<ClassTimeData>();
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        name = (EditText) findViewById(R.id.name);
        st_time = (EditText) findViewById(R.id.st_time);
        end_time = (EditText)findViewById(R.id.end_time);
        ListView listView = (ListView) findViewById(R.id.listivew);
        MyAdapter myAdapter = new MyAdapter(this,classInfo);
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

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);

        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner1.setAdapter(arrayAdapter);

        dayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, day);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner2.setAdapter(dayAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_info1 = spinner1.getSelectedItem().toString();
                Log.e("###",selected_info1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"띠를 골라 주십시오",Toast.LENGTH_SHORT).show();
            }
        });


        listView.setAdapter(myAdapter);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classInfo.add(new ClassTimeData(spinner2.getSelectedItem().toString(),name.getText().toString(),st_time.getText().toString(),end_time.getText().toString()));
                listView.setAdapter(myAdapter);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classInfo.remove(classInfo.size()-1);
                listView.setAdapter(myAdapter);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
}