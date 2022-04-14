package com.example.Szaman.dataBaseConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.Szaman.MainActivity;
import com.example.Szaman.model.Dish;
import com.example.Szaman.model.Restaurant;
import com.example.Szaman.model.User;
import com.example.Szaman.model.UserComparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DatabaseConnector extends SQLiteOpenHelper {
    public static final String COLUMN_USER_ID = "UserId";
    public static final String USER_TABLE = "Users";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_LOGIN = "Login";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_SURNAME = "Surname";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_DEBIT_CARD_NUMBER = "DebitCardNumber";
    public static final String COLUMN_EXPIRE_DATE = "ExpireDate";
    public static final String COLUMN_CVV = "Cvv";
    public static final String COLUMN_EMAIL = "Email";


    public static final String RESTAURANT_TABLE = "Restaurants";
    public static final String DISH_TABLE = "Dishes";
    public static final String SHOPPING_CARD_TABLE = "ShoppingCart";
    public static final String COLUMN_DISH_ID = "DishId";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_RESTAURANT_ID = "RestaurantId";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_IMAGE_URL = "ImageUrl";
    private SQLiteDatabase vDatabase;
    private Context vContext;
    private static String DB_NAME = "data.db"; //nazwa bazy danych znajdujaca sie w assets

    public DatabaseConnector(@Nullable Context context) {

        super(context, DB_NAME, null, 1);
        this.vContext = context;
        // Copy the DB if need be when instantiating the DataBaseHelper
        if (!checkDataBase()) {
            copyDB();
        }
        vDatabase = this.getWritableDatabase();
    }


    private boolean checkDataBase() {
        File db = new File(vContext.getDatabasePath(DB_NAME).getPath()); //Get the file name of the database
        Log.d("DBPATH","DB Path is " + db.getPath());
        if (db.exists()) return true; // If it exists then return doing nothing

        // Get the parent (directory in which the database file would be)
        File dbdir = db.getParentFile();
        // If the directory does not exist then make the directory (and higher level directories)
        if (!dbdir.exists()) {
            db.getParentFile().mkdirs();
            dbdir.mkdirs();
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        UserComparator userComparator = new UserComparator();
        List<User> users = this.getUsers();
        for (User singleUser : users) {
            if (userComparator.compare(user, singleUser) == 0){
                return false;
            }
        }

        cv.put(COLUMN_LOGIN, user.getLogin());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_NAME, user.getName());
        cv.put(COLUMN_SURNAME, user.getSurname());
        cv.put(COLUMN_ADDRESS, user.getAddress());
        cv.put(COLUMN_DEBIT_CARD_NUMBER, user.getDebitCardNumber());
        cv.put(COLUMN_EXPIRE_DATE, user.getExpireDate());
        cv.put(COLUMN_CVV, user.getCvv());
        cv.put(COLUMN_EMAIL, user.getEmail());

        long insert = db.insert(USER_TABLE, null, cv);
        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + USER_TABLE + " WHERE " + COLUMN_USER_ID + " = " + user.getUserId();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }

    public void copyDB() throws SQLiteException {
        try {
            InputStream myInput = vContext.getAssets().open(DB_NAME);
            String outputFileName = vContext.getDatabasePath(DB_NAME).getPath();
            Log.d("LIFECYCLE", outputFileName);
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;
            while( (length=myInput.read(buffer)) > 0 ){
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public List<Restaurant> getRestaurants(){
        List<Restaurant> restaurants = new ArrayList<>();
        String queryString = "SELECT * FROM " + RESTAURANT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do {
                int restaurantId = cursor.getInt(0);
                String restaurantName = cursor.getString(1);
                Restaurant restaurant = new Restaurant(restaurantId, restaurantName);
                restaurants.add(restaurant);
            } while (cursor.moveToNext());
        } else
        {

        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return restaurants;
    }

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        String queryString = "SELECT * FROM " + USER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do {
                int userId = cursor.getInt(0);
                String login = cursor.getString(1);
                String password = cursor.getString(2);
                String name = cursor.getString(3);
                String surname = cursor.getString(4);
                String address = cursor.getString(5);
                String debitCardNumber = cursor.getString(6);
                String expireDate = cursor.getString(7);
                String cvv = cursor.getString(8);
                String email = cursor.getString(9);
                User user = new User(userId, login, password,
                        name, surname, address, debitCardNumber,
                        expireDate, cvv, email);
                users.add(user);
            } while (cursor.moveToNext());
        } else
        {

        }
//        if(cursor != null && !cursor.isClosed()){
//            cursor.close();
//        }
//        db.close();
        return users;
    }

    public List<Dish> getDishes(){
        List<Dish> dishes = new ArrayList<>();
        String queryString = "SELECT * FROM " + DISH_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do {
                int dishId = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Double price = cursor.getDouble(3);
                int restaurantId = cursor.getInt(4);
                String imageUrl = cursor.getString(5);
                Dish dish = new Dish(dishId, name, description, price, restaurantId, imageUrl);
                dishes.add(dish);
            } while (cursor.moveToNext());
        } else
        {

        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return dishes;
    }

    public List<Integer> getDishIds(){
        List<Integer> dishIds = new ArrayList<>();
        String queryString = "SELECT * FROM " + SHOPPING_CARD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do {
                int dishId = cursor.getInt(0);
                dishIds.add(dishId);
            } while (cursor.moveToNext());
        } else
        {

        }
//        if(cursor != null && !cursor.isClosed()){
//            cursor.close();
//        }
//        db.close();
        return dishIds;
    }
    public boolean addDishId(Integer dishId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        List<Integer> dishIds = this.getDishIds();
        if (dishIds.contains(dishId) ){
            return false;
        }
        cv.put(COLUMN_DISH_ID, dishId);

        long insert = db.insert(SHOPPING_CARD_TABLE, null, cv);
        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }

}
