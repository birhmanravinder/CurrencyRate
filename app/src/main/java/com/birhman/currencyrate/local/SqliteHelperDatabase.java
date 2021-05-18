package com.birhman.currencyrate.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.birhman.currencyrate.model.MyCustomModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SqliteHelperDatabase extends SQLiteOpenHelper {
    private static final String TAG = SqliteHelperDatabase.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "currencyRateDb";

    private static final String TABLE_CURRENCY_RATE = "currency_rate"; // Currency rate table

    private static final String KEY_ID = "id";
    private static final String KEY_RATE_NAME = "currency_name";
    private static final String KEY_RATE_VALUE = "currency_value";

    Context context;
    private ContentValues cValues;
    private SQLiteDatabase dataBase = null;
    private Cursor cursor;

    public SqliteHelperDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CURRENCY_RATE_TABLE = "CREATE TABLE "
                + TABLE_CURRENCY_RATE + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_RATE_NAME + " TEXT, "
                + KEY_RATE_VALUE + " TEXT "
                +")";

        db.execSQL(CREATE_CURRENCY_RATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCY_RATE);
        onCreate(db);
    }

    public void addCurrency(List<Map.Entry<String, String>> lisOfCategory) {
        dataBase = this.getWritableDatabase();

        for (Map.Entry<String, String> enter : lisOfCategory) {
            cValues = new ContentValues();
            cValues.put(KEY_RATE_NAME, enter.getKey());
            cValues.put(KEY_RATE_VALUE, enter.getValue());
            dataBase.insert(TABLE_CURRENCY_RATE, null, cValues);
        }
        dataBase.close();
    }

    public List<MyCustomModel> getAllCategory() {
        List<MyCustomModel> listOfHighlightedText = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CURRENCY_RATE;
        dataBase = this.getReadableDatabase();
        try{
            Cursor cursor = dataBase.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    MyCustomModel allDishesResult = new MyCustomModel();
                    allDishesResult.setId(cursor.getString(cursor.getColumnIndex("id")));
                    allDishesResult.setStrKey(cursor.getString(cursor.getColumnIndex("currency_name")));
                    allDishesResult.setaDouble(cursor.getString(cursor.getColumnIndex("currency_value")));
                    listOfHighlightedText.add(allDishesResult);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
            listOfHighlightedText = Collections.EMPTY_LIST;
        }
        dataBase.close();
        return listOfHighlightedText;
    }

    public void update(String ID, String name, String value) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(KEY_RATE_NAME, name);
        values.put(KEY_RATE_VALUE, value);
        //updating row
        sqLiteDatabase.update(TABLE_CURRENCY_RATE, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    public void deleteEntry(String ID) {
        dataBase = this.getWritableDatabase();
        String where="ID=?";
        int numberOFEntriesDeleted= dataBase.delete(TABLE_CURRENCY_RATE, where, new String[]{ID}) ;
    //    toast("Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted);
     //   return numberOFEntriesDeleted;
    }

    /*

    public void inserRecord(long l, String title, String thumb_images, int i,
                            String bigDecimal, String total_stock) {

        dataBase = getWritableDatabase();

        cursor = dataBase.rawQuery("select count(*) from " + TABLE_ADD_TO_CART
                + " WHERE Nid =" + l + " and Title=" + "'" + title + "'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        //    Log.v("size_id", size_id);
        if (count <= 0) {
            Log.v("", "Not Found");
            cValues = new ContentValues();
            cValues.put(Nid, l);
            cValues.put(Title, title);
            cValues.put(Thumb_images, thumb_images);
            cValues.put(Qty, i);
            cValues.put(Price, bigDecimal);
            cValues.put(Total_stock, total_stock);
            dataBase.insert(TABLE_ADD_TO_CART, null, cValues);
        } else {
            Log.v("", "Found");
            cursor = dataBase.rawQuery("select Qty from " + TABLE_ADD_TO_CART
                    + " WHERE Nid = " + l, null);
            cursor.moveToFirst();
            int count1 = cursor.getInt(0);
            updateRecord(l, i, total_stock, count1);
        }
        dataBase.close();
    }

    public void updateRecord(long nid, int Quan, String total, int preqty) {

        dataBase = getWritableDatabase();

        cValues = new ContentValues();
        if (Quan + preqty <= Integer.parseInt(total)) {

            cValues.put(Qty, Quan + preqty);

        } else {
            cValues.put(Qty, Integer.parseInt(total));
            Log.v("total", total);

        }
        if (Quan + preqty > 0) {
            dataBase.update(SqliteHandler.TABLE_ADD_TO_CART, cValues, "Nid=" + nid, null);
        }
        dataBase.close();

    }

    public void updateRecordgift(long nid, int Quan, String total, int preqty, String title) {

        dataBase = getWritableDatabase();

        cValues = new ContentValues();
        if (Quan + preqty <= Integer.parseInt(total)) {

            cValues.put(Qty, Quan + preqty);

        } else {
            cValues.put(Qty, Integer.parseInt(total));
            Log.v("total", total);

        }
        if (Quan + preqty > 0) {
            dataBase.update(SqliteHandler.TABLE_ADD_TO_CART, cValues, "Nid=" + nid
                    + " and Title='" + title + "'", null);
        }
        dataBase.close();

    }

    public void DeleteRecord(long nid, String string) {

        dataBase = getWritableDatabase();
        dataBase.delete(SqliteHandler.TABLE_ADD_TO_CART, "nid=? and Size_id=? ",
                new String[] { String.valueOf(nid), String.valueOf(string) });
        dataBase.close();
    }

    public void DeleteRecordgift(long nid, String string) {

        dataBase = getWritableDatabase();
        dataBase.delete(SqliteHandler.TABLE_ADD_TO_CART, "nid=? and Title=? ",
                new String[] { String.valueOf(nid), String.valueOf(string) });
        dataBase.close();
    }

    public int countRecord() {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select sum(Qty) from " + TABLE_ADD_TO_CART, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int countRecordbynid(long l) {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Qty from " + TABLE_ADD_TO_CART + " WHERE Nid = " + l  , null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int gettotalprice() {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select sum(Qty*Price) from " + TABLE_ADD_TO_CART,
                null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public Cursor selectRecords() {
        dataBase = getReadableDatabase();

        // Getting data from database table
        cursor = dataBase.rawQuery("select * from " + TABLE_ADD_TO_CART, null);
        return cursor;
    }

    public void deleteRecord() {

        dataBase = getWritableDatabase();

        // Deleting all records from database table
        dataBase.delete(TABLE_ADD_TO_CART, null, null);

        dataBase.close();
    }*/

    public void deleteCurrencyTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CURRENCY_RATE, null, null);
        db.close();
    }
}
