package io.embarque.embarque.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.parse.ParseObject;

import java.util.List;

import io.embarque.embarque.holders.FeedbackItemView;

public class FeedAdapter extends RecyclerView.Adapter {
    private List<ParseObject> feedbackList;

    public void setFeedbackList(List<ParseObject> feedbackList) {
        this.feedbackList = feedbackList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return FeedbackItemView.onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeedbackItemView.onBindViewHolder((FeedbackItemView.FeedbackItemViewHolder) holder, feedbackList.get(position));
    }

    @Override
    public int getItemCount() {
        if (feedbackList == null) {
            return 0;
        }
        return feedbackList.size();
    }
}
