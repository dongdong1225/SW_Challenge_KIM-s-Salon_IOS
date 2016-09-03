package com.app.sample.messenger.cctv_DB;

/**
* Created by duddu on 2016-09-03.
*/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

public class NotesDbAdapter extends AppCompatActivity
{
    public static final String KEY_ID = "ID";
    public static final String KEY_ADDRESS = "Address";
    public static final String KEY_CONTACT = "Contact";
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";
    private static final String TAG = "NotesDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table CCTV (ID integer primary key, "
                                                                + "Address text not null, "
                                                                + "Contact text not null," +
                                                                    "Latitude text not null," +
                                                                    "Longitude text not null);";

    private static final String DATABASE_NAME = "data.db";
    private static final String DATABASE_TABLE = "cctv";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }

    }

    public void copyExcelDataToDatabase() {

        Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            Log.i("COLUM","before i");
            InputStream is = getBaseContext().getResources().getAssets().open("incheon.xls");
            Log.i("COLUM","before iff");
            workbook = Workbook.getWorkbook(is);
            Log.i("COLUM","before if");

            if (workbook != null) {
                sheet = workbook.getSheet(0);
                Log.i("COLUM","in if");
                if (sheet != null) {

                    int nMaxColumn = 2;
                    int nRowStartIndex = 0;
                    int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                    int nColumnStartIndex = 0;
                    int nColumnEndIndex = sheet.getRow(2).length - 1;

                    for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {

                        String column1 = sheet.getCell(nColumnStartIndex , nRow).getContents();
                        String column2 = sheet.getCell(nColumnStartIndex + 1, nRow).getContents();
                        String column3 = sheet.getCell(nColumnStartIndex + 2, nRow).getContents();
                        String column4 = sheet.getCell(nColumnStartIndex + 3, nRow).getContents();
                        String column5 = sheet.getCell(nColumnStartIndex + 4, nRow).getContents();
//
//                        str += column1;
//                        str += column2;
//                        str += column3;
//                        str += column4;
//                        str += "\n";
                        Log.i("COLUMN",column1);
                        createNote(column1,column2,column3,column4,column5);
                    }

                }

                else {
                    System.out.println("Sheet is null!!");
                }

            } else {
                System.out.println("WorkBook is null!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("COLUM",e.getMessage());
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createNote(String ID, String addr, String contact, String latitude, String longitude)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, ID);
        initialValues.put(KEY_ADDRESS, addr);
        initialValues.put(KEY_CONTACT, contact);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);
        try {
            return mDb.insert(DATABASE_TABLE, null, initialValues);
        }
        catch (Exception e) {
         //   e.printStackTrace();
        }
        return 0;
    }

//    public boolean deleteNote(long rowId) {
//        Log.i("Delete called", "value__" + rowId);
//        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
//    }

    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_ADDRESS, KEY_CONTACT, KEY_LATITUDE, KEY_LONGITUDE }, null, null, null, null, null);
    }

//    public Cursor fetchNote(long rowId) throws SQLException {
//
//        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY }, KEY_ROWID
//                + "=" + rowId, null, null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//    }
//
//    public boolean updateNote(long rowId, String title, String body) {
//        ContentValues args = new ContentValues();
//        args.put(KEY_TITLE, title);
//        args.put(KEY_BODY, body);
//        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
//    }



}