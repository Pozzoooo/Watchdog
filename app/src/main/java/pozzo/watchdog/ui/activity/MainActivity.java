package pozzo.watchdog.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import pozzo.watchdog.pojo.WatchEntry;

import pozzo.watchdog.R;
import pozzo.watchdog.ui.frag.WatchEntryFragList;

/**
 * Will built app flow using a list with {@link WatchEntry} and a menu for actions.
 * //TODO long click for action menu
 * //TODO check if insertion is ok and list is showing entries
 *
 * @author Luiz Gustavo Pozzo
 * @since 17/04/15
 */
public class MainActivity extends ActionBarActivity {
	private static final int REQ_ADD_WATCHENTRY = 0x01;

	private WatchEntryFragList fragList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragList = (WatchEntryFragList)
				getSupportFragmentManager().findFragmentById(R.id.fragWatchEntries);
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
				Intent intent = new Intent(this, AddWatchActivity.class);
				startActivityForResult(intent, REQ_ADD_WATCHENTRY);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQ_ADD_WATCHENTRY:
				if(resultCode == RESULT_OK)
					fragList.refreshList();
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
		}
	}
}
