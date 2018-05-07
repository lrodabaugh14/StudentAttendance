package com.example.lrodabaugh14.studentattendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by lrodabaugh14 on 1/3/2018.
 */

public class attendanceTracker extends AppCompatActivity {
    studentDatabase db = new studentDatabase();
    HashMap<String, Object> StudentInClass = new HashMap();
    String strClass = "";


    HashMap<String, Object> attendancePerClass = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_picker);
        HashMap<String, Object> classes = AppUtil.hashClasses;
        List<String> teacherClasses = new ArrayList<String>();
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter();
        String facID = AppUtil.strFacID;
        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutClick();
            }
        });
        Spinner classPicker = (Spinner) findViewById(R.id.classChooser);
        classPicker.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                onDropDownClassSelected(adapterView, view, position, id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        Object[] keySet = classes.keySet().toArray();

        if (!classes.isEmpty()) {
            Collection<Object> objects = classes.values();
            for(int i=0; i< classes.size(); i++) {
                Object key = keySet[i];
                HashMap<String, Object> c = (HashMap<String, Object>) classes.get(key);
                if(facID.equals(c.get("FacultyId"))) {
                    teacherClasses.add((String) key);
                    AppUtil.hshTeachersClasses.put((String) key, c);
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, teacherClasses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classPicker.setAdapter((SpinnerAdapter) adapter);
    }

    public void onDropDownClassSelected(AdapterView<?> adapterView, View view,
                                        int position, long id) {
        StudentInClass.clear();
        Object item = adapterView.getItemAtPosition(position);
        HashMap<String, Object> choseClass = (HashMap<String, Object>) AppUtil.hshTeachersClasses.get(item.toString());
        strClass = item.toString();
        attendancePerClass.put(strClass,AppUtil.hshAttendancePerClass.get(strClass));

        ArrayList  students = (ArrayList) choseClass.get("Students");
        for(int i =0; i< students.size(); i++){
            StudentInClass.put(students.get(i).toString(), AppUtil.hshStudents.get(students.get(i).toString()));
        }
        loadStudentNames();

    }
    public void loadStudentNames(){

        //Find linear layout and clear out all views to remove students
        LinearLayout ll = (LinearLayout) findViewById(R.id.llStudents);
        ll.removeAllViews();

        //Create button and set the listener
        Button b = new Button(this);
        b.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,0f));
        b.setText("Submit");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick();
            }
        });

        for(int i = 0; i< StudentInClass.size(); i++) {
            final String key = (String) StudentInClass.keySet().toArray()[i];
            HashMap<String, Object> stu = (HashMap<String, Object>)StudentInClass.get(key);
            final String strStuName = stu.get("Name").toString();

            LinearLayout w = new LinearLayout(this);
            w.setOrientation(LinearLayout.HORIZONTAL);
            w.setPadding(16,16,16,16);
            // Add text
            TextView tv = new TextView(this);
            tv.setTextSize(24);
            tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            tv.setText(strStuName);
            tv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onViewClick(key, strStuName);
                }
            });

            //Insert Switch
            Switch s = new Switch(this);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            HashMap<String, Object> classDay = (HashMap<String, Object>) AppUtil.hshAttendancePerClass.get(strClass);
            if (classDay != null && classDay.get(dateFormat.format(date)) != null) {
                HashMap<String, Object> days = (HashMap<String, Object>) classDay.get(dateFormat.format(date));
                if (days.get(key).equals("absent")) {
                    s.setChecked(false);
                } else {
                    s.setChecked(true);
                }
            } else {
                s.setChecked(true);
            }
            s.setId(Integer.valueOf(key));
            s.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 6f));

            //Add the text view and switch to the linear layout
            w.addView(tv);
            w.addView(s);
            // Add the LinearLayout element to the Main Linear Layout
            ll.addView(w);
        }
        // Add the button to the layout last
        ll.addView(b);
    }

    private void onButtonClick(){

        HashMap<String, String> absentStudents = new HashMap();
        for(int i=0; i < StudentInClass.size(); i++) {
            String key = (String) StudentInClass.keySet().toArray()[i];
            Switch s = (Switch) findViewById(Integer.valueOf(key));
            if(s.isChecked()) {
                absentStudents.put(key, "present");
            } else {
                absentStudents.put(key, "absent");
            }
        }

        db.submitAttendance(strClass, absentStudents);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        String strAlert = "Attendance for " + strClass + " has been submitted";
        alertDialog.setMessage(strAlert);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }
    private void onViewClick(String strStuID, String strStuName) {
        ArrayList<String> datesMissed = new ArrayList();
        HashMap classAttendance = new HashMap();
        classAttendance =  (HashMap<String, Object>) attendancePerClass.get(strClass);
        if (classAttendance != null) {
            Object[] keys = classAttendance.keySet().toArray();
            for (int i = 0; i < classAttendance.size(); i++) {
                HashMap<String,String> day = (HashMap<String, String>) classAttendance.get(keys[i]);
                if (day.get(strStuID).equals("absent")) {
                    datesMissed.add(keys[i].toString());
                }

            }

            Intent i = new Intent(attendanceTracker.this, ViewAttendance.class);
            i.putExtra("stu_id", strStuID);
            i.putExtra("stu_name", strStuName);
            i.putExtra("class", strClass);
            i.putExtra("days", datesMissed.toArray());
            i.putStringArrayListExtra("dates_missed", datesMissed);
            startActivity(i);

        }  else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            String strAlert = "Attendance for " + strClass + " has never been taken";
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
    private void onLogoutClick() {
//        LoginActivity loginActivity = new LoginActivity();
//        loginActivity.onLogout();
        AppUtil.logout();
        startActivity(new Intent(attendanceTracker.this, LoginActivity.class));

    }
}
