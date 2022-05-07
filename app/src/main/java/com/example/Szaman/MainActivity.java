package com.example.Szaman;

import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.Szaman.model.Dish;
import com.example.Szaman.model.*;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.service.*;

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

public class MainActivity extends AppCompatActivity implements DrawerMenuController {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    DrawerLayout drawer;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
/**
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        EditText searchBar=new EditText(getApplicationContext());
        ImageButton searchButton=new ImageButton(getApplicationContext());
        searchBar.setWidth((int) (width/1.5));
        ((ImageButton) searchButton).setImageResource(R.drawable.ic_search);

        binding.appBarMain.toolbar.addView(searchBar);
        binding.appBarMain.toolbar.addView(searchButton);
 **/
        setSupportActionBar(binding.appBarMain.toolbar);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_restaurants, R.id.nav_login,R.id.nav_settings,R.id.dish,R.id.nav_settings,R.id.items,R.id.nav_summary)
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
        List<Dish> dishes = databaseConnector.getDishes();
        RestaurantDishConnector.fillRestaurantsWithDishes(restaurants,dishes);

        List<User> users = databaseConnector.getUsers();
        //boolean success = databaseConnector.addUser(user1);

        CartItem cartItem =new CartItem(2,3);
        CartItem cartItem2 =new CartItem(11,1,3);
        CartItem cartItem3 =new CartItem(18,2,3,10);
        //boolean addItemSuccess = databaseConnector.addCartItem(cartItem);
        //boolean updateItemSuccess = databaseConnector.updateCartItem(cartItem3);
        //boolean delItemSuccess = databaseConnector.deleteCartItem(cartItem3);
        boolean delItemSuccess = databaseConnector.upsertCartItem(cartItem3);
        List<CartItem> cartItems = databaseConnector.getCartItems();
        CartItemService.connectCartItemsWithDishesAndUsers(cartItems, dishes, users);
        List<UserCart> userCarts = UserCartService.makeUserCarts(users, cartItems);

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

  /**  public void setToolbarTitle(){
        binding.appBarMain.toolbar.setTitle("");
    }
**/
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
}