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

import com.example.Szaman.model.CartItemComparator;
import com.example.Szaman.model.Dish;
import com.example.Szaman.model.Restaurant;
import com.example.Szaman.model.CartItem;
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
    public static final String SHOPPING_CART_TABLE = "ShoppingCart";
    public static final String COLUMN_DISH_ID = "DishId";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_RESTAURANT_ID = "RestaurantId";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_IMAGE_URL = "ImageUrl";
    private static final String COLUMN_CART_ITEM_ID = "CartItemId";
    public static final String COLUMN_COUNT_OF_DISH = "CountOfDish";

    private SQLiteDatabase vDatabase;
    private static Context vContext;
    private static String DB_NAME = "data.db"; //nazwa bazy danych znajdujaca sie w assets
    private static DatabaseConnector sInstance;

    public DatabaseConnector(@Nullable Context context) {

        super(context, DB_NAME, null, 1);
        this.vContext = context;
        // Copy the DB if need be when instantiating the DataBaseHelper
        if (!checkDataBase()) {
            copyDB();
        }
        vDatabase = this.getWritableDatabase();
    }

    public static synchronized DatabaseConnector getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseConnector(context.getApplicationContext());
        }
        return sInstance;
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

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        UserComparator userComparator = new UserComparator();
        List<User> users = this.getUsers();
        for (User singleUser : users) {
            if (userComparator.compare(user, singleUser) == 0) {
                return false;
            }
        }
        vDatabase.beginTransaction();
        try {
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
            if (insert == -1) {
                return false;
            } else {
                return true;
            }
        } finally {
            db.endTransaction();
        }
    }

    public boolean updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String queryString = "UPDATE Users\n" +
                "   SET \n" +
                "       Login = ?,\n" +
                "       Password = ?,\n" +
                "       Name = ?,\n" +
                "       Surname = ?,\n" +
                "       Address = ?,\n" +
                "       DebitCardNumber = ?,\n" +
                "       ExpireDate = ?,\n" +
                "       Cvv = ?,\n" +
                "       Email = ?\n" +
                "   WHERE " + COLUMN_USER_ID + " = ?;";
        String[] whereArgs = new String[]{String.valueOf(user.getUserId())};
        String[] selectionArgs = new String[]{
                String.valueOf(user.getLogin()),
                String.valueOf(user.getPassword()),
                String.valueOf(user.getName()),
                String.valueOf(user.getSurname()),
                String.valueOf(user.getAddress()),
                String.valueOf(user.getDebitCardNumber()),
                String.valueOf(user.getExpireDate()),
                String.valueOf(user.getCvv()),
                String.valueOf(user.getEmail()),
                String.valueOf(user.getUserId())
        };
        Cursor cursor = db.rawQuery(queryString, selectionArgs);
        if (cursor.getCount() > 0) {
            vDatabase.beginTransaction();
            long result = -1;
            try {
                result = db.update(USER_TABLE, cv, COLUMN_USER_ID + " = ?", whereArgs);
            } finally {
                db.endTransaction();
                if (result == -1) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public boolean deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + USER_TABLE + " WHERE " + COLUMN_USER_ID + " = " + user.getUserId();
        Cursor cursor = db.rawQuery(queryString,null);
        vDatabase.beginTransaction();
        try {
            if(cursor.moveToFirst()){
                return true;
            }
            else {
                return false;
            }
        } finally {
            db.endTransaction();
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
        try {
            if (cursor.moveToFirst()) {
                do {
                    int restaurantId = cursor.getInt(0);
                    String restaurantName = cursor.getString(1);
                    String imageUrl = cursor.getString(2);
                    Restaurant restaurant = new Restaurant(restaurantId, restaurantName, imageUrl);
                    restaurants.add(restaurant);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return restaurants;
    }
    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        String queryString = "SELECT * FROM " + USER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        try {
            if (cursor.moveToFirst()) {
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
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return users;
    }

    public List<Dish> getDishes(){
        List<Dish> dishes = new ArrayList<>();
        String queryString = "SELECT * FROM " + DISH_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        try {
            if (cursor.moveToFirst()) {
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
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return dishes;
    }

    //pobieranie listy dishów z koszyka
    public List<CartItem> getCartItems(){
        List<CartItem> cartItems = new ArrayList<>();
        String queryString = "SELECT * FROM " + SHOPPING_CART_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        try {
            if (cursor.moveToFirst()){
                do {
                    int cartItemId = cursor.getInt(0);
                    int userId = cursor.getInt(1);
                    int dishId = cursor.getInt(2);
                    int countOfDish = cursor.getInt(3);
                    CartItem cartItem = new CartItem(cartItemId, userId, dishId, countOfDish);
                    cartItems.add(cartItem);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return cartItems;
    }
    public User getUser(String l ,String p){
        User user = null;
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE Login = '" + l + "';";
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
                user = new User(userId, login, password,
                        name, surname, address, debitCardNumber,
                        expireDate, cvv, email);
            } while (cursor.moveToNext());
        } else
        {

        }
        return user;
    }
    // dodawanie produktu do koszyka
    public boolean addCartItem(CartItem cartItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        vDatabase.beginTransaction();
        long insert = 0;
        cv.put(COLUMN_USER_ID, cartItem.getUserId());
        cv.put(COLUMN_DISH_ID, cartItem.getDishId());
        cv.put(COLUMN_COUNT_OF_DISH, cartItem.getCountOfDish());

        insert = db.insert(SHOPPING_CART_TABLE, null, cv);

        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }
    public List<CartItem> getCartItems(int usId){
        List<CartItem> cartItems = new ArrayList<>();
        String queryString = "SELECT * FROM " + SHOPPING_CART_TABLE+" WHERE UserId="+usId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do {
                int cartItemId = cursor.getInt(0);
                int userId = cursor.getInt(1);
                int dishId = cursor.getInt(2);
                int countOfDish = cursor.getInt(3);
                CartItem cartItem = new CartItem(cartItemId, userId, dishId, countOfDish);
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        } else
        {

        }

        return cartItems;
    }
    //usuwanie produktów z koszyka za pomocą obiektu
    public boolean deleteCartItem(CartItem cartItem){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + SHOPPING_CART_TABLE + " WHERE " + COLUMN_CART_ITEM_ID + " = " + cartItem.getCartItemId();
        vDatabase.beginTransaction();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(queryString,null);
        } finally {
            db.endTransaction();
            if (cursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        }
    }
    public boolean updateCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put(COLUMN_COUNT_OF_DISH, cartItem.getCountOfDish());
        String queryString = "UPDATE " + SHOPPING_CART_TABLE + "\n" +
                "   SET CountOfDish = ? \n" +
                "   WHERE CartItemId = ?;";
        String[] whereArgs = new String[]{String.valueOf(cartItem.getCartItemId())};
        String[] selectionArgs = new String[]{String.valueOf(cartItem.getCountOfDish()), String.valueOf(cartItem.getCartItemId())};
        Cursor cursor = db.rawQuery(queryString, selectionArgs);
        if (cursor.getCount() > 0) {
            vDatabase.beginTransaction();
            long result = -1;
            try {
                result = db.update(SHOPPING_CART_TABLE, cv, COLUMN_CART_ITEM_ID + " = ?", whereArgs);
            } finally {
                db.endTransaction();
                if (result == -1) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }
    public boolean upsertCartItem(CartItem cartItem){
        List<CartItem> cartItems = this.getCartItems();
        CartItemComparator cartItemComparator = new CartItemComparator();
        for (CartItem item : cartItems) {
            if(cartItemComparator.compare(item,cartItem) == 0){
                cartItem.setCartItemId(item.getCartItemId());
                cartItem.setCountOfDish(item.getCountOfDish() + cartItem.getCountOfDish());
                return this.updateCartItem(cartItem);
            }
        }
        return this.addCartItem(cartItem);
    }
}