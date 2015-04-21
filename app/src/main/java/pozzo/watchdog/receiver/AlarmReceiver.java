package pozzo.watchdog.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pozzo.watchdog.util.ReceiverUtil;

/**
 * Created by ghost on 20/04/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//TODO Here I should tigger Ping and check all.

		//if all goes wrong I fallback
		fallback(context);
	}

	/**
	 * Supposed to be called when Receiver fails to watch.
	 */
	private void fallback(Context context) {
		//We enable NetworkReceiver, so it will watch it as soon as we have network back
		ReceiverUtil.enableReceiver(context, NetworkReceiver.class);
	}
}
