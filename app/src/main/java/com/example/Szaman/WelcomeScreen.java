package com.example.Szaman;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.Szaman.MainActivity;
import com.example.Szaman.R;
import com.example.Szaman.ui.login.LoginFragment;

public class WelcomeScreen extends Activity {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public WelcomeScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_welcome_screen);


        final Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(WelcomeScreen.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onStop() {
        super.onStop();

    }

}