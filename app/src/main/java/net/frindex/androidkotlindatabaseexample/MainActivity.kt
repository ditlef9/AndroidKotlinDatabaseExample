package net.frindex.androidkotlindatabaseexample

import android.content.ContentValues.TAG
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import net.frindex.androidkotlindatabaseexample.dao.DBAdapter
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*- Stetho Database Tools */
        // To browse database open Edge URL: edge://inspect/
        Stetho.initializeWithDefaults(this);
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()


        // Database
        val db: DBAdapter = DBAdapter(this)

        // Truncate (delete old data)
        db.rawQuery("DELETE FROM games_index")

        // Insert
        Log.i(TAG, "- Starting insert ---------------------------------------------------- -------")
        db.insert("games_index", "game_title, game_language", "'Mitt spill', 'no'")
        db.insert("games_index", "game_title, game_language", "'My game', 'en'")

        // Select
        var cursor: Cursor? = null
        cursor = db.select("SELECT game_id, game_title, game_language FROM games_index ORDER BY game_id DESC")
        val numberOfCols = cursor!!.columnCount
        val numberOfRows = cursor.count
        val rowsSaying = "Rows in table = " + numberOfRows
        Toast.makeText(applicationContext,rowsSaying,Toast.LENGTH_SHORT).show()

        while (cursor.moveToNext()) {
            var id = cursor.getInt(0)
            var title = cursor.getString(1)
            var language = cursor.getString(2)

            val output = "ID=" + id + " Title=" + title + " Language=" + language;
            Toast.makeText(applicationContext, output, Toast.LENGTH_SHORT).show()
        }


    } // onCreate
} // MainActivity