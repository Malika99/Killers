package com.example.killers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    //Initialize variable
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Create constructor
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    //Create set login method
    public void setLogin(boolean login) {
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    //Create get login method
    public  boolean getLogin() {
        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    //Create set email method
    public void setEmail(String email) {
        editor.putString("KEY_EMAIL",email);
        editor.commit();
    }

    //Create get email method
    public String getEmail() {
        return sharedPreferences.getString("KEY_EMAIL", "");
    }

}
