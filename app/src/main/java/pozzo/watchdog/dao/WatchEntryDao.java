package pozzo.watchdog.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import pozzo.watchdog.db.ConexaoDBManager;
import pozzo.watchdog.db.WatchEntryCr;
import pozzo.watchdog.pojo.WatchEntry;

/**
 * Database Access for {@link WatchEntry} objects.
 *
 * @author Luiz Gustavo Pozzo
 * @since 18/04/15.
 */
public class WatchEntryDao {

	/**
	 * Insert or replace an existing {@link WatchEntry} object in persistence database.
	 *
	 * @param watchEntry to be inserted/replaced.
	 * @return _id created for the just created object.
	 */
	public long replace(WatchEntry watchEntry) {
		SQLiteDatabase db = new ConexaoDBManager().getDb();
		return db.replace(WatchEntryCr.TB_NAME, null, WatchEntryCr.getValues(watchEntry));
	}

	/**
	 * Get a specific {@link WatchEntry} by its _id.
	 *
	 * @param id related to the desired object.
	 * @return object related to the id.
	 */
	public WatchEntry get(long id) {
		WatchEntry entry = null;
		SQLiteDatabase db = new ConexaoDBManager().getDb();
		Cursor cur = db.query(WatchEntryCr.TB_NAME, null, WatchEntryCr._ID + "=?",
				new String[]{"" + id}, null, null, null);
		if(cur.moveToNext())
			entry = WatchEntryCr.objectFrom(cur);
		return entry;
	}

	/**
	 * Get the next entry which should be checked.
	 *
	 * @param canBeDelayed true or false...
	 * @return Object returned by query or null if not found.
	 */
	public WatchEntry getNext(boolean canBeDelayed) {
		WatchEntry entry = null;

		SQLiteDatabase db = new ConexaoDBManager().getDb();
		Cursor cur = db.query(WatchEntryCr.TB_NAME, null,
				WatchEntryCr.CAN_BE_DELAYED + (canBeDelayed ? " is true" : " is false"),
				null, null, null, WatchEntryCr.NEXT_WATCH + " DESC", "1");
		if(cur.moveToNext())
			entry = WatchEntryCr.objectFrom(cur);
		return entry;
	}
}
