package com.example.Szaman.dataBaseConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.Szaman.model.User;
import com.example.Szaman.model.UserComparator;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUserConnector extends SQLiteOpenHelper {
    public static final String COLUMN_USER_ID = "UserId";
    public static final String USER_TABLE = "Users";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_LOGIN = "Login";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_SURNAME = "Surname";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_DEBIT_CARD_NUMBER = "debitCardNumber";
    public static final String COLUMN_EXPIRE_DATE = "expireDate";
    public static final String COLUMN_CVV = "cvv";

    public DatabaseUserConnector(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
                User user = new User(userId, login, password,
                        name, surname, address, debitCardNumber,
                        expireDate, cvv);
                users.add(user);
            } while (cursor.moveToNext());
        } else
        {

        }
        cursor.close();
        db.close();
        return users;
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
}
