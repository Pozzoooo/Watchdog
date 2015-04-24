package pozzo.watchdog.db;

import android.content.ContentValues;
import android.database.Cursor;

import pozzo.watchdog.pojo.PingRequest;
import pozzo.watchdog.pojo.WatchEntry;

/**
 * {@link PingRequest} table representation.
 *
 * @author Luiz Gustavo Pozzo
 * @since 21/04/15.
 */
public class PingRequestCr {
	public static final String TB_NAME = "pingRequest";

	public static final String _ID = "_id";
	public static final String FROM_NETWORK = "fromNetwork";
	public static final String LATENCY = "latency";
	public static final String DATE = "date";
	public static final String FK_WATCH_ENTRY = "fkWatchEntry";

	public static final String TB_CREATE = "create table " + TB_NAME + " (" +
			_ID + " integer primary key autoincrement, " +
			FROM_NETWORK + " varchar, " +
			LATENCY + " integer, " +
			DATE + " integer, " +
			FK_WATCH_ENTRY + " integer " +
			");";

	public static ContentValues getValues(PingRequest pingRequest) {
		ContentValues values = new ContentValues();

		if(pingRequest.getId() >= 0)
			values.put(_ID, pingRequest.getId());

		values.put(FROM_NETWORK, pingRequest.getFromNetwork());
		values.put(LATENCY, pingRequest.getLatency());
		values.put(DATE, pingRequest.getDate());
		values.put(FK_WATCH_ENTRY, pingRequest.getFkWatchEntry());

		return values;
	}

	public static PingRequest objectFrom(Cursor cursor) {
		PingRequest pingRequest = new PingRequest();

		pingRequest.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
		pingRequest.setFromNetwork(cursor.getString(cursor.getColumnIndex(FROM_NETWORK)));
		pingRequest.setLatency(cursor.getLong(cursor.getColumnIndex(LATENCY)));
		pingRequest.setDate(cursor.getLong(cursor.getColumnIndex(DATE)));
		pingRequest.setFkWatchEntry(cursor.getLong(cursor.getColumnIndex(FK_WATCH_ENTRY)));

		return pingRequest;
	}
}
