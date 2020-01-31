package com.example.logregsiterapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.logregsiterapp.utils.DataBaseInfo;
import com.example.logregsiterapp.model.User;

import java.util.LinkedList;
import java.util.List;

public class DatabaseConnection extends SQLiteOpenHelper {

    private Context context;


    public static final String COLOMN_ID = "id";
    public static final String COLOMN_NAME = "name";
    public static final String COLOMN_EMAIL = "email";
    public static final String COLOMN_PASSWORD = "password";
    public static final String COLOMN_DOB = "dob";


    public DatabaseConnection(@Nullable Context context) {
        super(context, DataBaseInfo.DATABASE_NAME, null, DataBaseInfo.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_QUERY = "CREATE TABLE " + DataBaseInfo.DATABASE_TABLE_NAME + "(" + COLOMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLOMN_NAME + " TEXT," + COLOMN_EMAIL + " TEXT," + COLOMN_PASSWORD + " TEXT," + COLOMN_DOB + " TEXT " + ");";

        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DATABASE_QUERY = " DROP TABLE IF EXISTS " + DataBaseInfo.DATABASE_TABLE_NAME;
        db.execSQL(DATABASE_QUERY);
        onCreate(db);
    }


    public boolean mInsertUserIntoDatabase(User user) {

        boolean database_status = false;

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        contentValues.put(COLOMN_NAME, user.getmName());
        contentValues.put(COLOMN_EMAIL, user.getmEmail());
        contentValues.put(COLOMN_PASSWORD, user.getmPassword());
        contentValues.put(COLOMN_DOB, user.getmDOB());

        database_status = sqLiteDatabase.insert(DataBaseInfo.DATABASE_TABLE_NAME, null, contentValues) > 0;
        return database_status;
    }

    public boolean CheckEmailAlreadyInDatabase(String email) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        // String query = " SELECT "+ COLOMN_EMAIL +" FROM "+DataBaseInfo.DATABASE_TABLE_NAME+" WHERE " + COLOMN_EMAIL + "= '"+email+"'";
        String query = " SELECT * FROM " + DataBaseInfo.DATABASE_TABLE_NAME + " WHERE " + COLOMN_EMAIL + "= '" + email + "'";
        cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    public String getPasswordofUserthroughEmail(String email) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(" SELECT * FROM " + DataBaseInfo.DATABASE_TABLE_NAME + " WHERE " + COLOMN_EMAIL + "= '" + email + "'", null);
        cursor.moveToFirst();
        String data = cursor.getString(cursor.getColumnIndex(COLOMN_PASSWORD));
        cursor.close();
        database.close();
        return data;
    }

    public String getUsername(String email) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(" SELECT * FROM " + DataBaseInfo.DATABASE_TABLE_NAME + " WHERE " + COLOMN_EMAIL + "= '" + email + "'", null);
        cursor.moveToFirst();
        String userName = cursor.getString(cursor.getColumnIndex(COLOMN_NAME));
        cursor.close();
        database.close();
        return userName;
    }

    public List<User> getUserList() {
        List<User> userList = new LinkedList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT * FROM " + DataBaseInfo.DATABASE_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(COLOMN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(COLOMN_EMAIL));
                String date = cursor.getString(cursor.getColumnIndex(COLOMN_DOB));
                userList.add(new User(name, email, date));
                cursor.moveToNext();
            }
        }
        return userList;
    }


}
