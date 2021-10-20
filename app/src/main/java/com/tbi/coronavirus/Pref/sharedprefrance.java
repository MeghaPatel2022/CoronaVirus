package com.tbi.coronavirus.Pref;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedprefrance {

    public static final String MyPREFERENCES = "VirusBeep";
    public static String firebasetoken = "firebasetoken";
    public static String userId = "userId";
    public static String fullName = "fullName";
    public static String email = "email";
    public static String contact_no = "contact_no";
    public static String dob = "dob";
    public static String corona_status = "corona_status";
    public static String country = "country";
    public static String latitude = "latitude";
    public static String longitude = "longitude";
    public static String isLogin = "isLogin";

    public static String getfirebasetoken(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(firebasetoken, "");
        return ans;
    }

    public static void setfirebasetoken(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(firebasetoken, value);
        editor.apply();
    }

    public static String getUserID(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(userId, "");
        return ans;
    }

    public static void setUserID(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(userId, value);
        editor.apply();
    }

    public static String getFullName(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(fullName, "");
        return ans;
    }

    public static void setFullName(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(fullName, value);
        editor.apply();
    }

    public static String getEmail(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(email, "");
        return ans;
    }

    public static void setEmail(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(email, value);
        editor.apply();
    }

    public static String getContact_no(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(contact_no, "");
        return ans;
    }

    public static void setContact_no(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(contact_no, value);
        editor.apply();
    }

    public static String getDob(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(dob, "");
        return ans;
    }

    public static void setDob(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(dob, value);
        editor.apply();
    }

    public static String getCorona_status(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(corona_status, "");
        return ans;
    }

    public static void setCorona_status(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(corona_status, value);
        editor.apply();
    }

    public static String getCountry(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(country, "");
        return ans;
    }

    public static void setCountry(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(country, value);
        editor.apply();
    }

    public static String getLatitude(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(latitude, "");
        return ans;
    }

    public static void setLatitude(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(latitude, value);
        editor.apply();
    }

    public static String getLongitude(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(longitude, "");
        return ans;
    }

    public static void setLongitude(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(longitude, value);
        editor.apply();
    }

    public static Boolean getIsLogin(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean ans = sharedpreferences.getBoolean(isLogin, false);
        return ans;
    }

    public static void setIsLogin(Context c1, Boolean value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(isLogin, value);
        editor.apply();
    }

    public static void clearAll(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }

}
