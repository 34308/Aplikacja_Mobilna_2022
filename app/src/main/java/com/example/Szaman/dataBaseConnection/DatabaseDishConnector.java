package com.example.Szaman.dataBaseConnection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.Szaman.model.Dish;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDishConnector extends SQLiteOpenHelper {
    public static final String DISH_TABLE = "Dishes";
    public static final String COLUMN_DISH_ID = "DishId";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_RESTAURANT_ID = "RestaurantId";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_IMAGE_URL = "ImageUrl";

    public DatabaseDishConnector(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
        cursor.close();
        db.close();
        return dishes;
    }
}
