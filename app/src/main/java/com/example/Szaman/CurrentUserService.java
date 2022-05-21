package com.example.Szaman;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUserService {
    public static void savePasses(String data, Activity activity){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("CurrentUser",data);
        editor.apply();
    }
    public static String loadPasses(Activity activity,Context context){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String login=sharedPreferences.getString("CurrentUser", String.valueOf(R.string.default_value));
        return login;
    }
}
