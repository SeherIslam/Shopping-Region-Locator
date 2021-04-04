package com.seher.shoppingregionlocator;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GridItemView extends FrameLayout {

    private TextView textView;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.grid_item_view, this);
        textView = (TextView) getRootView().findViewById(R.id.gridText);
    }

    public void display(String text, boolean isSelected) {
        textView.setText(text);
        display(isSelected);
    }

    public void display(boolean isSelected) {
        textView.setBackgroundResource(isSelected ? R.drawable.selected_item : R.drawable.un_selected_item);
    }
}
