package master.com.annotationpro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pankaj Sharma on 14/7/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper dbHelper;

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null)
            dbHelper = new DBHelper(context);
        return dbHelper;
    }

    private DBHelper(Context context) {
        super(context, "DB", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Student(name TEXT, age TEXT)");

        ContentValues values = new ContentValues();
        values.put("name", "Pankaj");
        values.put("age", "50");
        db.insert("Student", null, values);

        ContentValues values1 = new ContentValues();
        values1.put("name", "Sumit");
        values1.put("age", "25");
        db.insert("Student", null, values1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SQLite sqLite = new SQLite(db, "Student");
        sqLite.addColumn("Company", "TEXT");
        sqLite.addColumn("TelNo", "TEXT");
        sqLite.renameColumn("name", "FirstName");
        sqLite.renameColumn("age", "AGE");
        sqLite.removeColumns("AGE");
        sqLite.renameTableName("Employee");
        sqLite.execute();

    }
}
