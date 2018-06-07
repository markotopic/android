package com.example.marko.mojaaplikacija.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marko.mojaaplikacija.R;
import com.example.marko.mojaaplikacija.tools.NavigationItem;

import java.util.ArrayList;

/**
 * Created by marko on 23.5.18..
 */

public class DrawerListAdapter extends BaseAdapter {

    Context context;
    ArrayList<NavigationItem> navigationItems;

    public DrawerListAdapter(Context context, ArrayList<NavigationItem> navigationItems) {
        this.context = context;
        this.navigationItems = navigationItems;
    }

    @Override
    public int getCount() {
        return navigationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_list_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText( navigationItems.get(position).getTitle() );
        subtitleView.setText( navigationItems.get(position).getSubtitle() );
        iconView.setImageResource(navigationItems.get(position).getIcon());

        return view;
    }

}
