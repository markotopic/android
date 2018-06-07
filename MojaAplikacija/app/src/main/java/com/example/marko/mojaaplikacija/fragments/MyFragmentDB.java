package com.example.marko.mojaaplikacija.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.marko.mojaaplikacija.R;
import com.example.marko.mojaaplikacija.ReadPostActivity;
import com.example.marko.mojaaplikacija.database.AplikacijaSQLiteHelper;
import com.example.marko.mojaaplikacija.database.DBContentProvider;

/**
 * Created by marko on 13.5.18..
 */

public class MyFragmentDB extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static String USER_KEY = "com.example.marko.mojaaplikacija.USER_KEY";
    private SimpleCursorAdapter adapter;

    public static MyFragmentDB newInstance() {

        MyFragmentDB mpf = new MyFragmentDB();

        return mpf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.map_layout, vg, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), ReadPostActivity.class);
        Uri todoUri = Uri.parse(DBContentProvider.CONTENT_URI_POSTS + "/" + id);
        intent.putExtra("id", todoUri);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Dodaje se adapter
        getLoaderManager().initLoader(0, null, this);
        String[] from = new String[] { AplikacijaSQLiteHelper.COLUMN_TITLE, AplikacijaSQLiteHelper.COLUMN_DESCRIPTION };
        int[] to = new int[] {R.id.txtView1, R.id.txtView2};
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item, null, from,
                to, 0);
        setListAdapter(adapter);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        getActivity().getMenuInflater().inflate(R.menu.activity_itemdetail, menu);
//    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] allColumns = { AplikacijaSQLiteHelper.COLUMN_ID,
                AplikacijaSQLiteHelper.COLUMN_TITLE, AplikacijaSQLiteHelper.COLUMN_DESCRIPTION};

        CursorLoader cursor = new CursorLoader(getActivity(), DBContentProvider.CONTENT_URI_POSTS,
                allColumns, null, null, null);

        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
