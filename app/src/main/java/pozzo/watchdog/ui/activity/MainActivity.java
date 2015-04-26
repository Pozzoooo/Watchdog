package pozzo.watchdog.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import pozzo.watchdog.pojo.WatchEntry;

import pozzo.watchdog.R;

/**
 * Will built app flow using a list with {@link WatchEntry} and a menu for actions.
 * //TODO long click for action menu
 * //TODO check if insertion is ok and list is showing entries
 *
 * @author Luiz Gustavo Pozzo
 * @since 17/04/15
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.mNew:
				Intent intent = new Intent(this, NewWatchActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
