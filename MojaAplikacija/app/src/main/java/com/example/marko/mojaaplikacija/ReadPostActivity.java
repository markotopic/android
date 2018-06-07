package com.example.marko.mojaaplikacija;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.adapters.DrawerListAdapter;
import com.example.marko.mojaaplikacija.adapters.ViewPagerAdapter;
import com.example.marko.mojaaplikacija.database.AplikacijaSQLiteHelper;
import com.example.marko.mojaaplikacija.fragments.CommentsFragment;
import com.example.marko.mojaaplikacija.fragments.ReadFragment;
import com.example.marko.mojaaplikacija.fragments.ReadPostFragment;
import com.example.marko.mojaaplikacija.services.PostService;
import com.example.marko.mojaaplikacija.services.ServiceUtils;
import com.example.marko.mojaaplikacija.services.UserService;
import com.example.marko.mojaaplikacija.tools.NavigationItem;
import com.google.gson.Gson;

import java.util.ArrayList;

import model.Post;
import model.Tag;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPostActivity extends AppCompatActivity {
    private Uri todoUri;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mTitle;
    private ArrayList<NavigationItem> mNavItems = new ArrayList<NavigationItem>();
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    private Post post = new Post();
    private User user = new User();
    private Tag tag = new Tag();
    private ArrayList<Tag> tags = new ArrayList<>();

    String userNamePref;
    private SharedPreferences sharedPreferences;

    private PostService postService;
    private UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tablayout);
//        appBarLayout = findViewById(R.id.app_bar);
        viewPager = findViewById(R.id.viewpager);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(new ReadPostFragment(),"Read post");
        pagerAdapter.AddFragment(new CommentsFragment(),"Comments");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        prepareMenu(mNavItems);
        mTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.nav_list);
        mDrawerPane = findViewById(R.id.drawer_pane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new ReadPostActivity.DrawerItemClickListener());
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

        //>>

        TextView textViewUser = findViewById(R.id.user);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPreferances,Context.MODE_PRIVATE);
        if(sharedPreferences.contains(LoginActivity.Username)){
            textViewUser.setText(sharedPreferences.getString(LoginActivity.Name,""));
        }

        String jsonMyObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }
        post = new Gson().fromJson(jsonMyObject, Post.class);

        System.out.println(post.getId());
        postService = ServiceUtils.postService;
        userService = ServiceUtils.userService;

        userNamePref = sharedPreferences.getString(LoginActivity.Username,"");

        Call<User> call = userService.getUserByUsername(userNamePref);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        invalidateOptionsMenu();

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void prepareMenu(ArrayList<NavigationItem> mNavItems ){
        mNavItems.add(new NavigationItem(getString(R.string.create_post),getString(R.string.create_post_long),R.drawable.ic_action_add));
        mNavItems.add(new NavigationItem(getString(R.string.preferances), getString(R.string.preferance_long), R.drawable.ic_action_settings));
        mNavItems.add(new NavigationItem(getString(R.string.logout),getString(R.string.logout_long),R.drawable.ic_logout));
    }

    private void selectItemFromDrawer(int position) {
        if (position == 0){
            Intent createIntent = new Intent(this,CreatePostActivity.class);
            startActivity(createIntent);
        } else if (position == 1){
            Intent settings = new Intent(ReadPostActivity.this,SettingsActivity.class);
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

    private void fillData(Uri todoUri) {
        String[] allColumns = { AplikacijaSQLiteHelper.COLUMN_ID,
                AplikacijaSQLiteHelper.COLUMN_TITLE, AplikacijaSQLiteHelper.COLUMN_DESCRIPTION};

        Cursor cursor = getContentResolver().query(todoUri, allColumns, null, null,
                null);

        cursor.moveToFirst();
        Post post = createPost(cursor);

//        TextView title = (TextView)findViewById(R.id.lbltitle);
//        TextView decr = (TextView)findViewById(R.id.lbltext);
//
//        title.setText(post.getTitle());
//        decr.setText(post.getDescription());

        cursor.close();

    }

    private Post createPost(Cursor cursor) {
        Post post = new Post();
        post.setId(cursor.getInt(0));
        post.setTitle(cursor.getString(1));
        post.setDescription(cursor.getString(2));
        return post;

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
        finish();
        startActivity(getIntent());
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

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent settings = new Intent(ReadPostActivity.this,SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.action_add:
                Toast.makeText(this, "New post", Toast.LENGTH_SHORT).show();
                Intent createIntent = new Intent(this,CreatePostActivity.class);
                startActivity(createIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void delete(){
        Call<Post> call = postService.deletePost(post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }
}
