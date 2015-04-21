package pozzo.watchdog.util;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by ghost on 20/04/15.
 */
public class ReceiverUtil {

	/**
	 * Enables receivers.
	 *
	 * @param context needed to enable.
	 * @param claz which will be enabled.
	 */
	public static void enableReceiver(Context context, Class<? extends BroadcastReceiver> claz) {
		ComponentName receiver = new ComponentName(context, claz);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	/**
	 * Disables receiver.
	 *
	 * @param context needed to disable.
	 * @param claz which will be disabled.
	 */
	public static void disableReceiver(Context context, Class<? extends BroadcastReceiver> claz) {
		ComponentName receiver = new ComponentName(context, claz);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}
}
