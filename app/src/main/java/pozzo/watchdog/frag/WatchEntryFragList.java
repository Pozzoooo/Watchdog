package pozzo.watchdog.frag;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pozzo.watchdog.R;
import pozzo.watchdog.SimpleCursorLoader;
import pozzo.watchdog.adapter.WatchEntryAdapter;
import pozzo.watchdog.db.ConexaoDBManager;
import pozzo.watchdog.db.WatchEntryCr;

/**
 * List containing {@link pozzo.watchdog.pojo.WatchEntry} saved on database.
 *
 * @author Luiz Gustavo Pozzo
 * @since 18/04/15.
 *
 * TODO Implement search
 */
public class WatchEntryFragList extends ListFragment
		implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {
	private ConexaoDBManager conexao;
	private SQLiteDatabase loaderDb;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.watch_entry_list, container, false);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(1, null, this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		conexao = new ConexaoDBManager(activity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		conexao.close();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new SimpleCursorLoader(getActivity()) {
			@Override
			public Cursor loadInBackground() {
				loaderDb = conexao.getDb();
				return loaderDb.query(WatchEntryCr.TB_NAME, null, null, null, null, null, null);
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		WatchEntryAdapter adapter = new WatchEntryAdapter(getActivity(), data, 0);
		setListAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		setListAdapter(null);
		loaderDb.close();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
}
