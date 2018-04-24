package com.example.marko.mojaaplikacija.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marko.mojaaplikacija.R;
import com.example.marko.mojaaplikacija.tools.Mokap;

import model.Post;

/**
 * Created by marko on 24.4.18..
 */

public class PostAdapter1 extends BaseAdapter {

    private Activity activity;

    public PostAdapter1(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return Mokap.getPosts().size();
    }

    @Override
    public Object getItem(int position) {
        return Mokap.getPosts().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        Post post = Mokap.getPosts().get(position);

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.list_item, null);

        TextView title = (TextView)vi.findViewById(R.id.txtView1);
        TextView description = (TextView)vi.findViewById(R.id.txtView2);

        title.setText(post.getTitle());
        description.setText(post.getDescription());

        return  vi;
    }


}
