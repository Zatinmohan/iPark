package com.example.ipark.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.ipark.Models.Vehicle;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

public class databasehelper extends SQLiteOpenHelper {
    final String number = "number";
    final String vehicle = "Vehicle";
    final String Owner_Name = "owner";
    final String Car_Type = "cartype";
    final String Model = "model";
    final String regDate = "regDate";
    final String Engine = "engine";
    final String chassis = "chassis";


    public databasehelper(@Nullable Context context) {
        super(context, "Numbers.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement = "CREATE TABLE " + vehicle + " (" +
                number + " TEXT " + " PRIMARY KEY, " +
                " " + Owner_Name + " TEXT, " +
                Car_Type + " TEXT, " +
                Model + " TEXT, " +
                regDate + " TEXT, " +
                Engine + " TEXT, " +
                chassis + " TEXT" + " )";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS "+ vehicle) ;
//        onCreate(db);
    }

    public boolean addVehicle(Vehicle v){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(number,v.getReg_number());
        values.put(Owner_Name,v.getOwner());
        values.put(Car_Type,v.getVehicle_class());
        values.put(Model,v.getMaker_model());
        values.put(regDate,v.getReg_date());
        values.put(Engine,v.getEngine());
        values.put(chassis,v.getChassis());

        long insert = db.insert(vehicle,null,values);

        return insert != -1;
    }

   public List<Vehicle> getEveryone(){
       List<Vehicle> returnList = new ArrayList<>();
        String querryString = "Select * From " + vehicle;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(querryString,null);

        if(cursor.moveToFirst()){
            do{
                String plate_number = cursor.getString(0);
                String owner = cursor.getString(1);
                String type = cursor.getString(2);
                String model = cursor.getString(3);
                String register = cursor.getString(4);
                String engine = cursor.getString(5);
                String chassis = cursor.getString(6);


                returnList.add(new Vehicle(plate_number,register,chassis,engine,owner,type,model));

          }while(cursor.moveToNext());
        }else{
            //Something went wrong
            //Nothing to do here
        }
        cursor.close();
        db.close();
        return returnList;
    }

}
