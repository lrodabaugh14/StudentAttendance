package com.example.lrodabaugh14.studentattendance;

import android.content.SharedPreferences;
import android.renderscript.RSRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.firebase.messaging.*;

import android.support.annotation.Nullable;
import android.util.*;

/**
 * Created by dsutter on 2/16/18.
 */

public class AppUtil {


    public static HashMap<String, Object> hashClasses;
    public static String strFacID;
    public static HashMap<String, Object> hshTeachersClasses = new HashMap();
    public static ArrayList<Object> arrStudents = new ArrayList();
    public static HashMap<String, Object> hshAttendancePerClass = new HashMap();
    public static boolean emergencyActive = false;
    public static boolean admin = false;
//    public static Object teacherClasses;


}
