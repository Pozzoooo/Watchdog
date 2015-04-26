package pozzo.watchdog.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pozzo.watchdog.R;
import pozzo.watchdog.db.WatchEntryCr;
import pozzo.watchdog.pojo.WatchEntry;

/**
 * Adapter for the {@link WatchEntry} objects using R.layout.row_watch_entry.
 *
 * @author Luiz Gustavo Pozzo
 * @since 19/04/15.
 */
public class WatchEntryAdapter extends CursorAdapter {
	private LayoutInflater inflater;

	public WatchEntryAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View line = inflater.inflate(R.layout.row_watch_entry, parent, false);
		return line;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		WatchEntry watchEntry = WatchEntryCr.objectFrom(cursor);

		TextView lTitle = (TextView) view.findViewById(R.id.lTitle);

		lTitle.setText(watchEntry.getAddress());
	}

	@Override
	public WatchEntry getItem(int position) {
		Cursor cursor = (Cursor) super.getItem(position);
		if(cursor == null)
			return null;
		return WatchEntryCr.objectFrom(cursor);
	}
}
