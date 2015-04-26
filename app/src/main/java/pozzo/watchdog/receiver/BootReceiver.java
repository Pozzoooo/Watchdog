package pozzo.watchdog.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pozzo.watchdog.business.WatchEntryBusiness;

/**
 * Our boot receiver should run only when there is active WatchEntry to be watched.
 *
 * @author Luiz Gustavo Pozzo
 * @since 20/04/15.
 */
public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//We simplely start our watch dog alarm
		new WatchEntryBusiness().updateNextWatch(context);
	}
}
