package pozzo.watchdog.business;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import pozzo.watchdog.App;
import pozzo.watchdog.Ping;
import pozzo.watchdog.R;
import pozzo.watchdog.db.dao.PingRequestDao;
import pozzo.watchdog.db.dao.WatchEntryDao;
import pozzo.watchdog.pojo.PingRequest;
import pozzo.watchdog.pojo.WatchEntry;
import pozzo.watchdog.receiver.AlarmReceiver;
import pozzo.watchdog.receiver.BootReceiver;
import pozzo.watchdog.util.ReceiverUtil;

/**
 * This is probably the most important class.
 * All Watchdog flow pass through here.
 *
 * @author Luiz Gustavo Pozzo
 * @since 19/04/15.
 *
 * TODO for the sake of perfomance I should iplement a batch watch
 */
public class WatchEntryBusiness {
	private static final String NEXT_CAN_BE_DELAYED = "nextCanBeDelayed";
	private static final String NEXT_CANNOT_BE_DELAYED = "nextCannotBeDelayed";

	public long replace(Context context, WatchEntry watchEntry) {
		//We always request it to be watched rigth after insert or update
		watchEntry.setNextWatch(new Date().getTime());

		//Persist
		long _id = new WatchEntryDao().replace(watchEntry);
		watchEntry.setId(_id);

		//Schedule
		checkUpdateNextWatch(context, watchEntry);
		return _id;
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

	/**
	 * Get every entry that should be watched before given timestamp.
	 *
	 * @param timestamp to compared on database.
	 * @return All entries to be watched before given timestamp.
	 */
	public List<WatchEntry> getTriggersBefore(long timestamp) {
		return new WatchEntryDao().getTriggersBefore(timestamp);
	}

	/**
	 * It calculates and update next time this entry will be watchned.
	 *
	 * @param watchEntry to be updated.
	 */
	public void updateToNextWatch(WatchEntry watchEntry) {
		long frequency = watchEntry.getFrequency();
		watchEntry.setNextWatch(System.currentTimeMillis() + frequency);
		new WatchEntryDao().replace(watchEntry);

		//Schedule
		checkUpdateNextWatch(App.getAppContext(), watchEntry);
	}

	/**
	 * It will check if WatchEntry is alive.
	 *
	 * @param watchEntry to be checked.
	 * @return Latency from the request.
	 * @throws IOException Not able to reach network.
	 */
	public long watch(WatchEntry watchEntry) throws IOException {
		long latency = ping(watchEntry);
		//If an exception happens, update should never run
		updateToNextWatch(watchEntry);
		return latency;
	}

	/**
	 * It will execute the ping rountine for the given entry and updates the next time to check.
	 *
	 * @param watchEntry to be checked.
	 * @return Latency from the request.
	 * @throws IOException Not able to reach network.
	 */
	public long ping(WatchEntry watchEntry) throws IOException {
		long latency = new Ping().pingRequestTcp(watchEntry.getAddress(), watchEntry.getPort());

		PingRequest request = new PingRequest();
		request.setFkWatchEntry(watchEntry.getId());
		request.setDate(System.currentTimeMillis());
		request.setLatency(latency);
		//TODO get network name, if it takes too long, maybe I can run it in parallel
		new PingRequestDao().replace(request);
		return latency;
	}

	/**
	 * This method is responsible for controlling when AlarmManger will trigger to a new WatchDog
	 * 	check.
	 * We check if this entry may be earlier than what we alredy have and update if needed.
	 * It is expected to be called every time we have a new WatchEntry inserted on the system and on
	 * 	system boot.
	 * Times are saved on preferences, so we don't need to query database for every new entry and
	 * 	especially we do not need to query on boot (trying to not delay user boot).
	 *
	 * @param watchEntry to be checked.
	 */
	private void checkUpdateNextWatch(Context context, WatchEntry watchEntry) {
		long nextWatch;
		SharedPreferences pref = context.getSharedPreferences(
				context.getString(R.string.pref_service), Context.MODE_PRIVATE);
		//Prefered this way to make code looks cleaner and less copy paste
		String checkKey = watchEntry.isCanBeDelayed()
				? NEXT_CAN_BE_DELAYED : NEXT_CANNOT_BE_DELAYED;

		//Max value will be used to active states
		nextWatch = pref.getLong(checkKey, Long.MAX_VALUE);

		if(nextWatch > watchEntry.getNextWatch()) {
			SharedPreferences.Editor prefEditor = context.getSharedPreferences(
					context.getString(R.string.pref_service), Context.MODE_PRIVATE).edit();
			prefEditor.putLong(checkKey, watchEntry.getNextWatch());
			prefEditor.commit();
			updateNextWatch(context);

			//We enable boot receiver if it seems not be enabled before.
			if(nextWatch == Long.MAX_VALUE) {
				ReceiverUtil.enableReceiver(context, BootReceiver.class);
			}
		}
	}

	/**
	 * Updates alarm to receive on next moment to check.
	 */
	public void updateNextWatch(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				context.getString(R.string.pref_service), Context.MODE_PRIVATE);
		long canBeDelayed = pref.getLong(NEXT_CAN_BE_DELAYED, 0l);
		long cannotBeDelayed = pref.getLong(NEXT_CANNOT_BE_DELAYED, 0l);

		//Just init common variables
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent;

		//We only set a alarm for canBeDelayed if it happens before the cannotBeDelayed entry
		if(canBeDelayed != 0l && canBeDelayed < cannotBeDelayed) {
			pendingIntent = PendingIntent
					.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			alarmManager.set(AlarmManager.RTC, canBeDelayed, pendingIntent);
		}

		//cannotBeDelayed will always be set, we need make sure that it runs in the correct time
		if(cannotBeDelayed != 0l) {
			pendingIntent = PendingIntent
					.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			alarmManager.set(AlarmManager.RTC_WAKEUP, cannotBeDelayed, pendingIntent);
		}
	}
}
