package com.sujichim.jasanjao2.card;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedam on 15. 9. 28.
 * when adding a new column, add
 * 1. private static final String KEY_NEWCOLUMN = "newcolumn"
 * 2. KEY_NEWCOLUMN + " TEXT, " in onCreate()
 * 3. values.put(KEY_NEWCOLUMN, record.getNewColumn()) in addRecord()
 * 4. values.put(KEY_NEWCOLUMN, record.getNewColumn()) in updateRecord()
 * 5. record.setNewColumn(cursor.getString(12)) in getAllRecordsOfSameName()
 * 6. record.setNewColumn(cursor.getString(12)) in getAllRecords()
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "DBHelper";
    private Context context;

    private static DBHelper sInstance = null;

    // Database Version
    private static final int DATABASE_VERSION = 11;

    // Database Name
    private static final String DB_NAME = "MedicalRecord.db";

    // table name
    private static final String TABLE_RECORDS = "records";

    // Records Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_NAME = "name";
    private static final String KEY_MEMO = "memo";

    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context);
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_DATE + " TEXT, "
                + KEY_NAME + " TEXT, "
                + KEY_MEMO + " TEXT);";
        Log.d(LOG_TAG, "db create: " + CREATE_RECORDS_TABLE);
        db.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "db upgrade");
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        // create fresh books table
        onCreate(db);
    }

    // Adding new record
    public void addRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_NAME, record.getName()); // Record Name
        values.put(KEY_MEMO, record.getMemo());

        // Inserting Row
        db.insert(TABLE_RECORDS, null, values);
        db.close(); // Closing database connection
    }

    // Updating new record
    public int updateRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, record.getName()); // Record Name
        values.put(KEY_MEMO, record.getMemo());

        Log.d(LOG_TAG, "updateRecord");

        // Updating Row
        int affectedRow = db.update(TABLE_RECORDS, values, KEY_ID + " = ?", new String[]{String.valueOf(record.getId())});
        db.close(); // Closing database connection

        return affectedRow;
    }

    public List<Record> getAllRecordsOfSameName(String name) {
        List<Record> recordList = new ArrayList<Record>();
        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_RECORDS + " WHERE "
//                + KEY_NAME + "=" + name;
        String selectQuery = "SELECT * FROM " + TABLE_RECORDS + " WHERE "
                + KEY_NAME + " LIKE '%" + name + "%';";
        Log.d(LOG_TAG, "getAllRecordsOfSameName " + selectQuery);

        //getReadableDatabase() : 읽기 가능
        //getWritableDatabase() : 읽기, 쓰기 가능
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Record record = new Record();
                    record.setId(Integer.parseInt(cursor.getString(0)));
                    record.setDate(cursor.getString(1));
                    record.setName(cursor.getString(2));
                    record.setMemo(cursor.getString(3));
                    // Adding record to list
                    recordList.add(record);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "Error while trying to get Points from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        db.close();
        return recordList;
    }

    public List<Record> getAllRecords() {
        List<Record> recordList = new ArrayList<Record>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RECORDS + " ORDER BY " + KEY_ID + " DESC";

        //getReadableDatabase() : 읽기 가능
        //getWritableDatabase() : 읽기, 쓰기 가능
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Record record = new Record();
                    record.setId(Integer.parseInt(cursor.getString(0)));
                    record.setDate(cursor.getString(1));
                    record.setName(cursor.getString(2));
                    record.setMemo(cursor.getString(3));
                    // Adding record to list
                    recordList.add(record);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "Error while trying to get Points from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        db.close();
        return recordList;
    }

    // Deleting single record
    public void deleteRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDS, KEY_ID + " = ?",
                new String[]{String.valueOf(record.getId())});
        db.close();
    }

    public int getRowCount() {
        String tableName = TABLE_RECORDS;
        // get row count
        SQLiteDatabase db = getReadableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, tableName);
        db.close();
        return (int) numRows;
    }

}