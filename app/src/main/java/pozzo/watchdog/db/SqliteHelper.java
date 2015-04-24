package pozzo.watchdog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ghost on 17/04/15.
 */
public class SqliteHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DB_NAME = "db.db";

	public SqliteHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(WatchEntryCr.TB_CREATE);
		db.execSQL(PingRequestCr.TB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
