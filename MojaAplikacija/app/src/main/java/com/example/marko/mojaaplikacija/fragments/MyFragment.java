package com.example.marko.mojaaplikacija.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.R;
import com.example.marko.mojaaplikacija.ReadPostActivity;
import com.example.marko.mojaaplikacija.adapters.PostAdapter;
import com.example.marko.mojaaplikacija.adapters.PostAdapter1;
import com.example.marko.mojaaplikacija.tools.Mokap;

import model.Post;

/**
 * Created by marko on 24.4.18..
 */

public class MyFragment extends ListFragment {

    public static MyFragment newInstance() {

        MyFragment mpf = new MyFragment();

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

        Post post = Mokap.getPosts().get(position);

        Intent intent = new Intent(getActivity(), ReadPostActivity.class);
        intent.putExtra("title", post.getTitle());
        intent.putExtra("descr", post.getDescription());
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "onActivityCreated()", Toast.LENGTH_SHORT).show();

        //Dodaje se adapter
        PostAdapter1 adapter = new PostAdapter1(getActivity());
        setListAdapter(adapter);
    }



}
