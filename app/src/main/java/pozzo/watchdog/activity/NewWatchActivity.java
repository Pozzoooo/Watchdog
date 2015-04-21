package pozzo.watchdog.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import pozzo.watchdog.ItemMenuHelper;
import pozzo.watchdog.R;
import pozzo.watchdog.business.WatchEntryBusiness;
import pozzo.watchdog.pojo.WatchEntry;
import pozzo.watchdog.view.FrequencySeekBar;

/**
 * Activity to create a new {@link WatchEntry} object and send it to persistence.
 *
 * @author Luiz Gustavo Pozzo
 * @since 17/04/15.
 */
public class NewWatchActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_watch);

		//Our choice action bar
		ItemMenuHelper.setDoneDiscard(getSupportActionBar(), this);
	}

	/**
	 * Screen action bar button event.
	 */
	public void onDone(View view) {
		new AsyncTask<Void, Void, Long>() {
			@Override
			protected Long doInBackground(Void... params) {
				WatchEntry watchEntry = getObject();
				return new WatchEntryBusiness().replace(watchEntry);
			}

			@Override
			protected void onPostExecute(Long aLong) {
				//We expect an Id to be generated by database as a success sign
				setResult(aLong >= 0 ? RESULT_OK : RESULT_CANCELED);
				finish();
			}
		}.execute();
	}

	/**
	 * Screen action bar button event.
	 */
	public void onDiscard(View view) {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * Screen button event.
	 */
	public void onHelp(View view) {
		//TODO show help popup
	}

	/**
	 * @return object inserted on screen fileds.
	 */
	public WatchEntry getObject() {
		//finding components
		EditText eAddress = (EditText) findViewById(R.id.eAddress);
		RadioGroup rgRequestType = (RadioGroup) findViewById(R.id.rgRequestType);
		EditText ePortNumber = (EditText) findViewById(R.id.ePortNumber);
		CheckBox cbAlarm = (CheckBox) findViewById(R.id.cbAlarm);
		CheckBox cbSendEmail = (CheckBox) findViewById(R.id.cbSendEmail);
		EditText eEmail = (EditText) findViewById(R.id.eEmail);
		FrequencySeekBar sbFrequency = (FrequencySeekBar) findViewById(R.id.sbFrequency);
		CheckBox cbCanBeDelayed = (CheckBox) findViewById(R.id.cbCanBeDelayed);

		//filling objects
		WatchEntry watchEntry = new WatchEntry();
		watchEntry.setAddress(eAddress.getText().toString());
		watchEntry.setPort(Integer.parseInt(ePortNumber.getText().toString()));
		watchEntry.setAlarm(cbAlarm.isChecked());
		watchEntry.setFrequency(sbFrequency.getProgressInMilliseconds());
		watchEntry.setCanBeDelayed(cbCanBeDelayed.isChecked());

		//dependents fields
		if(cbSendEmail.isChecked())
			watchEntry.setEmail(eEmail.getText().toString());

		//radio buttons
		switch (rgRequestType.getCheckedRadioButtonId()) {
			case R.id.rbGet:
				watchEntry.setRequestType("get");
				break;
			default:
				//TODO Send report to bugsense
			case R.id.rbPing:
				watchEntry.setRequestType("ping");
				break;
		}

		return watchEntry;
	}
}