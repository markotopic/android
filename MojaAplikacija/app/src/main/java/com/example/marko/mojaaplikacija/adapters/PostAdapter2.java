package com.example.marko.mojaaplikacija.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marko.mojaaplikacija.R;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Post;

/**
 * Created by marko on 26.5.18..
 */

public class PostAdapter2 extends ArrayAdapter<Post> {

    public PostAdapter2(Context context, List<Post> posts){
        super(context,0,posts);
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        final Post post = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item,viewGroup,false);
        }

        final TextView date_view = view.findViewById(R.id.txtView1);
        final TextView title_view = view.findViewById(R.id.txtView2);
//        final ImageView image_view = view.findViewById(R.id.image_view);

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());

        date_view.setText(newDate);
        title_view.setText(post.getTitle());

//        image_view.setImageBitmap(post.getPhoto());

        return view;
    }


}
