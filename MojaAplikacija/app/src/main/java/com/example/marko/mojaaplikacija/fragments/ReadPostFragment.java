package com.example.marko.mojaaplikacija.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.LoginActivity;
import com.example.marko.mojaaplikacija.R;
import com.example.marko.mojaaplikacija.services.PostService;
import com.example.marko.mojaaplikacija.services.ServiceUtils;
import com.example.marko.mojaaplikacija.services.TagService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Post;
import model.Tag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by marko on 1.6.18..
 */

public class ReadPostFragment extends Fragment {

    View view;

    private Post post;

    private TagService tagService;
    private PostService postService;

    private TextView tags_view;
    private TextView tag_view;
    private ImageButton like_view;
    private ImageButton dislike_view;

    private List<Tag> tags;
    private LinearLayout linearLayout;
    private LinearLayout newLinearLayout;
    private int counterLikes;
    private int counterDislikes;

    private boolean clickedLike;
    private boolean clickedDislike;

    SharedPreferences sharedPreferences;
    String userNamePref;

    public ReadPostFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.read_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String jsonMyObject = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }
        post = new Gson().fromJson(jsonMyObject, Post.class);

        post.getId();

        TextView title_view = view.findViewById(R.id.title_view);
        title_view.setText(post.getTitle());

        TextView author_view = view.findViewById(R.id.author_view);
        author_view.setText(post.getAuthor().getName());

        TextView date_view = view.findViewById(R.id.date_view);
        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());
        date_view.setText(newDate);

        TextView description_view = view.findViewById(R.id.description_view);
        description_view.setText(post.getDescription());

        final TextView like_text = view.findViewById(R.id.like_text);
        like_text.setText(String.valueOf(post.getLikes()));

        final TextView dislike_text = view.findViewById(R.id.dislike_text);
        dislike_text.setText(String.valueOf(post.getDislikes()));

        linearLayout = view.findViewById(R.id.linear_layout);

        tags_view = view.findViewById(R.id.tags_view);
        tags_view.setText(getActivity().getIntent().getStringExtra("tags"));
        postService = ServiceUtils.postService;
        tagService = ServiceUtils.tagService;

        Call<List<Tag>> call = tagService.getTagsByPost(post.getId());

        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                tags = response.body();

                newLinearLayout = new LinearLayout(getContext());
                newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                String empty = "";
                for (Tag t : tags) {
                    empty += t.getName();
                    tags_view.setText(empty);
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {

            }
        });

        like_view = view.findViewById(R.id.like_view);
        dislike_view = view.findViewById(R.id.dislike_view);

        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.MyPreferances, Context.MODE_PRIVATE);
        userNamePref = sharedPreferences.getString(LoginActivity.Username, "");

        clickedLike = false;
        like_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userNamePref.equals(post.getAuthor().getUsername())) {
                    Toast.makeText(getContext(), "You can't like your post", Toast.LENGTH_SHORT).show();
                } else {
                    if (clickedLike == false) {
                        addLike();
                        like_text.setText(String.valueOf(post.getLikes()));
                        clickedLike = true;
                        dislike_view.setEnabled(false);
                    } else {
                        removeLike();
                        like_text.setText(String.valueOf(post.getLikes()));
                        clickedLike = false;
                        dislike_view.setEnabled(true);
                    }
                }
            }
        });


        clickedDislike = false;
        dislike_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userNamePref.equals(post.getAuthor().getUsername())) {
                    Toast.makeText(getContext(), "You can't dislike your post", Toast.LENGTH_SHORT).show();
                } else {
                    if (clickedDislike == false) {
                        addDislike();
                        dislike_text.setText(String.valueOf(post.getDislikes()));
                        clickedDislike = true;
                        like_view.setEnabled(false);
                    } else {

                        removeDislike();
                        dislike_text.setText(String.valueOf(post.getDislikes()));
                        clickedDislike = false;
                        like_view.setEnabled(false);
                    }

                }
            }
        });

        ImageView image_view = view.findViewById(R.id.image_view);

    }


    public void addLike(){

        counterLikes = post.getLikes();

        post.setLikes(counterLikes+1);

        System.out.println(post.getId());
        System.out.println(post.getLikes());

        Call<Post> call = postService.addLikeDislike(post,post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                like_view.setImageResource(R.drawable.ic_like_green);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    public void addDislike(){

        counterDislikes = post.getDislikes();
        post.setDislikes(counterDislikes+1);

        Call<Post> call = postService.addLikeDislike(post,post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                dislike_view.setImageResource(R.drawable.ic_dislike_red);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }

    public void removeLike(){
        counterLikes = post.getLikes();
        post.setLikes(counterLikes-1);
        Call<Post> call = postService.addLikeDislike(post,post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                like_view.setImageResource(R.drawable.ic_action_like_white);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    public void removeDislike(){
        counterDislikes = post.getDislikes();
        post.setDislikes(counterDislikes - 1);
        Call<Post> call = postService.addLikeDislike(post,post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                dislike_view.setImageResource(R.drawable.ic_action_dislike_white);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

}
