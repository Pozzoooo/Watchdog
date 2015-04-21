package pozzo.watchdog.business;

import java.util.Date;

import pozzo.watchdog.dao.WatchEntryDao;
import pozzo.watchdog.pojo.WatchEntry;

/**
 * Created by ghost on 19/04/15.
 */
public class WatchEntryBusiness {
	public long replace(WatchEntry watchEntry) {
		//We always request it to be watched rigth after insert or update
		watchEntry.setNextWatch(new Date().getTime());

		return new WatchEntryDao().replace(watchEntry);
	}

	public WatchEntry get(long id) {
		return new WatchEntryDao().get(id);
	}

	/**
	 * Get the next entry which should be checked.
	 *
	 * @param canBeDelayed true or false...
	 * @return Object returned by query or null if not found.
	 */
	public WatchEntry getNext(boolean canBeDelayed) {
		return new WatchEntryDao().getNext(canBeDelayed);
	}
}
