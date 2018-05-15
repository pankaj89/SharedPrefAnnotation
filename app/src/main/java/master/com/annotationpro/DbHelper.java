package master.com.annotationpro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.photohotel.database.table.Customers;
import com.photohotel.database.table.GroupTypeMaster;
import com.photohotel.database.table.HotelHotspotLog;
import com.photohotel.database.table.HotelSettings;
import com.photohotel.database.table.NationalityMaster;
import com.photohotel.database.table.PhotoSession;
import com.photohotel.database.table.PhotoSessionLength;
import com.photohotel.database.table.PhotoSessionSlots;
import com.photohotel.database.table.PhotoViewSession;
import com.photohotel.database.table.Proposal;
import com.photohotel.database.table.SessionTypeMaster;
import com.photohotel.database.table.UserNotification;
import com.photohotel.database.table.Users;
import com.photohotel.database.table.ViewSessionLength;
import com.photohotel.database.table.ViewSessionSlots;

import java.util.ArrayList;

/**
 * Created By Pankaj, Database Helper class.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static Context context;
    private static DbHelper dbHelper;
    private static ArrayList<SQLiteDatabase> sqLiteDatabases;

    public static final void install(Context mContext) {
        context = mContext;
    }

    public static SQLiteDatabase getInstance() throws Exception {
        return getInstance(context);
    }

    public static SQLiteDatabase getInstance(Context context) throws Exception {
        if (dbHelper == null) {
            dbHelper = new DbHelper(context);
            sqLiteDatabases = new ArrayList<>();
            sqLiteDatabases.add(dbHelper.getWritableDatabase());
            Log.i("DB_LOG", "DBConnections:" + sqLiteDatabases.size());
            Log.i("DB_LOG", "DBPath:" + sqLiteDatabases.get(sqLiteDatabases.size() - 1).getPath());
            return sqLiteDatabases.get(sqLiteDatabases.size() - 1);
        } else {
            int i = 0;
            for (SQLiteDatabase sqLiteDatabase : sqLiteDatabases) {
                if (!sqLiteDatabase.isOpen()) {
                    SQLiteDatabase sqliteDb = dbHelper.getWritableDatabase();
                    sqLiteDatabases.set(i, sqliteDb);
                    return sqliteDb;
                } else {
                    return sqLiteDatabase;
                }
            }
            Log.i("DB_LOG", "DBConnections:" + sqLiteDatabases.size());
            return sqLiteDatabases.get(sqLiteDatabases.size() - 1);
        }
    }

    public DbHelper(Context context) {
        super(context, "DB", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Customers.CREATE_TABLE);
        sqLiteDatabase.execSQL(GroupTypeMaster.CREATE_TABLE);
        sqLiteDatabase.execSQL(HotelHotspotLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(HotelSettings.CREATE_TABLE);
        sqLiteDatabase.execSQL(NationalityMaster.CREATE_TABLE);
        sqLiteDatabase.execSQL(PhotoSession.CREATE_TABLE);
        sqLiteDatabase.execSQL(PhotoSessionLength.CREATE_TABLE);
        sqLiteDatabase.execSQL(PhotoSessionSlots.CREATE_TABLE);
        sqLiteDatabase.execSQL(PhotoViewSession.CREATE_TABLE);
        sqLiteDatabase.execSQL(Proposal.CREATE_TABLE);
        sqLiteDatabase.execSQL(SessionTypeMaster.CREATE_TABLE);
        sqLiteDatabase.execSQL(UserNotification.CREATE_TABLE);
        sqLiteDatabase.execSQL(Users.CREATE_TABLE);
        sqLiteDatabase.execSQL(ViewSessionLength.CREATE_TABLE);
        sqLiteDatabase.execSQL(ViewSessionSlots.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}