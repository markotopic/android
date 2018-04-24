package com.example.marko.mojaaplikacija;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.adapters.PostAdapter;
import com.example.marko.mojaaplikacija.fragments.MyFragment;
import com.example.marko.mojaaplikacija.tools.FragmentTransition;

import java.util.ArrayList;

import model.Post;

public class PostsActivity extends AppCompatActivity {

    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransition.to(MyFragment.newInstance(), this, false);


//        ListView listView = (ListView) findViewById(R.id.lista);
//        final ArrayList<Post> postList = new ArrayList<>();
//        postList.add(new Post("Naslov 1", "Neki opis 1"));
//        postList.add(new Post("Naslov 2", "Neki opis 2"));
//        postList.add(new Post("Naslov 3", "Neki opis 3"));
//
//        final PostAdapter pAdapter = new PostAdapter(this, postList);
//        listView.setAdapter(pAdapter);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void btnStartCreatePostActivity(View view) {
        Intent intent = new Intent(PostsActivity.this, CreatePostActivity.class);
        startActivity(intent);
    }

    public void btnStartReadPostActivity(View view) {
        Intent intent = new Intent(PostsActivity.this, ReadPostActivity.class);
        startActivity(intent);
    }

    public void btnStartSettingsActivity(View view) {
        Intent intent = new Intent(PostsActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_add:
                Toast.makeText(this, "New post", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
