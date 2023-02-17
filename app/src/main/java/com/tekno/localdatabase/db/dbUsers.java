package com.tekno.localdatabase.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.tekno.localdatabase.entities.Users;

import java.util.ArrayList;

public class dbUsers extends dbHelper{
   Context context;
   public dbUsers (@Nullable Context context){
        super(context);
        this.context = context;
   }
   public long insertUser(String name){
       long id = 0;
       try {
           dbHelper dbhelper = new dbHelper(context);
           SQLiteDatabase db = dbhelper.getWritableDatabase();
           ContentValues values = new ContentValues();
           values.put("name", name);
           id = db.insert(TABLE_USERS, null,values);
       }catch (Exception e){
           e.toString();
       }
       return id;
   }
   public ArrayList<Users> showUsers (){
       dbHelper dbhelper = new dbHelper(context);
       SQLiteDatabase db = dbhelper.getWritableDatabase();
       ArrayList<Users> listUsers = new ArrayList<>();
       Users user = null;
       Cursor cursorUser = null;
       cursorUser = db.rawQuery("select * from " + TABLE_USERS , null);
       if (cursorUser.moveToFirst()){
           do {
               user = new Users();
               user.setId(cursorUser.getInt(0));
               user.setName(cursorUser.getString(1));
               listUsers.add(user);
           }while(cursorUser.moveToNext());
           cursorUser.close();
       }
       return listUsers;
    }

}
