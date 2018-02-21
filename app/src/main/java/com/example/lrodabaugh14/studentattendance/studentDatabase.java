package com.example.lrodabaugh14.studentattendance;



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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by lrodabaugh14 on 1/5/2018.
 */



public class studentDatabase {
    private Object lock = new Object();
    private ChildEventListener mChildEventListener;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = firebaseDatabase.getReference();
    String facNumber = "";

    studentDatabase() {

    }


    public boolean LoginAttempt(String username, String password) throws InterruptedException {

        DatabaseReference user = dbRef.child("Faculty").child(username);
        DatabaseReference dbStudent = dbRef.child("Students");
        DatabaseReference refClasses = dbRef.child("Classes");

        final List<Object> userInfo = getDbInfo(user);
        List<Object> students = getDbInfo(dbStudent);
        final List<Object> classes = getDbInfo(refClasses);

        boolean matching = false;
        Thread.sleep(1000);
        if (!userInfo.isEmpty()) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) userInfo.get(0);
            String strPassword = (String) hashMap.get("Password");
            AppUtil.strFacID = String.valueOf(hashMap.get("ID"));
            facNumber = String.valueOf(hashMap.get("ID"));
            matching = strPassword.equals(password);
        }
        if(matching){
            if (!students.isEmpty()) {
                AppUtil.arrStudents = (ArrayList<Object>) students.get(0);
            }
            if (!classes.isEmpty()) {
                AppUtil.hashClasses = (HashMap<String, Object>) classes.get(0);
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

    public void submitAttendance(String strClass, List<String> absentStudents){
        DatabaseReference classAttendance =  dbRef.child("ClassAttendance").child(strClass);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        HashMap<String, Object> classDay = new HashMap();
        classDay.put(dateFormat.format(date),absentStudents);

        classAttendance.setValue(classDay);
    }
}
