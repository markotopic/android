package com.example.marko.mojaaplikacija.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.mojaaplikacija.LoginActivity;
import com.example.marko.mojaaplikacija.R;
import com.example.marko.mojaaplikacija.services.CommentService;
import com.example.marko.mojaaplikacija.services.ServiceUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Comment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by marko on 1.6.18..
 */

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private CommentService commentService;

    private SharedPreferences sharedPreferences;

    String userNamePref;

    public CommentListAdapter(Context context, List<Comment> comments){
        super(context,0,comments);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final Comment comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_item,viewGroup,false);
        }

        TextView title_view = view.findViewById(R.id.title_comment_view);
        TextView author_view = view.findViewById(R.id.author_comment_view);
        TextView date_view = view.findViewById(R.id.date_comment_view);
        String formatedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(comment.getDate());
        TextView comment_view = view.findViewById(R.id.comment_view);
        TextView like_view = view.findViewById(R.id.like_comment_text);
        TextView dislike_view = view.findViewById(R.id.dislike_comment_text);
        ImageButton deleteBtn = view.findViewById(R.id.delete_btn);

        title_view.setText(comment.getTitle());
        author_view.setText(comment.getAuthor().getName());
        date_view.setText(formatedDate);
        comment_view.setText(comment.getDescription());
        like_view.setText(String.valueOf(comment.getLikes()));
        dislike_view.setText(String.valueOf(comment.getDislikes()));


        sharedPreferences  = getContext().getSharedPreferences(LoginActivity.MyPreferances, Context.MODE_PRIVATE);
        userNamePref = sharedPreferences.getString(LoginActivity.Username,"");

        if(!userNamePref.equals(comment.getAuthor().getUsername())){
            deleteBtn.setVisibility(View.INVISIBLE);
        }

        commentService = ServiceUtils.commentService;

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(comment.getId());
                Toast.makeText(getContext(),"Comment is deleted",Toast.LENGTH_SHORT).show();

            }
        });

        return view;

    }

    public void delete(int id){
        Call<Comment> call = commentService.deleteComment(id);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });

        this.notifyDataSetChanged();
    }



}
