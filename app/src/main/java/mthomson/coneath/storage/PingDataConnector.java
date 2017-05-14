package mthomson.coneath.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import mthomson.coneath.AndroidDatabaseManager;

public class PingDataConnector extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    public PingDataConnector(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, "PingData", factory, DATABASE_VERSION);
    }

    public PingDataConnector(Context context) {
        super(context, "PingData", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PingData (ID INTEGER PRIMARY KEY ASC, Timestamp BIGINT, PingData INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PingData");
        onCreate(db);
    }

    public void savePing(int ping){
        PingData newData = new PingData();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        newData.Timestamp = calendar.getTimeInMillis() / 1000L;
        newData.PingValue = ping;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format(
                "INSERT INTO PingData (Timestamp, PingData) VALUES ('%d', %d)", newData.Timestamp, newData.PingValue));
        db.close();

        Log.d(this.getClass().getName(), "Added Point: " + newData.toString());
    }

    public ArrayList<PingData> getPings(long seconds_before){
        long cutoff = (System.currentTimeMillis() / 1000L) - seconds_before;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format(
                "SELECT * FROM PingData WHERE Timestamp >= %d ORDER BY Timestamp ASC", cutoff
        ), null);

        ArrayList<PingData> data = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                int timestampColumnId = cursor.getColumnIndex("Timestamp");
                int valueColumnId = cursor.getColumnIndex("PingData");
                PingData newData = new PingData();
                newData.PingValue = cursor.getInt(valueColumnId);
                newData.Timestamp = cursor.getLong(timestampColumnId);
                data.add(newData);
            } while (cursor.moveToNext());
        }
        db.close();
        return data;
    }
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
