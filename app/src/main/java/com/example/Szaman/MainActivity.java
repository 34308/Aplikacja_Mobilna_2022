package com.example.Szaman;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Szaman.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements DrawerMenuController {
    private static final int STORAGE_PERMISSION_CODE = 101;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    DrawerLayout drawer;
    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(loadStyle().equals("1")||loadStyle().equals( String.valueOf(R.string.style_value))){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_restaurants, R.id.nav_login,R.id.nav_settings,R.id.nav_summary)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //DataBase Connection Test
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void registerWindow(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_register);
    }



  public void checkPermission(String permission, int requestCode)
  {
      if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

          // Requesting the permission
          ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
      }
      else {
          Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
      }
  }

    @Override
    public void unlockMneu() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void lockMneu() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void hideMneu() {
        binding.appBarMain.toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showMneu() {
        binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
    }
    public static void saveStyle(String data, Activity activity){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("CurrentStyle",data);
        editor.apply();
    }
    public String loadStyle(){
        SharedPreferences sharedPreferences=getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String style=sharedPreferences.getString("CurrentStyle", String.valueOf(R.string.style_value));
        return style;
    }
}