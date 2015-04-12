package io.embarque.embarque.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.embarque.embarque.R;

public class FeedbackItemView {

    public static RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_card_item, parent, false);

        return new FeedbackItemViewHolder(view);
    }

    public static void onBindViewHolder(FeedbackItemViewHolder holder, ParseObject feedback) {
        if (feedback.getString("flight") != null) {
            holder.flight.setText(feedback.getString("flight"));
        } else {
            holder.flight.setText(R.string.airport);
        }

        holder.punctuality.setText(feedback.getString("punctuality"));
        holder.information.setText(feedback.getString("information"));
        holder.wifi.setText(feedback.getString("wifi"));
        holder.food.setText(feedback.getString("food"));
        holder.conservation.setText(feedback.getString("conservation"));
        holder.security.setText(feedback.getString("security"));
    }

    public static class FeedbackItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.flight) TextView flight;
        @InjectView(R.id.time_ago) TextView timeAgo;
        @InjectView(R.id.punctuality) TextView punctuality;
        @InjectView(R.id.information) TextView information;
        @InjectView(R.id.wifi) TextView wifi;
        @InjectView(R.id.food) TextView food;
        @InjectView(R.id.conservation) TextView conservation;
        @InjectView(R.id.security) TextView security;

        public FeedbackItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
