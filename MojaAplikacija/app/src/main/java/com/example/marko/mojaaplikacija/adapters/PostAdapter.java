package com.example.marko.mojaaplikacija.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.marko.mojaaplikacija.R;

import java.util.ArrayList;
import java.util.List;

import model.Post;

/**
 * Created by marko on 23.4.18..
 */

public class PostAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private List<Post> postsList = new ArrayList<>();

    public PostAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Post> list) {
        super(context, 0 , list);
        mContext = context;
        postsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        }

        Post currentPost = postsList.get(position);

        TextView title = (TextView)listItem.findViewById(R.id.txtView1);
        title.setText(currentPost.getTitle());

        TextView opis = (TextView)listItem.findViewById(R.id.txtView2);
        opis.setText(currentPost.getDescription());


        return listItem;
    }

}
