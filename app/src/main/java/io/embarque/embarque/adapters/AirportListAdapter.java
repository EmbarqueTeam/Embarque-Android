package io.embarque.embarque.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.parse.ParseObject;

import java.util.List;

import io.embarque.embarque.holders.AirportListItemView;

public class AirportListAdapter extends RecyclerView.Adapter {
    private List<ParseObject> airports;

    public void setAirports(List<ParseObject> airports) {
        this.airports = airports;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AirportListItemView.onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AirportListItemView.onBindViewHolder((AirportListItemView.AirportListItemViewHolder) holder, airports.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (airports == null) {
            return 0;
        }
        return airports.size();
    }
}
