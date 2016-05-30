package com.eagle.sqlitecrudexample;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class SQLiteCrudExample extends ListActivity {

    //DB name
    private final String dbName = "Android";
    //Table name
    private final String tableName = "Versions";
    //String array has list Android versions which will be populated in the list
    private final String[] versionNames= new String[]{"Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "Kitkat", "Lollipop", "Marshmallow"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase sampleDB = null;

        try {
            sampleDB = this.openOrCreateDatabase(dbName,MODE_PRIVATE, null);

            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS "+ tableName + " (versionname VARCHAR);");

            for(String ver: versionNames){
                sampleDB.execSQL("INSERT INTO " + tableName + " Values ('"+ver+"');");
            }

            Cursor c = sampleDB.rawQuery("SELECT versionname FROM " + tableName, null);

            if (c != null) {
                if (c.moveToFirst()){
                    do{
                        String firstName = c.getString(c.getColumnIndex("versionname"));

                        result.add(firstName);
                    } while (c.moveToNext());
                }
            }

            this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,result));
        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(), "Couldn't create open the database", Toast.LENGTH_SHORT).show();
        } finally {
            if (sampleDB != null) {
                sampleDB.execSQL("DELETE FROM " + tableName);
                sampleDB.close();
            }
        }
    }
}
