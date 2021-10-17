package net.frindex.androidkotlindatabaseexample.dao

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues




class DBAdapter (context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    /* 01 Object -------------------------------------------------------------------------- */
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "myDatabase"


        // private val sQLiteOpenHelper: SQLiteOpenHelper? = null // Java: private DatabaseHelper DBHelper;
        // private val db: SQLiteDatabase? = null // Java: private SQLiteDatabase db;
    }


    /*- 02 On Create ----------------------------------------------------------------------- */
    override fun onCreate(db: SQLiteDatabase?) {
        // Create tables
        val createTableGamesIndex = ("CREATE TABLE games_index(" +
                "game_id INTEGER PRIMARY KEY," +
                "game_title TEXT, " +
                "game_language TEXT" + ")")
        db?.execSQL(createTableGamesIndex)

    } // onCreate

    /*- 03 On Upgrade ---------------------------------------------------------------------- */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop tables
        db!!.execSQL("DROP TABLE IF EXISTS games_index")

        // Create tables
        onCreate(db)
    }

    /*- 04 Open --------------------------------------------------------------------------- */
    /*
    fun open(){
        // Java: db = DBHelper.getWritableDatabase();
        db = this.writableDatabase
    }*/

    /*- 05 Close --------------------------------------------------------------------------- */
    /*
    fun close(){
        // Java: DBHelper.close();
        sQLiteOpenHelper.close()
    }*/

    /* 06 Quote smart ---------------------------------------------------------------------- */
    fun quoteSmart(value: String?): String? {
        // Escapes special characters in a string for use in an SQL statement
        var value = value
        if (value != null && value.length > 0) {
            value = value.replace("\\", "\\\\")
            value = value.replace("'", "\\'")
            value = value.replace("\u0000", "\\0")
            value = value.replace("\n", "\\n")
            value = value.replace("\r", "\\r")
            value = value.replace("\"", "\\\"")
            value = value.replace("\\x1a", "\\Z")
            value = value.replace("'", "&#39;")
        }
        value = "'$value'"

        // Remove 'null'
        value = value.replace("'null'", "NULL")
        return value
    }
    fun quoteSmart(value: Double): Double {
        return value
    }
    fun quoteSmart(value: Int): Int {
        return value
    }
    fun quoteSmart(value: Long): Long {
        return value
    }


    /*- 07 RawQuery ----------------------------------------------------------------- */
    // Used for update, delete, truncate
    fun rawQuery(query: String?): String{

        val dbAdapter = this.writableDatabase
        dbAdapter.rawQuery(query, null)
        dbAdapter.close()

        return "ok";

    }


    /*- 08 Insert ------------------------------------------------------------------ */
    fun insert(table: String?, fieldCols: String?, fieldsValues: String?): Long? {
        val dbAdapter = this.writableDatabase
        val values = ContentValues()

        // Create arrays from fields and values
        var fieldsArray = fieldCols?.split(",")?.toTypedArray();
        var valuesArray = fieldsValues?.split(",")?.toTypedArray();

        // Put fields
        var x = 0
        if (fieldsArray != null) {
            for (field in fieldsArray) {
                values.put(field, valuesArray?.get(x))
                x = x+1;
            }
        }

        // Insert into db
        val insertedRowId: Long = dbAdapter.insert(table, null, values)

        // Close db
        dbAdapter.close()

        return insertedRowId;
    }


    /*- 09 Count -------------------------------------------------------------------- */
    fun count(query: String?): Int?{
        return try {
            val db = this.writableDatabase
            val mCount: Cursor = db.rawQuery(query + "", null)
            mCount.moveToFirst()
            val count = mCount.getInt(0)
            mCount.close()
            db.close()

            count
        } catch (e: SQLiteException) {
            -1
        }
    }


    /*- 10 Select ------------------------------------------------------------------ */
    fun select(query: String?): Cursor? {
        val dbAdapter = this.writableDatabase
        val mCursor: Cursor = dbAdapter.rawQuery(query, null)
        if (mCursor != null) {
            mCursor.moveToFirst()
        }
        return mCursor
        dbAdapter.close()
    }


} // class DBAdapter