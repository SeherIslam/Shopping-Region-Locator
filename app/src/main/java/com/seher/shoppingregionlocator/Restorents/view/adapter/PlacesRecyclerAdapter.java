package com.seher.shoppingregionlocator.Restorents.view.adapter;

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

import com.seher.shoppingregionlocator.Restorents.data.api.URLBuilder;
import com.seher.shoppingregionlocator.Restorents.data.entity.Places;
import com.seher.shoppingregionlocator.Restorents.ui.ImageLoader;

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

            loadThumbnail(URLBuilder.thumbURL(places.getPhoto_reference()));
        }


        private void loadThumbnail(final String url)
        {
            try
            {
                if(!url.isEmpty())
                {
                    ImageLoader.loadThumbnail(context, url, iv_thumbnail, R.drawable.restaurant, 75, 75);
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