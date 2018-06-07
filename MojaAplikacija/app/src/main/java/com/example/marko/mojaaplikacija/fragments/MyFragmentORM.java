package com.example.marko.mojaaplikacija.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.marko.mojaaplikacija.PostsActivity;
import com.example.marko.mojaaplikacija.R;
import com.example.marko.mojaaplikacija.provider.PostContract;
import com.example.marko.mojaaplikacija.provider.model.Post;
import com.j256.ormlite.dao.CloseableIterator;

/**
 * Created by marko on 21.5.18..
 */

public class MyFragmentORM extends ListFragment {

    private Cursor c;
    private PostsActivity activity;

    private CloseableIterator<Post> iterator;

    public interface OnProductSelectedListener {
        void onProductSelected(int id);
    }

    OnProductSelectedListener listener;
    ListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=((PostsActivity)getActivity());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        /**
         * Na svaki klik, uzimamo kursor adaptera i trazimo _id elmenta
         * sa pozicije koji smo kliknuli.
         *
         * NAPOMENA: biblioteka zahteva postojanje _id elementa _id nije isto sto i id!
         * */

        Cursor cur = (Cursor) adapter.getItem(position);
        cur.moveToPosition(position);
        String cID = cur.getString(cur.getColumnIndexOrThrow("_id"));

        //Kada dobijemo kljuc, pretvorimo ga u Integer i izazivamo klik doagadjaj
        listener.onProductSelected(Integer.parseInt(cID));
    }

    @Override
    public void onResume() {
        super.onResume();
        c = activity.getContentResolver().query(PostContract.Post.contentUri, null, null, null, null);
        if (c != null) {

            //iz kursora izvlacimo kolone baze koji nas zanimaju
            String[] from = new String[] { PostContract.Post.FIELD_NAME_TITLE, PostContract.Post.FIELD_NAME_DESCRIPTION};

            //i smestamo u nas layout na poziciju gde zelimo, pozicije su opisane id-om elemenata
            int[] to = new int[] {R.id.txtView1, R.id.txtView2};

            //inicijalizujemo adapter da se sam osvezava
            adapter = new SimpleCursorAdapter(activity, R.layout.list_item, c, from, to, 0);

            //ListFrafgment vec u sebi ima listu sto znaci daje potrebno da samo dodamo adapter
            setListAdapter(adapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (c != null) {
            c.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.map_layout, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnProductSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnProductSelectedListener");
        }
    }





}
