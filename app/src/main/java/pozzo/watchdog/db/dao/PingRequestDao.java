package pozzo.watchdog.db.dao;

import android.database.sqlite.SQLiteDatabase;

import pozzo.watchdog.db.ConexaoDBManager;
import pozzo.watchdog.db.PingRequestCr;
import pozzo.watchdog.pojo.PingRequest;

/**
 * Persistence of {@link PingRequest}.
 *
 * @author Luiz Gustavo Pozzo
 * @since 21/04/15.
 */
public class PingRequestDao {

	public long replace(PingRequest pingRequest) {
		SQLiteDatabase db = new ConexaoDBManager().getDb();
		return db.replace(PingRequestCr.TB_NAME, null, PingRequestCr.getValues(pingRequest));
	}
}
