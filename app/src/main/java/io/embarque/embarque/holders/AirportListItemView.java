package io.embarque.embarque.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.embarque.embarque.R;
import io.embarque.embarque.events.AirportClickedEvent;
import io.embarque.embarque.services.BusService;

public class AirportListItemView {

    public static RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_airport_list_item, parent, false);

        AirportListItemViewHolder holder = new AirportListItemViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusService.getBus().post(new AirportClickedEvent((int) v.getTag()));
            }
        });

        return holder;
    }

    public static void onBindViewHolder(AirportListItemViewHolder holder, ParseObject airport, int position) {
        holder.airportName.setText(airport.getString("name"));
        holder.airportLocation.setText(airport.getString("city"));

        if (airport.getParseFile("cover") != null) {
            Picasso.with(holder.airportPhoto.getContext()).load(airport.getParseFile("cover").getUrl())
                    .fit().centerCrop().into(holder.airportPhoto);
        }

        holder.itemView.setTag(position);
    }

    public static class AirportListItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.airport_photo) ImageView airportPhoto;
        @InjectView(R.id.airport_name) TextView airportName;
        @InjectView(R.id.airport_location) TextView airportLocation;

        public AirportListItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.inject(this, itemView);
        }
    }
}
