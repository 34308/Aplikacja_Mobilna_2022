package com.example.Szaman;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.example.Szaman.model.Dish;
import com.example.Szaman.model.Restaurant;
import com.example.Szaman.model.RestaurantDishConnector;
import com.example.Szaman.model.ShoppingCardDish;
import com.example.Szaman.model.User;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Szaman.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_restaurants, R.id.nav_login, R.id.nav_register,R.id.nav_settings,R.id.dish,R.id.nav_settings,R.id.items,R.id.meal_list,R.id.personalData,R.id.nav_summary)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //DataBase Connection Test

        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity.this);

        User user = new User("login123", "hasło123", "Filip",
                 "Broniek", "Różana 20", "1234567890",
                "12/25", "111", "filip@pwsz.com");
        User user1 = new User(1,"login123", "hasło123", "Filip",
                "Broniek", "Różana 20", "1234567890",
                "12/25", "111", "filip@pwsz.com");
        //listy pobranych obiektów z bazy danych, gotowe do obsługi w porgramie
        List<Restaurant> restaurants = databaseConnector.getRestaurants();
        List<User> users = databaseConnector.getUsers();
        //boolean success = databaseConnector.addUser(user1);
        List<Dish> dishes = databaseConnector.getDishes();
        ShoppingCardDish shoppingCardDish = new ShoppingCardDish(1,2);
        List<ShoppingCardDish> shoppingCart = databaseConnector.getShoppingCardDishes();
        boolean successAdd = databaseConnector.addShoppingCardDish(shoppingCardDish);
        boolean successDel = databaseConnector.deleteShoppingCardDish(1);
        List<ShoppingCardDish> shoppingCart2 = databaseConnector.getShoppingCardDishes();
        RestaurantDishConnector.fillRestaurantsWithDishes(restaurants,dishes);
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
}