package io.embarque.embarque.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.embarque.embarque.R;

public class VerticalSpacerView {
    public static RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_vertical_spacer, parent, false);

        return new VerticalSpacerViewHolder(view);
    }

    public static class VerticalSpacerViewHolder extends RecyclerView.ViewHolder {
        public VerticalSpacerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
