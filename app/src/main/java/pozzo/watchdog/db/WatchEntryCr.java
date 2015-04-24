package pozzo.watchdog.db;

import android.content.ContentValues;
import android.database.Cursor;

import pozzo.watchdog.pojo.WatchEntry;

/**
 * {@link WatchEntry} table representation.
 *
 * @author Luiz Gustavo Pozzo
 * @since 17/04/15.
 */
public class WatchEntryCr {
	public static final String TB_NAME = "watchEntry";

	public static final String _ID = "_id";
	public static final String ADDRESS = "address";
	public static final String REQUEST_TYPE = "requestType";
	public static final String PORT = "port";
	public static final String ALARM = "alarm";
	public static final String EMAIL = "email";
	public static final String FREQUENCY = "frequency";
	public static final String CAN_BE_DELAYED = "canBeDelayed";
	//I prefered to use demoralization cause I will need a lot of query on this
	public static final String NEXT_WATCH = "nextWatch";

	public static final String TB_CREATE = "create table " + TB_NAME + " (" +
			_ID + " integer primary key autoincrement, " +
			ADDRESS + " varchar not null, " +
			REQUEST_TYPE + " varchar, " +
			PORT + " integer, " +
			ALARM + " integer, " +
			EMAIL + " varchar, " +
			FREQUENCY + " integer, " +
			CAN_BE_DELAYED + " integer, " +
			NEXT_WATCH + " integer " +
			");";

	public static ContentValues getValues(WatchEntry watchEntry) {
		ContentValues values = new ContentValues();

		if(watchEntry.getId() > 0)
			values.put(_ID, watchEntry.getId());
		values.put(ADDRESS, watchEntry.getAddress());
		values.put(REQUEST_TYPE, watchEntry.getRequestType());
		values.put(PORT, watchEntry.getPort());
		values.put(ALARM, watchEntry.isAlarm());
		values.put(EMAIL, watchEntry.getEmail());
		values.put(FREQUENCY, watchEntry.getFrequency());
		values.put(CAN_BE_DELAYED, watchEntry.isCanBeDelayed());
		values.put(NEXT_WATCH, watchEntry.getNextWatch());

		return values;
	}

	public static WatchEntry objectFrom(Cursor cursor) {
		WatchEntry entry = new WatchEntry();

		entry.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
		entry.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
		entry.setRequestType(cursor.getString(cursor.getColumnIndex(REQUEST_TYPE)));
		entry.setPort(cursor.getInt(cursor.getColumnIndex(PORT)));
		entry.setAlarm(cursor.getInt(cursor.getColumnIndex(ALARM)) != 0);
		entry.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
		entry.setFrequency(cursor.getLong(cursor.getColumnIndex(FREQUENCY)));
		entry.setCanBeDelayed(cursor.getInt(cursor.getColumnIndex(CAN_BE_DELAYED)) != 0);
		entry.setNextWatch(cursor.getLong(cursor.getColumnIndex(NEXT_WATCH)));

		return entry;
	}
}
