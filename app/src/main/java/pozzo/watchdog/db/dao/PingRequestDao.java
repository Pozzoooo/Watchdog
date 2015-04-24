package pozzo.watchdog.db.dao;

import android.database.sqlite.SQLiteDatabase;

import pozzo.watchdog.db.ConexaoDBManager;
import pozzo.watchdog.db.PingRequestCr;
import pozzo.watchdog.pojo.PingRequest;

/**
 * Created by ghost on 21/04/15.
 *
 * TODO implement PingRequestDao
 */
public class PingRequestDao {

	public long replace(PingRequest pingRequest) {
		SQLiteDatabase db = new ConexaoDBManager().getDb();
		return db.replace(PingRequestCr.TB_NAME, null, PingRequestCr.getValues(pingRequest));
	}
}
