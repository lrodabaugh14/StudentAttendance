package com.example.lrodabaugh14.studentattendance;



import android.os.Looper;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Handler;

import java.util.ArrayList;
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


    public boolean LoginAttempt(String username, String passwords) throws InterruptedException {
        username = "candace_farmer";
        final String password = "password";
        DatabaseReference dbFaculty = dbRef.child("Faculty");
        DatabaseReference dbStudent = dbRef.child("Students");
        DatabaseReference refClasses = dbRef.child("Classes");
        DatabaseReference user = dbFaculty.child(username);


        final List<Object> userInfo = getUserInfo(user);
        List<Object> students = getStudents(dbStudent);
        final List<Object> classes = getClassInfo(refClasses);

        boolean matching = false;
        Thread.sleep(1000);
        if (!userInfo.isEmpty()) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) userInfo.get(0);
            String strPassword = (String) hashMap.get("Password");
            AppUtil.strFacID = (String) hashMap.get("ID");
            facNumber = (String) hashMap.get("ID");
            matching = strPassword.equals(password);
        }
        if (!students.isEmpty()) {
            // AppUtil.hshStudents = (HashMap<String, Object>) students.get(0);
            AppUtil.arrStudents = (ArrayList<Object>) students.get(0);
        }
        Thread t1 = new Thread(new Runnable() {
            public void run()
            {
                if (!classes.isEmpty()) {
                    AppUtil.hashClasses = (HashMap<String, Object>) classes.get(0);
                }
            }});
//        Thread.sleep(4000);
        t1.start();
        Thread.sleep(1000);
        return matching;
    }
    public List<Object> getStudents(DatabaseReference student) {
        final List<Object> studentInfo = new ArrayList();
        student.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        studentInfo.add(dataSnapshot.getValue());
                        return;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){

                    }
                }

        );
        return studentInfo;
    }
//
//    public List<Object> getClassesByTeacher() throws InterruptedException {
//
//
//        return classes;
//
//
//
//    }

    public List<Object> getClassInfo(DatabaseReference refClasses) {
        final List<Object> classes = new ArrayList();
        refClasses.orderByValue()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange (DataSnapshot dataSnapshot) {
                                classes.add(dataSnapshot.getValue());
                            }
                            @Override
                            public void onCancelled (DatabaseError databaseError){

                            }
                        }

                );
        return classes;
    }

    public List<Object> getUserInfo(DatabaseReference user) {
        final List<Object> userInfo = new ArrayList();
        user.addValueEventListener(
                new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userInfo.add(dataSnapshot.getValue());
            return;
        }

        @Override
        public void onCancelled(DatabaseError databaseError){

        }
    }

                );
        return userInfo;
    }
}
