package pozzo.watchdog.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import pozzo.watchdog.R;
import pozzo.watchdog.pojo.WatchEntry;
import pozzo.watchdog.receiver.AlarmReceiver;
import pozzo.watchdog.receiver.BootReceiver;
import pozzo.watchdog.util.ReceiverUtil;

/**
 * This service is responsible for controlling when AlarmManger will trigger to a new WatchDog
 * 	check.
 * It is expected to be called every time we have a new WatchEntry inserted on the system and on
 * 	system boot.
 * Times are saved on preferences, so we don't need to query database for every new entry and
 * 	especially we do not need to query on boot (trying to not delay user boot).
 *
 * @author Luiz Gustavo Pozzo
 * @since 20/04/15.
 */
public class WatchDogService extends Service {
	private static final String NEXT_CAN_BE_DELAYED = "nextCanBeDelayed";
	private static final String NEXT_CANNOT_BE_DELAYED = "nextCannotBeDelayed";

	@Override
	public IBinder onBind(Intent intent) {
		return null;//Not bound
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		WatchEntry watchEntry = (WatchEntry)
				intent.getSerializableExtra(WatchEntry.class.getName());
		if(watchEntry != null)
			checkUpdateNextWatch(watchEntry);
		else//I suppose it is a boot call, there is no other reason right now
			updateNextWatch();//and being called twice is not that bad

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * We check if this entry may be earlier than what we alredy have and update if needed.
	 *
	 * @param watchEntry to be checked.
	 */
	private void checkUpdateNextWatch(WatchEntry watchEntry) {
		long nextWatch;
		SharedPreferences pref = getSharedPreferences(
				getString(R.string.pref_service), Context.MODE_PRIVATE);
		//Prefered this way to make code looks cleaner and less copy paste
		String checkKey = watchEntry.isCanBeDelayed()
				? NEXT_CAN_BE_DELAYED : NEXT_CANNOT_BE_DELAYED;

		//Max value will be used to active states
		nextWatch = pref.getLong(checkKey, Long.MAX_VALUE);

		if(nextWatch > watchEntry.getNextWatch()) {
			SharedPreferences.Editor prefEditor = getSharedPreferences(
					getString(R.string.pref_service), Context.MODE_PRIVATE).edit();
			prefEditor.putLong(checkKey, watchEntry.getNextWatch());
			prefEditor.commit();
			updateNextWatch();

			//We enable boot receiver if it seems not be enabled before.
			if(nextWatch == Long.MAX_VALUE) {
				ReceiverUtil.enableReceiver(this, BootReceiver.class);
			}
		}
	}

	/**
	 * Updates alarm to receive on next moment to check.
	 */
	private void updateNextWatch() {
		SharedPreferences pref = getSharedPreferences(
				getString(R.string.pref_service), Context.MODE_PRIVATE);
		long canBeDelayed = pref.getLong(NEXT_CAN_BE_DELAYED, 0l);
		long cannotBeDelayed = pref.getLong(NEXT_CANNOT_BE_DELAYED, 0l);

		//Just init common variables
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendingIntent;

		//We only set a alarm for canBeDelayed if it happens before the cannotBeDelayed entry
		if(canBeDelayed != 0l && canBeDelayed < cannotBeDelayed) {
			pendingIntent = PendingIntent
					.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			alarmManager.set(AlarmManager.RTC, canBeDelayed, pendingIntent);
		}

		//cannotBeDelayed will always be set, we need make sure that it runs in the correct time
		if(cannotBeDelayed != 0l) {
			pendingIntent = PendingIntent
					.getBroadcast(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			alarmManager.set(AlarmManager.RTC_WAKEUP, cannotBeDelayed, pendingIntent);
		}
	}
}
