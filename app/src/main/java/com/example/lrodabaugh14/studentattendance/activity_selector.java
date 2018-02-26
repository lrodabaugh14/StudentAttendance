package com.example.lrodabaugh14.studentattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class activity_selector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
    }

    public void onTakeAttendanceClick(View view){
        startActivity(new Intent(activity_selector.this, attendanceTracker.class));
    }

    public void onViewAttendanceClick(View view){
        startActivity(new Intent(activity_selector.this, ViewAttendance.class));
    }

}
