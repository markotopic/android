package com.example.marko.mojaaplikacija;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.ORMdatabase.PostHelper;
import com.example.marko.mojaaplikacija.adapters.DrawerListAdapter;
import com.example.marko.mojaaplikacija.adapters.PostAdapter;
import com.example.marko.mojaaplikacija.adapters.PostAdapter2;
import com.example.marko.mojaaplikacija.fragments.MyFragment;
import com.example.marko.mojaaplikacija.fragments.MyFragmentDB;
import com.example.marko.mojaaplikacija.fragments.MyFragmentORM;
import com.example.marko.mojaaplikacija.fragments.ReadFragment;
import com.example.marko.mojaaplikacija.provider.PostContract;
import com.example.marko.mojaaplikacija.services.PostService;
import com.example.marko.mojaaplikacija.services.ServiceUtils;
import com.example.marko.mojaaplikacija.tools.FragmentTransition;
import com.example.marko.mojaaplikacija.tools.NavigationItem;
import com.example.marko.mojaaplikacija.tools.Util;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;
    private CharSequence drawerTitle;
    private CharSequence title;
    private ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();
    private AlertDialog dialog;
    private boolean landscapeMode = false;
    private boolean listShown = false;
    private boolean detailShown = false;
    private int productId = 0;

    private PostHelper databaseHelper;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mTitle;
    private ArrayList<NavigationItem> mNavItems = new ArrayList<NavigationItem>();

    private PostService postService;
    private ListView listView;
    private List<Post> posts;
    private PostAdapter2 postListAdapter;
    private Post post = new Post();
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepareMenu(mNavItems);
        mTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.nav_list);
        mDrawerPane = findViewById(R.id.drawer_pane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ){
            public void onDrawerClosed(View view){
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle("MojaApp");
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

//        setContentView(R.layout.main1);
        //>>>

        listView = findViewById(R.id.lista);
        postService = ServiceUtils.postService;

        Call call = postService.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                posts = response.body();
                postListAdapter = new PostAdapter2(getApplicationContext(),posts);
                listView.setAdapter(postListAdapter);
//                consultPreferences();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                post = posts.get(i);

                Call<Post> call = postService.getPost(post.getId());

                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        post = response.body();
                        Intent intent = new Intent(PostsActivity.this,ReadPostActivity.class);
                        intent.putExtra("Post",new Gson().toJson(post));

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {

                    }
                });
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void addItemWithProvider(){
        // insert test
        ContentValues values = new ContentValues();
        values.clear();
        values.put(PostContract.Post.FIELD_NAME_TITLE, "apple");
        values.put(PostContract.Post.FIELD_NAME_DESCRIPTION,"The apple tree is a deciduous tree in the rose family best known for its sweet, pomaceous fruit, the apple.");
//        values.put(ProductContract.Product.FIELD_NAME_RATING, 5.0f);
//        values.put(ProductContract.Product.FIELD_NAME_IMAGE, "apples.jpg");
        getContentResolver().insert(PostContract.Post.contentUri, values);

        Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();

        Cursor c = getContentResolver().query(PostContract.Post.contentUri, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()){
                for (int i = 0; i < c.getColumnCount(); i++)
                {
                    Log.i("REZ", c.getColumnName(i) + " : " + c.getString(i));
                }
            }
            //zatvorimo kursor obavezno!
            c.close();
        }

        finish();
        startActivity(getIntent());
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

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent settings = new Intent(PostsActivity.this,SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.action_add:
                Toast.makeText(this, "New post", Toast.LENGTH_SHORT).show();
                Intent createIntent = new Intent(this,CreatePostActivity.class);
                startActivity(createIntent);
//                addItemWithProvider();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareMenu(ArrayList<NavigationItem> mNavItems ){
        mNavItems.add(new NavigationItem(getString(R.string.create_post),getString(R.string.create_post_long),R.drawable.ic_action_add));
        mNavItems.add(new NavigationItem(getString(R.string.preferances), getString(R.string.preferance_long), R.drawable.ic_action_settings));
        mNavItems.add(new NavigationItem(getString(R.string.logout),getString(R.string.logout_long),R.drawable.ic_logout));
    }

    //***Zurka pocinje ovde***

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItemFromDrawer(int position) {
        if (position == 0){
            Intent createIntent = new Intent(this,CreatePostActivity.class);
            startActivity(createIntent);
        } else if (position == 1){
            Intent settings = new Intent(PostsActivity.this,SettingsActivity.class);
            startActivity(settings);
        } else if (position == 2){
            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MyPreferances, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.clear();
            editor.commit();
            Intent logoutIntent = new Intent(this, LoginActivity.class);
            startActivity(logoutIntent);
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void onBackPressed() {
        if (landscapeMode) {
            finish();
        } else if (listShown == true) {
            finish();
        } else if (detailShown == true) {
            getFragmentManager().popBackStack();
            MyFragmentORM myListFragment = new MyFragmentORM();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.displayList, myListFragment, "List_Fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            listShown = true;
            detailShown = false;
        }
    }

    //Metoda koja komunicira sa bazom podataka
    public PostHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, PostHelper.class);
        }
        return databaseHelper;
    }

}
