package com.example.lrodabaugh14.studentattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class AdminActivity extends AppCompatActivity {
    ArrayList studentsInGrade = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button btnLogin = (Button) findViewById(R.id.btnLogout);
        List<String> grades = new ArrayList<String>();
        grades.add("9");
        grades.add("10");
        grades.add("11");
        grades.add("12");
        Spinner gradePicker = (Spinner) findViewById(R.id.spinner2);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutClick();
            }
        });
        gradePicker.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                onGradeSelected(adapterView, view, position, id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, grades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradePicker.setAdapter((SpinnerAdapter) adapter);

    }

    public void onGradeSelected(AdapterView<?> adapterView, View view,
                    int position, long id){
        studentsInGrade.clear();
        String strGrade  = String.valueOf(adapterView.getItemAtPosition(position));
        HashMap<String, Object> studentsInGrade = new HashMap();


        HashMap<String, Object> hshStudents = AppUtil.hshStudents;
        Object[] keys = hshStudents.keySet().toArray();
        for(int i =0; i< hshStudents.size(); i++){
            HashMap<String, Object> student = (HashMap<String, Object>) hshStudents.get(keys[i]);
            if(student.get("Grade").equals(strGrade)){
                studentsInGrade.put((String) keys[i], hshStudents.get(keys[i]));
            }

        }
        loadStudentNames(studentsInGrade);

    }
    public void loadStudentNames(HashMap<String, Object> hshStudents){

        //Find linear layout and clear out all views to remove students
        LinearLayout ll = (LinearLayout) findViewById(R.id.llStudents);
        ll.removeAllViews();

        for(int i = 0; i< hshStudents.size(); i++) {

            final String key = (String) hshStudents.keySet().toArray()[i];
            HashMap<String, Object> stu = (HashMap<String, Object>)hshStudents.get(key);

            final String strStuName = stu.get("Name").toString();

            LinearLayout w = new LinearLayout(this);
            w.setOrientation(LinearLayout.HORIZONTAL);
            w.setPadding(16,16,16,16);
            // Add text
            TextView tv = new TextView(this);
            tv.setTextSize(24);
            tv.setId(i);
            tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            tv.setText(strStuName);
            tv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onViewClick(key, strStuName);
                }
            });

            //Add the text view and switch to the linear layout
            w.addView(tv);
            // Add the LinearLayout element to the Main Linear Layout
            ll.addView(w);
        }
    }

    public void onViewClick(String key, String strStuName){
        ArrayList<Object> arrStuClasses = AppUtil.arrStuClasses;
        ArrayList<String> specStuClass = new ArrayList();

        for(int i = 0; i< arrStuClasses.size(); i++){
           String strClass = (String) arrStuClasses.get(i);
           char[] arrClass = strClass.toCharArray();
           Boolean pastId = false;
            String strStudentInClass = "";
            String strSpecClass = "";
            for (char letter: arrClass) {
                if(letter != ' ' ){
                    if(!pastId){
                        strStudentInClass += letter;
                    } else {
                        strSpecClass += letter;
                    }
                } else {
                    pastId = true;
                }
            }
           if(strStudentInClass.equals(key)) {
               specStuClass.add(strSpecClass);
           }
        }
        Intent i = new Intent(AdminActivity.this, AdminViewAttendance.class);
        i.putExtra("stu_id", key);
        i.putExtra("stu_name", strStuName);
        i.putExtra("stu_classes",specStuClass);

        startActivity(i);

    }


    public void onLogoutClick(){
        AppUtil.logout();
        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
    }
}
