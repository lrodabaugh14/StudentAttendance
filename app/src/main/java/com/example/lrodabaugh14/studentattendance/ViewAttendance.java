package com.example.lrodabaugh14.studentattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.common.base.Strings;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAttendance extends AppCompatActivity {

    String strStudentID;
    String strStudentName;
    String strClass;
    ArrayList<String> alDatesMissed;


    ViewAttendance(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        TextView nameView = (TextView) findViewById(R.id.txtStudentName);
        TextView classView = (TextView) findViewById(R.id.txtClass);
        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()){
            strStudentName = extras.getString("stu_name");
            strStudentID = extras.getString("stud_id");
            strClass = extras.getString("class");
            alDatesMissed = extras.getStringArrayList("dates_missed");
        }
        classView.setText("has been absent from " + strClass + " on these days.");
        nameView.setText(strStudentName);
        addDates();
    }
    private void addDates(){
        LinearLayout ll = (LinearLayout) findViewById(R.id.llDates);
        ll.removeAllViews();
        if(!alDatesMissed.isEmpty()) {
            for (int i = 0; i < alDatesMissed.size(); i++) {
                final String date = alDatesMissed.get(i);

                TextView tv = new TextView(this);

                tv.setTextSize(18);
                tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                tv.setText(date);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);

                ll.addView(tv);
            }
        }
    }
    public void goBack(View view){
        startActivity(new Intent(ViewAttendance.this, attendanceTracker.class));
    }
}
