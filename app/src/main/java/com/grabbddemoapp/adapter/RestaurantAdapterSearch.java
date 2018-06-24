package com.grabbddemoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grabbddemoapp.R;
import com.grabbddemoapp.data.model.search.Category;
import com.grabbddemoapp.data.model.search.Venue;

import java.util.List;


/**
 * Created by Sandy
 */

public class RestaurantAdapterSearch extends RecyclerView.Adapter<RestaurantAdapterSearch.ViewHolder> {

    private List<Venue> venueList;

    public RestaurantAdapterSearch(final List<Venue> venueList) {

        this.venueList = venueList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_search, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final int adapterPos = holder.getAdapterPosition();
        try {
            holder.tvVenueName.setText(venueList.get(adapterPos).getName());
            holder.tvVenueCity.setText(String.format("%s%s", holder.itemView.getContext().getString(R.string.label_city), venueList.get(adapterPos).getLocation().getCity()));
            holder.tvVenueState.setText(String.format("%s%s", holder.itemView.getContext().getString(R.string.label_state), venueList.get(adapterPos).getLocation().getState()));
            holder.tvVenueCountry.setText(String.format("%s%s", holder.itemView.getContext().getString(R.string.label_country), venueList.get(adapterPos).getLocation().getCountry()));
            for (Category category : venueList.get(adapterPos).getCategories()) {
                holder.tvVenueCategory.append( ", " + category.getName());
            }
            holder.tvVenueCategory.setText(holder.tvVenueCategory.getText().toString().replaceFirst(",", ""));
//            if (venueList.get(adapterPos).getPhotos().getGroups() != null)
//                Glide.with(holder.itemView).load(venueList.get(adapterPos).getVenue().getPhotos().getGroups().get(0).getItems().get(0).getImageUrl()).into(holder.ivVenuePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return venueList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivVenuePic;
        private LinearLayout llDetails;
        private TextView tvVenueName, tvVenueCity, tvVenueState, tvVenueCountry, tvVenueCategory;

        ViewHolder(View itemView) {
            super(itemView);
            ivVenuePic = itemView.findViewById(R.id.ivVenuePic);
            tvVenueName = itemView.findViewById(R.id.tvVenueName);
            tvVenueCity = itemView.findViewById(R.id.tvVenueCity);
            tvVenueState = itemView.findViewById(R.id.tvVenueState);
            tvVenueCountry = itemView.findViewById(R.id.tvVenueCountry);
            tvVenueCategory = itemView.findViewById(R.id.tvVenueCategory);
            llDetails = itemView.findViewById(R.id.llDetails);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    llDetails.setVisibility(llDetails.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

}
