package com.example.phonebook;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class DataBase   {

    private String tableName="phones";
    SQLiteDatabase dataBase;


   private final String Create=("CREATE TABLE if not exists "
           + tableName +
           "(ID integer PRIMARY KEY  AUTOINCREMENT , " +
           "Name text not null, " +
           "Phone text not null UNIQUE, " +
           "Description text not null, " +
           "Category text not null " +
           ");" );


    public void create() throws SQLException{
        dataBase.execSQL(Create);
    }

    public void insert(String Name, String Phone, String Description, String Category) throws  SQLException{

        dataBase.execSQL("INSERT INTO "+tableName+"(Name, Phone, Description, Category) values(?, ?, ?, ?);",
                new String[]{Name, Phone, Description, Category});
    }

    public void update(String ID, String Name, String Phone, String Description, String Category) throws  SQLException{

        dataBase.execSQL("UPDATE "+tableName+" SET Name=?, Phone=?, Description=?, Category=? WHERE ID=?",
                new String[]{Name, Phone, Description,Category, ID});
    }

    public void delete(String ID) throws  SQLException{
        dataBase.execSQL("DELETE FROM "+tableName+" WHERE ID=?", new String[]{ID});
    }

    public ArrayList<String> AllContact() throws  SQLException{
        ArrayList<String> contacts=new ArrayList<String>();
        dataBase.beginTransaction();
        Cursor cursor=dataBase.rawQuery("SELECT * FROM "+tableName+" ORDER BY Name;", null);
        while(cursor.moveToNext()){
            String ID=cursor.getString(cursor.getColumnIndex("ID"));
            String Name=cursor.getString(cursor.getColumnIndex("Name"));
            String Phone=cursor.getString(cursor.getColumnIndex("Phone"));
            String Description=cursor.getString(cursor.getColumnIndex("Description"));
            String Category=cursor.getString(cursor.getColumnIndex("Category"));
            String element=ID+"\t"+Name+"\t"+Phone+"\t"+Description+"\t"+Category;
            contacts.add(element);
        }
        dataBase.endTransaction();
        return contacts;
    }


    public void closeDataBase(){
        dataBase.close();
        dataBase=null;
    }


    public DataBase(AddContact context) throws SQLException {
        dataBase=SQLiteDatabase.openOrCreateDatabase(context.getFilesDir()+"/phones.db", null);

    }


    public DataBase(ViewContacts context) throws SQLException {
        dataBase=SQLiteDatabase.openOrCreateDatabase(context.getFilesDir()+"/phones.db", null);

    }

    public DataBase(EditContact context) throws SQLException {
        dataBase=SQLiteDatabase.openOrCreateDatabase(context.getFilesDir()+"/phones.db", null);

    }

}
