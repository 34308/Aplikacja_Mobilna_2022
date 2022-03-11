package com.example.Szaman.dataBaseConnection;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.Szaman.Models.Dish;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.ProviderMismatchException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DatabaseConnector extends SQLiteOpenHelper {
    public static final String DISH_TABLE = "Dishes";
    public static final String COLUMN_DISH_ID = "DishId";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_RESTAURANT_ID = "RestaurantId";

    public DatabaseConnector(@Nullable Context context) {
        super(context, "data.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + DISH_TABLE + " (\n" +
                "    " + DatabaseConnector.COLUMN_DISH_ID + "       INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + DatabaseConnector.COLUMN_NAME + "         TEXT,\n" +
                "    " + DatabaseConnector.COLUMN_PRICE + "        TEXT,\n" +
                "    " + DatabaseConnector.COLUMN_RESTAURANT_ID + " INTEGER\n" +
                ");";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addDish(Dish dish){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, dish.getName());
        cv.put(COLUMN_PRICE, dish.getPrice());
        cv.put(COLUMN_RESTAURANT_ID, dish.getRestaurantId());

        long insert = db.insert(DISH_TABLE, null, cv);
        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }

//    public void connectToDataBase(PackageManager packageManager,
//                                  String packageName) throws PackageManager.NameNotFoundException {
//        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
//        packageName = packageInfo.applicationInfo.dataDir;
//        String jdbcUrl = "jdbc:sqlite:"+packageName;
//        conn = DriverManager.getConnection(url);
//
//        System.out.println("Connection to SQLite has been established.");
//
//        } catch (SQLException e) {
//        System.out.println(e.getMessage());
//    }
}
