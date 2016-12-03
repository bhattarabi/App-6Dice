package edu.mnstate.vh8237yk.project2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aemuxu on 11/28/2016.
 */

public class RollDB {

    public static final String DB_NAME = "roll.db";
    public static final int DB_VERSION = 1;

    public static final String ROLL_TABLE = "roll";

    public static final String ROLL_ID = "_id";
    public static final int ROLL_ID_COL = 0;

    public static final String ROLL_PLAYER = "player";
    public static final int ROLL_PLAYER_COL = 1;

    public static final String ROLL_DATE = "date";
    public static final int ROLL_DATE_COL = 2;

    public static final String ROLL_GAME_ID = "game_id";
    public static final int ROLL_GAME_ID_COL = 3;

    public static final String ROLL_VALS = "roll_vals";
    public static final int ROLL_VALS_COL = 4;

    public static final String CREATE_ROLL_TABLE =
            "CREATE TABLE " + ROLL_TABLE + " (" +
                    ROLL_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ROLL_PLAYER   + " INTEGER NOT NULL, " +
                    ROLL_DATE + " TEXT, " +
                    ROLL_GAME_ID + " INTEGER, " +
                    ROLL_VALS + " INTEGER);";

    public static final String DROP_ROLL_TABLE =
            "DROP TABLE IF EXISTS " + ROLL_TABLE;

    synchronized public ArrayList<Roll> getGameRolls(String s, String gameId) {
        ArrayList<Roll> arr = new ArrayList<Roll>();

        String[] from = new String[]{ROLL_ID, ROLL_PLAYER, ROLL_VALS};
        String clause = ROLL_DATE + "= ? AND " + ROLL_GAME_ID +"= ?";
        String[] whereArgs = new String[]{s, gameId};

        openReadableDB();
        Cursor c = db.query(ROLL_TABLE, from, clause,
                whereArgs, null, null, null);

        while (c.moveToNext())
        {
            Roll r = new Roll(
                    c.getInt(0),
                    c.getInt(1),
                    getRollVals(c.getInt(2))
            );
            arr.add(r);
        }
        closeDB();
        return arr;
    }

    synchronized public void delete(String s, String selectedItem) {
        this.openWriteableDB();
        String clause = ROLL_DATE + "= ? AND " + ROLL_GAME_ID +"= ?";
        String[] whereArgs = new String[]{s, selectedItem};
        db.delete(ROLL_TABLE, clause, whereArgs);
        closeDB();
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL(CREATE_ROLL_TABLE);

            // insert test data
            db.execSQL("INSERT INTO roll (player, date, game_id, roll_vals)" +
                    " VALUES (1, \"01-22-2016\", 2, 324)");
            db.execSQL("INSERT INTO roll (player, date, game_id, roll_vals)" +
                    " VALUES (2, \"01-22-2016\", 2, 125)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("6-Dice DB", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(RollDB.DROP_ROLL_TABLE);
            onCreate(db);
        }
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public RollDB(Context context)
    {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    synchronized private void openReadableDB()
    {
        db = dbHelper.getReadableDatabase();
    }

    synchronized private void openWriteableDB()
    {
        db = dbHelper.getWritableDatabase();
    }

    synchronized private void closeDB()
    {
        if (db != null)
        {
            db.close();
        }
    }

    synchronized public ArrayList<Roll> getAllRolls()
    {
        this.openReadableDB();
        Cursor c = db.query(ROLL_TABLE, null, null, null, null, null, null);
        ArrayList<Roll> rolls = new ArrayList<Roll>();
        while (c.moveToNext())
        {
            Roll r = new Roll(
                    c.getInt(ROLL_ID_COL),
                    c.getString(ROLL_DATE_COL),
                    c.getString(ROLL_GAME_ID_COL),
                    c.getInt(ROLL_PLAYER_COL),
                    getRollVals(c.getInt(ROLL_VALS_COL))
            );
            rolls.add(r);
        }
        closeDB();
        return rolls;
    }

    private ArrayList<Integer> getRollVals(int anInt) {
        ArrayList<Integer> arr = new ArrayList<Integer>() {
        };
        while (anInt > 0)
        {
            arr.add(0, anInt%10);
            anInt /= 10;
        }
        return arr;
    }

    synchronized public long insertRoll(Roll r) {
        ContentValues cv = new ContentValues();
        cv.put(ROLL_DATE, r.date);
        cv.put(ROLL_GAME_ID, r.gameId);
        cv.put(ROLL_PLAYER, r.playerNum);
        cv.put(ROLL_VALS, r.rollValue());

        openWriteableDB();
        long rowID = db.insert(ROLL_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    synchronized public ArrayList<String> getGameIds(String date)
    {
        ArrayList<String> arr = new ArrayList<String>();

        String[] from = new String[]{ROLL_GAME_ID};
        String clause = ROLL_DATE + "= ?";
        String[] whereArgs = new String[]{date};

        openReadableDB();
        Cursor c = db.query(true, ROLL_TABLE, from, clause,
                whereArgs, ROLL_GAME_ID, null, null, null);

        while (c.moveToNext()) {
            arr.add(c.getString(0));
        }
        closeDB();

        return arr;
    }
}
