package com.seher.shoppingregionlocator.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.seher.shoppingregionlocator.GridItemView;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends BaseAdapter {
    Context context;
     ArrayList<String> itemNames = new ArrayList<String>();
    public List selectedPositions;
    private Activity activity;

    public  HomeAdapter(Activity activity , ArrayList<String> itemNames){
        this.context=context;
        this.itemNames= itemNames;
        selectedPositions = new ArrayList<>();
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return itemNames.size();
    }

    @Override
    public Object getItem(int position) {
        return itemNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(activity) : (GridItemView) convertView;
        customView.display(itemNames.get(position), selectedPositions.contains(position));

        return customView;
    }
}
