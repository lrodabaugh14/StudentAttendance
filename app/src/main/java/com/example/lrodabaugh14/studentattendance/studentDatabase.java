package com.example.lrodabaugh14.studentattendance;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by lrodabaugh14 on 1/5/2018.
 */

public class studentDatabase {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    String strUser;

    public void StudentDatabase() {

    }

    public String LogIn(String strUsername, String strPassword) {
        if (myRef.child("Faculty").child(strUsername).equals(null)) {
            return "Error";
        } else if (strPassword != "password") {
            return "Error";
        } else {
            strUser = strUsername;
            return strUsername;
        }
    }


}
