package io.embarque.embarque.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.embarque.embarque.R;
import io.embarque.embarque.adapters.FeedAdapter;
import io.embarque.embarque.data.ParseData;
import io.embarque.embarque.widgets.FixedRecyclerView;

public class FeedFragment extends Fragment {

    @InjectView(R.id.recycler_view) FixedRecyclerView recyclerView;
    @InjectView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @InjectView(R.id.no_content) TextView noContent;

    private FeedAdapter adapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.inject(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FeedAdapter();
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        getData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void getData() {
        noContent.setVisibility(View.GONE);

        ParseQuery.getQuery("Feedback").setLimit(100)
                .whereEqualTo("airport", ParseData.selectedAirport)
                .setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK)
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e != null) {
                            // error
                            return;
                        }

                        adapter.setFeedbackList(parseObjects);

                        if (parseObjects == null || parseObjects.size() == 0) {
                            noContent.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
