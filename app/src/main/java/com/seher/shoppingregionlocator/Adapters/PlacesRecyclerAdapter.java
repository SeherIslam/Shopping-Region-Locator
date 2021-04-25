package com.seher.shoppingregionlocator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.seher.shoppingregionlocator.Map.data.api.URLBuilder;
import com.seher.shoppingregionlocator.Map.data.entity.Places;
import com.seher.shoppingregionlocator.Map.ui.ImageLoader;

import java.util.List;
import com.seher.shoppingregionlocator.R;


public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder>
{
    private OnItemClickListener clickListener;
    private Context context;
    private List<Places> placesList;

    public PlacesRecyclerAdapter(Context context, List<Places> placesList)
    {
        super();

        this.placesList = placesList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_recyclerview_layout, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i)
    {
        viewHolder.bindData(placesList.get(i));
        viewHolder.ib_favourite.setTag(i);
    }


    @Override
    public int getItemCount()
    {
        return placesList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tv_name;
        TextView tv_location;
        TextView tv_rating;
        TextView tv_opening_hour;
        TextView placeType;
        ImageView iv_thumbnail;
        ImageButton ib_favourite;

        public ViewHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);

            tv_name = itemView.findViewById(R.id.tvName);
            tv_location = itemView.findViewById(R.id.tvLocation);
            tv_rating = itemView.findViewById(R.id.tvRating);
            tv_opening_hour = itemView.findViewById(R.id.tvOpeningHour);
            placeType = itemView.findViewById(R.id.placeType);
            iv_thumbnail = itemView.findViewById(R.id.thumbnail);
            ib_favourite = itemView.findViewById(R.id.ibFavorite);

            ib_favourite.setOnClickListener(onFavouriteClickListener);
        }

        private View.OnClickListener onFavouriteClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                int index = (int) v.getTag();

                switch (v.getId())
                {
                    case R.id.ibFavorite:

                        clickListener.onFavoriteClick(v, index);
                        break;
                }
            }
        };


        @Override
        public void onClick(View v)
        {
            clickListener.onItemClick(v, getAdapterPosition());
        }


        private void bindData(Places places)
        {
            tv_name.setText(places.getName());
            tv_location.setText(places.getAddress());
            tv_rating.setText(String.valueOf(places.getRating()));
            placeType.setText(String.valueOf(places.getType()));

            if(places.getRating() >= 3)
            {
                tv_rating.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rating_background_green));
            }

            else if(places.getRating() >= 2)
            {
                tv_rating.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rating_background_yellow));
            }

            else
            {
                tv_rating.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rating_background_red));
            }

            if(places.isOpen_now())
            {
                tv_opening_hour.setTextColor(ContextCompat.getColor(context, R.color.green));
                tv_opening_hour.setText("Open");
            }

            else
            {
                tv_opening_hour.setTextColor(ContextCompat.getColor(context, R.color.red));
                tv_opening_hour.setText("Closed");
            }

            if(places.isIs_favourite())
            {
                ib_favourite.setImageResource(R.drawable.ic_favorite_red);
            }

            else
            {
                ib_favourite.setImageResource(R.drawable.ic_favorite_grey);
            }

            loadThumbnail(URLBuilder.thumbURL(places.getPhoto_reference()),places.getType());
        }


        private void loadThumbnail(final String url, String type)
        {
            try
            {
                if(type.toLowerCase().equals("atm") || type.toLowerCase().equals("finance") || type.toLowerCase().equals("bank")) {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.atm, 75, 75);
                    }
                }
                else if(type.toLowerCase().equals("cafe") ) {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.cafe, 75, 75);
                    }
                }
                else if(type.toLowerCase().equals("clothing_store") ) {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.cloths, 75, 75);
                    }
                }
                else if(type.toLowerCase().equals("electronics_store") ) {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.electronics, 75, 75);
                    }
                }
                else if(type.toLowerCase().equals("food") ) {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.food, 75, 75);
                    }
                }
                else if(type.toLowerCase().equals("jewelry_store") ) {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.jewellery, 75, 75);
                    }
                }
                else if(type.toLowerCase().equals("shopping_mall") ) {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.shopping, 75, 75);
                    }
                }

                else if(type.toLowerCase().equals("department_store") ) {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.store, 75, 75);
                    }
                }
                else  {
                    if (!url.isEmpty()) {
                        ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.things, 75, 75);
                    }
                }


            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        void onFavoriteClick(View view, int position);
    }


    public void SetOnItemClickListener(final OnItemClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }
}