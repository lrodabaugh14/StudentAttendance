package com.example.lrodabaugh14.studentattendance;



import android.content.SharedPreferences;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Handler;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.content.SharedPreferences.*;


/**
 * Created by lrodabaugh14 on 1/5/2018.
 */



public class studentDatabase {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = firebaseDatabase.getReference();
    String facNumber = "";

    studentDatabase() {

    }


    public boolean LoginAttempt(String username, String password) throws InterruptedException {

        DatabaseReference user = dbRef.child("Faculty").child(username);
        DatabaseReference dbStudent = dbRef.child("Students");
        DatabaseReference refClasses = dbRef.child("Classes");
        DatabaseReference refAttend = dbRef.child("ClassAttendance");
        DatabaseReference refEmergency = dbRef.child("EmergencyState");



        List<Object> userInfo = getDbInfo(user);
        List<Object> students = getDbInfo(dbStudent);
        List<Object> classes = getDbInfo(refClasses);
        List<Object> attendance = getDbInfo(refAttend);
        List<Object> emergency = getDbInfo(refEmergency);


        boolean matching = false;
        Thread.sleep(1000);
        if (!userInfo.isEmpty()) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) userInfo.get(0);
            String strPassword = (String) hashMap.get("Password");
            AppUtil.strFacID = String.valueOf(hashMap.get("ID"));
            if(hashMap.containsKey("admin")){
                if(Integer.valueOf(hashMap.get("admin").toString()) == 1){
                    AppUtil.admin = true;
                } else {
                    AppUtil.admin = false;
                }
            }
            facNumber = String.valueOf(hashMap.get("ID"));
            matching = strPassword.equals(password);
        }
        if(matching){
            if(!emergency.isEmpty()){
                if (students.get(0).toString().equals("false")) {
                    AppUtil.emergencyActive = false;
                } else {
                    AppUtil.emergencyActive = true;
                }
            }
            if (!students.isEmpty()) {
                AppUtil.arrStudents = (ArrayList<Object>) students.get(0);
            }
            if (!classes.isEmpty()) {
                AppUtil.hashClasses = (HashMap<String, Object>) classes.get(0);
            }
            if(!attendance.isEmpty()){
                AppUtil.hshAttendancePerClass  = (HashMap<String, Object>) attendance.get(0);
            }
        }



        return matching;
    }
    public List<Object> getDbInfo(DatabaseReference dbRef) {
        final List<Object> info = new ArrayList();
        dbRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        info.add(dataSnapshot.getValue());
                        return;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){

                    }
                }

        );
        return info;
    }

    public void submitAttendance(String strClass, HashMap<String, String> absentStudents){
        DatabaseReference classAttendance =  dbRef.child("ClassAttendance").child(strClass);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        HashMap<String, Object> classDay = new HashMap();
        classDay.put(dateFormat.format(date),absentStudents);
        AppUtil.hshAttendancePerClass.put(strClass,classDay);

        classAttendance.child(dateFormat.format(date)).setValue(absentStudents);
    }
    public void setEmergency(){
        if (AppUtil.emergencyActive) {
            AppUtil.emergencyActive = false;
            dbRef.child("EmergencyState").setValue("false");
        } else {
            AppUtil.emergencyActive = true;
            dbRef.child("EmergencyState").setValue("true");
        }
    }
}
