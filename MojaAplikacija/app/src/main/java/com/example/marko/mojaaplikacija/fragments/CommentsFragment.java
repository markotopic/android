package com.example.marko.mojaaplikacija.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.LoginActivity;
import com.example.marko.mojaaplikacija.R;
import com.example.marko.mojaaplikacija.adapters.CommentListAdapter;
import com.example.marko.mojaaplikacija.services.CommentService;
import com.example.marko.mojaaplikacija.services.ServiceUtils;
import com.example.marko.mojaaplikacija.services.UserService;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Comment;
import model.Post;
import model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by marko on 1.6.18..
 */

public class CommentsFragment extends Fragment {

    View view;

    private CommentService commentService;
    private UserService userService;

    private Post post;
    private User user;

    private List<Comment> comments;
    private ListView listView;
    private CommentListAdapter commentListAdapter;

    private EditText comment_edit;
    private EditText title_comment_edit;

    private SharedPreferences sharedPreferences;

    private boolean sortCommentsByDate;
    private boolean sortCommentsByPopularity;

    public CommentsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.comment_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String jsonMyObject = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }
        post = new Gson().fromJson(jsonMyObject, Post.class);

        listView = view.findViewById(R.id.comment_list);
        commentService = ServiceUtils.commentService;

        Call<List<Comment>> call = commentService.getCommentsByPost(post.getId());
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                comments = response.body();

                commentListAdapter = new CommentListAdapter(getContext(),comments);
                commentListAdapter.notifyDataSetChanged();
                listView.setAdapter(commentListAdapter);

//                consultPreferences();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });

        userService = ServiceUtils.userService;
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.MyPreferances, Context.MODE_PRIVATE);
        String userNamePref = sharedPreferences.getString(LoginActivity.Username,"");

        Call<User> call_user = userService.getUserByUsername(userNamePref);

        call_user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        title_comment_edit = view.findViewById(R.id.title_comment_edit);
        comment_edit = view.findViewById(R.id.comment_edit);
        Button btn_comment = view.findViewById(R.id.btn_comment);

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
                title_comment_edit.setText("");
                comment_edit.setText("");
                FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                t.setAllowOptimization(false);
                t.detach(CommentsFragment.this).attach(CommentsFragment.this).commitAllowingStateLoss();
            }
        });

        setUp();
    }


    private void addComment(){
        Comment comment = new Comment();
        String title = title_comment_edit.getText().toString();
        comment.setTitle(title);
        String comment_text = comment_edit.getText().toString();
        comment.setDescription(comment_text);
        Date date = Calendar.getInstance().getTime();
        comment.setDate(date);
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setLikes(0);
        comment.setDislikes(0);
        Call<ResponseBody> call = commentService.addComment(comment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getContext(),"Added comment",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void setUp(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }



}
