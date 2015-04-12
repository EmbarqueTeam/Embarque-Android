package io.embarque.embarque.holders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
        if (!TextUtils.isEmpty(feedback.getString("company")) || !TextUtils.isEmpty(feedback.getString("flight"))) {
            StringBuilder sb = new StringBuilder();

            if (!TextUtils.isEmpty(feedback.getString("company"))) {
                sb.append(feedback.getString("company"));
            }

            if (!TextUtils.isEmpty(feedback.getString("flight"))) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(feedback.getString("flight"));
            }

            holder.flight.setText(sb.toString());
        } else {
            holder.flight.setText(R.string.airport);
        }

        holder.punctuality.setText(String.valueOf(feedback.getInt("punctuality")));
        holder.information.setText(String.valueOf(feedback.getInt("information")));
        holder.wifi.setText(String.valueOf(feedback.getInt("wifi")));
        holder.food.setText(String.valueOf(feedback.getInt("food")));
        holder.conservation.setText(String.valueOf(feedback.getInt("conservation")));
        holder.security.setText(String.valueOf(feedback.getInt("security")));
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
