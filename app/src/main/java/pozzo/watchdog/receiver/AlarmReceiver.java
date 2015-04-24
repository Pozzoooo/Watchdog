package pozzo.watchdog.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import pozzo.watchdog.business.WatchEntryBusiness;
import pozzo.watchdog.pojo.WatchEntry;

/**
 * This will be triggered every time we need to automagicaly check for and entry.
 * On previewed check times and on delayed times when network get back.
 *
 * @author Luiz Gustavo Pozzo
 * @since 20/04/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			context.unregisterReceiver(this);
		}

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				//TODO Wake lock
				//TODO add wake lock permission
			}

			@Override
			protected Void doInBackground(Void... params) {
				checkNow(context);
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				//TODO I don't think it will work this way with the preferences...
				//new WatchEntryBusiness().updateNextWatch(context);
			}
		}.execute();
	}

	/**
	 * It will check every entry needed to be checked.
	 *
	 * @param context so we can fallback if necessary.
	 */
	public void checkNow(Context context) {
		WatchEntryBusiness bus = new WatchEntryBusiness();

		try {
			List<WatchEntry> entries = bus.getTriggersBefore(System.currentTimeMillis());
			for (WatchEntry it : entries) {
				bus.ping(it);
			}
		} catch (IOException e) {
			fallback(context);
		}
	}

	/**
	 * Supposed to be called when Receiver fails to watch.
	 */
	private void fallback(Context context) {
		//We enable network receiver, so it will watch it as soon as we have network back
		IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(this, intentFilter);
	}
}
