package com.example.lrodabaugh14.studentattendance;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lrodabaugh14 on 1/3/2018.
 */

public class attendanceTracker extends AppCompatActivity {
    studentDatabase db = new studentDatabase();
    HashMap<String, Object> StudentInClass = new HashMap();
    String strClass = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_picker);
        HashMap<String, Object> classes = AppUtil.hashClasses;
        List<String> teacherClasses = new ArrayList<String>();
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter();
        String facID = AppUtil.strFacID;
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
        ArrayList  students = (ArrayList) choseClass.get("Students");
        for(int i =0; i< students.size(); i++){
            StudentInClass.put(students.get(i).toString(), AppUtil.arrStudents.get(Long.valueOf((Long) students.get(i)).intValue()));
        }
        loadStudentNames();

    }
    public void loadStudentNames(){

//        TextView student1 = (TextView) findViewById(R.id.student1);
//        student1.setText(stu.get("Name").toString());


        ScrollView sv = (ScrollView) findViewById(R.id.scvStudents);
        LinearLayout ll = (LinearLayout) findViewById(R.id.llStudents);
        ll.removeAllViews();

        // Create a LinearLayout element
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
            String key = (String) StudentInClass.keySet().toArray()[i];
            HashMap<String, Object> stu = (HashMap<String, Object>)StudentInClass.get(key);

            LinearLayout w = new LinearLayout(this);
            w.setOrientation(LinearLayout.HORIZONTAL);
            w.setPadding(16,16,16,16);
//            LinearLayout.LayoutParams Wider = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    2.0f
//            );

            // Add text
            TextView tv = new TextView(this);
            tv.setTextSize(24);
            tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
//            tv.layout(Wider);
            Switch s = new Switch(this);
            s.setChecked(true);
            s.setId(Integer.valueOf(key));
            s.setHighlightColor(Color.GREEN);
//            s.setBackgroundColor(Color.GREEN);
            s.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 6f));



            // DO SOME FORMATTING stuff
            tv.setText(stu.get("Name").toString());

//            tv.setId(Integer.getInteger(key));
            w.addView(tv);
            w.addView(s);
            // Add the LinearLayout element to the ScrollView
            ll.addView(w);
        }
        ll.addView(b);



        // Display the view
//        setContentView(v);

    }
    private void onButtonClick(){
        //TODO Set up submit attendance data, Gonna need to be creative here
        List<String> absentStudents = new ArrayList();
        for(int i=0; i < StudentInClass.size(); i++) {
            String key = (String) StudentInClass.keySet().toArray()[i];
            Switch s = (Switch) findViewById(Integer.valueOf(key));
            if(!s.isChecked()) {
                absentStudents.add(key);
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
}
