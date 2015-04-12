package io.embarque.embarque.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.parse.ParseObject;

import java.util.List;

import io.embarque.embarque.holders.FeedbackItemView;
import io.embarque.embarque.holders.VerticalSpacerView;

public class FeedAdapter extends RecyclerView.Adapter {
    private List<ParseObject> feedbackList;

    public void setFeedbackList(List<ParseObject> feedbackList) {
        this.feedbackList = feedbackList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return VerticalSpacerView.onCreateViewHolder(parent);
            default:
                return FeedbackItemView.onCreateViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                FeedbackItemView.onBindViewHolder((FeedbackItemView.FeedbackItemViewHolder) holder, feedbackList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        if (feedbackList == null) {
            return 0;
        }
        return feedbackList.size() + 2; // +2: header + footer
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == feedbackList.size() + 1) {
            return 1;
        }

        return 0;
    }
}
