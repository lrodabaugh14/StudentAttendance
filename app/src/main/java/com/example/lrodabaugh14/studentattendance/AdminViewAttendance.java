package com.example.lrodabaugh14.studentattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminViewAttendance extends AppCompatActivity {
    boolean viewClasses;
    String stuID;
    String stuName;
    ArrayList<String> stuClasses;
    TextView txtSentence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewClasses = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtStudentName = (TextView) findViewById(R.id.txtStudentName);
        txtSentence = (TextView) findViewById(R.id.txtClass);

        final FloatingActionButton btnGoBack = (FloatingActionButton) findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            stuName = extras.getString("stu_name");
            stuID = extras.getString("stu_id");
//            strClass = extras.getString("class");
            stuClasses = extras.getStringArrayList("stu_classes");
        }
        txtStudentName.setText(stuName);
        addClasses();

    }

    private void addClasses() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.llFillThis);
        ll.removeAllViews();
        txtSentence.setVisibility(View.INVISIBLE);
        if (!stuClasses.isEmpty()) {
            for (int i = 0; i < stuClasses.size(); i++) {
                final String strClass = stuClasses.get(i);

                TextView tv = new TextView(this);

                tv.setTextSize(24);
                tv.setPadding(0, 8, 0, 8);
                tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                tv.setText(strClass);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClassClicked(stuID, strClass);
                    }

                });
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                ll.addView(tv);
            }

        }
    }

    private void onBackClick() {
        if (viewClasses) {
            startActivity(new Intent(AdminViewAttendance.this, AdminActivity.class));
        } else {
            addClasses();
            viewClasses = true;
        }

    }


    private void onClassClicked(String strStudentID, String strChosenClass) {
        HashMap<String, Object> attendancePerClass = AppUtil.hshAttendancePerClass;
        HashMap classAttendance = (HashMap<String, Object>) attendancePerClass.get(strChosenClass);
        if (classAttendance != null) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.llFillThis);
            ll.removeAllViews();

            ArrayList<String> datesMissed = new ArrayList();
            txtSentence.setVisibility(View.VISIBLE);
            txtSentence.setText("has been absent from " + strChosenClass + " on these days.");
            Object[] keys = classAttendance.keySet().toArray();
            for (int i = 0; i < classAttendance.size(); i++) {
                HashMap<String, String> day = (HashMap<String, String>) classAttendance.get(keys[i]);
                if (day.get(strStudentID).equals("absent")) {
                    datesMissed.add(keys[i].toString());
                }
            }

            for (int i = 0; i < datesMissed.size(); i++) {
                final String date = datesMissed.get(i);

                TextView tv = new TextView(this);

                tv.setTextSize(18);
                tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                tv.setText(date);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);

                ll.addView(tv);
            }
            viewClasses = false;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            String strAlert = "Attendance for " + strChosenClass + " has never been taken";
            alertDialog.setMessage(strAlert);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();

        }
    }
}
