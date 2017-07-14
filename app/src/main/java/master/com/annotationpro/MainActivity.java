package master.com.annotationpro;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.text)).setText("MainActivity");

        UserPref userPref = UserPref.getInstance(this);
        ((TextView) findViewById(R.id.text)).setText(userPref.getName() + "," + userPref.getTimestamp());
        userPref.setName("Pankaj");
        userPref.setAge(60);
        userPref.setTimestamp(System.currentTimeMillis());

        SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
        /*Cursor cursor = db.rawQuery("SELECT COUNT(*) value FROM Student", null);
        do {
            cursor.moveToFirst();
            String value=cursor.getString(0);
            ((TextView) findViewById(R.id.text)).setText(value);
        }while(cursor.moveToNext());*/



        /*Cursor cursor = db.rawQuery("PRAGMA table_info(Student111)", null);
        cursor.moveToNext();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String default_value = cursor.getString(cursor.getColumnIndex("dflt_value"));
            String notnull = cursor.getString(cursor.getColumnIndex("notnull"));
            String pk = cursor.getString(cursor.getColumnIndex("pk"));
            Log.i("DB", name + "," + type + "," + default_value + "," + notnull + "," + pk);
        }*/
    }
}
