package com.example.lrodabaugh14.studentattendance;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class AdminActivity extends AppCompatActivity {
    HashMap<String, Object> StudentInGrade = new HashMap();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
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
        StudentInGrade.clear();
        Object item = adapterView.getItemAtPosition(position);
        String strGrade = item.toString();
        ArrayList studentsInGrade = new ArrayList();

        ArrayList arrStudents = AppUtil.arrStudents;
        for(int i =1; i< arrStudents.size(); i++){
            HashMap<String, Object> student = (HashMap<String, Object>) arrStudents.get(i);
            if(student.get("Grade").equals(strGrade)){
                studentsInGrade.add(arrStudents.get(i));
            }

        }
        loadStudentNames(arrStudents);

    }
    public void loadStudentNames(ArrayList arrStudents){

        //Find linear layout and clear out all views to remove students
        LinearLayout ll = (LinearLayout) findViewById(R.id.llStudents);
        ll.removeAllViews();

        for(int i = 1; i< arrStudents.size(); i++) {
            HashMap<String, Object> stu = (HashMap<String, Object>)arrStudents.get(i);
            String key = (String) stu.get("ID");
            final String strStuName = stu.get("Name").toString();

            LinearLayout w = new LinearLayout(this);
            w.setOrientation(LinearLayout.HORIZONTAL);
            w.setPadding(16,16,16,16);
            // Add text
            TextView tv = new TextView(this);
            tv.setTextSize(24);
            tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            tv.setText(strStuName);
//            tv.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view) {
//                    onViewClick(key, strStuName);
//                }
//            });

            //Add the text view and switch to the linear layout
            w.addView(tv);
            // Add the LinearLayout element to the Main Linear Layout
            ll.addView(w);
        }
    }

    public void onLogoutClick(){

    }
}
