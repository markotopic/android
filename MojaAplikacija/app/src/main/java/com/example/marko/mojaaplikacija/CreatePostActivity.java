package com.example.marko.mojaaplikacija;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.adapters.DrawerListAdapter;
import com.example.marko.mojaaplikacija.services.PostService;
import com.example.marko.mojaaplikacija.services.ServiceUtils;
import com.example.marko.mojaaplikacija.services.TagService;
import com.example.marko.mojaaplikacija.services.UserService;
import com.example.marko.mojaaplikacija.tools.NavigationItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Post;
import model.Tag;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.marko.mojaaplikacija.LoginActivity.Username;

@SuppressWarnings("deprecation")
public class CreatePostActivity extends AppCompatActivity {

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mTitle;
    private ArrayList<NavigationItem> mNavItems = new ArrayList<NavigationItem>();

    private EditText title_edit;
    private EditText description_edit;
    private static EditText tags_edit;

    private UserService userService;
    private PostService postService;
    private static TagService tagService;

    private SharedPreferences sharedPreferences;

    private List<Tag> tagsList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();

    public static Tag tagResponse;
    public static Tag tag;
    public static User user;
    public static Post postResponse;
    public static Post newPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepareMenu(mNavItems);
        mTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.nav_list);
        mDrawerPane = findViewById(R.id.drawer_pane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new CreatePostActivity.DrawerItemClickListener());
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

        title_edit = findViewById(R.id.title_edit);
        description_edit = findViewById(R.id.description_edit);
        tags_edit = findViewById(R.id.tags_edit);

        Button upload_btn = findViewById(R.id.post_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
                title_edit.setText("");
                description_edit.setText("");
                tags_edit.setText("");
            }
        });

        TextView textViewUser = findViewById(R.id.user);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPreferances,Context.MODE_PRIVATE);
        if(sharedPreferences.contains(LoginActivity.Username)){
            textViewUser.setText(sharedPreferences.getString(LoginActivity.Name,""));
        }

        postService = ServiceUtils.postService;
        userService = ServiceUtils.userService;
        tagService = ServiceUtils.tagService;


        String userNamePref = sharedPreferences.getString(Username,"");

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

    }

    public void createPost() {
        final Post post = new Post();

        String title = title_edit.getText().toString();
        String description = description_edit.getText().toString();
        post.setTitle(title);
        post.setDescription(description);
        post.setAuthor(user);
        post.setLikes(0);
        post.setDislikes(0);
        //post.setPhoto(bitmap);
        Date date = Calendar.getInstance().getTime();

        post.setDate(date);
        Call<Post> call = postService.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Snackbar.make(findViewById(R.id.coordinator),"Post is created",Snackbar.LENGTH_SHORT).show();
                postResponse =  response.body();
                System.out.println("-------------------");
                System.out.println(postResponse.getId());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

        addTags();

    }

    public  void addTags(){
        String tagsString = tags_edit.getText().toString().trim();
        String[] separated = tagsString.split("#");

        List<String> tagFilter = Arrays.asList(separated);
        tag = new Tag();
        for(String tagString : tagFilter.subList(1,tagFilter.size())) {
            tag.setName(tagString);
            System.out.println(tag.getName());
            Call<Tag> callTag = tagService.addTag(tag);
            callTag.enqueue(new Callback<Tag>() {
                @Override
                public void onResponse(Call<Tag> call, Response<Tag> response) {
                    System.out.println("****Tag created*****");
                    tagResponse = response.body();
                    System.out.println(postResponse.getId());
                    System.out.println(tagResponse.getId());
                    setTagsInPost(postResponse.getId(),tagResponse.getId());

                }

                @Override
                public void onFailure(Call<Tag> call, Throwable t) {

                }
            });
        }
    }

    public void setTagsInPost(int postId,int tagId){
        Call<Post> call = postService.setTagsInPost(postId,tagId);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                System.out.println("****Added tags*****");
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_post_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectItemFromDrawer(int position) {
        if (position == 0){
            Intent createIntent = new Intent(this,CreatePostActivity.class);
            startActivity(createIntent);
        } else if (position == 1){
            Intent settings = new Intent(CreatePostActivity.this,SettingsActivity.class);
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

    private void prepareMenu(ArrayList<NavigationItem> mNavItems ){
        mNavItems.add(new NavigationItem(getString(R.string.create_post),getString(R.string.create_post_long),R.drawable.ic_action_add));
        mNavItems.add(new NavigationItem(getString(R.string.preferances), getString(R.string.preferance_long), R.drawable.ic_action_settings));
        mNavItems.add(new NavigationItem(getString(R.string.logout),getString(R.string.logout_long),R.drawable.ic_logout));
    }
}
