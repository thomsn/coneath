package storage.mthomson.coneath;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Fayalite on 3/26/2017.
 */

public class PingDataConnector extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_PING_TABLE_NAME = "PingData";
    private static final String DATABASE_TABLE_ID = "ID";
    private static final String DATABASE_PING_TIMESTAMP_COLUMN = "Timestamp";
    private static final String DATABASE_PING_VALUE_COLUMN = "PingData";
    private static final String DATABASE_PING_CREATE =
            "CREATE TABLE " + DATABASE_PING_TABLE_NAME + " (" +
            DATABASE_TABLE_ID + " INTEGER PRIMARY KEY ASC, " +
            DATABASE_PING_TIMESTAMP_COLUMN + " INTEGER, " +
            DATABASE_PING_VALUE_COLUMN + " REAL);";

    private SQLiteDatabase _db;

    public PingDataConnector(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_PING_TABLE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_PING_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static final String INSERT_NEW_ENTRY = "INSERT INTO " + DATABASE_PING_TABLE_NAME + " (" + DATABASE_PING_TIMESTAMP_COLUMN + ", " + DATABASE_PING_VALUE_COLUMN + ") "
            + "VALUES ('%1d', %2f)";
    public void savePing(double ping){
        SQLiteDatabase db = getWritableDatabase();

        java.util.Date date = new java.util.Date();
        String sqlCommand = String.format(INSERT_NEW_ENTRY, date.getTime(), ping);
        db.execSQL(sqlCommand);
        db.close();
    }

    private static final String SELECT_DATA = "SELECT * FROM " + DATABASE_PING_TABLE_NAME + " WHERE " + DATABASE_PING_TIMESTAMP_COLUMN + " >= %1d ORDER BY " + DATABASE_PING_TIMESTAMP_COLUMN + " ASC";
    public ArrayList<PingData> getPing(Date from){
        ArrayList<PingData> data = new ArrayList<PingData>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(String.format(SELECT_DATA, from.getTime()), null);
        if (cursor.moveToFirst()){
            do {
                int timestampColumnId = cursor.getColumnIndex(DATABASE_PING_TIMESTAMP_COLUMN);
                int valueColumnId = cursor.getColumnIndex(DATABASE_PING_VALUE_COLUMN);
                PingData newData = new PingData();
                newData.PingValue = cursor.getInt(valueColumnId);
                newData.Timestamp = new Date(cursor.getLong(timestampColumnId));
                data.add(newData);
            } while (cursor.moveToNext());
        }

        db.close();
        return data;
    }

    private static final String DELETE_ALL_DATA = "DELETE FROM " + DATABASE_PING_TABLE_NAME;
    public void deleteAllData(){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(DELETE_ALL_DATA);
        db.close();
    }

    private static final String DROP_TABLE = "DROP " + DATABASE_PING_TABLE_NAME;
    public void dropTable() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(DROP_TABLE);
        db.close();
    }
}
