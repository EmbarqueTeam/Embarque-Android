package io.embarque.embarque.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.embarque.embarque.R;

public class AirportDetailsFragment extends Fragment {

    @InjectView(R.id.airport_content) LinearLayout airportContent;

    public AirportDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_airport_details, container, false);
        ButterKnife.inject(this, view);

        setUpView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void setUpView() {
        int[] titles = {
                R.string.pontualidade,
                R.string.informacoes,
                R.string.wifi,
                R.string.alimentacao,
                R.string.conservacao,
                R.string.seguranca
        };

        int[] icons = {
                R.drawable.clock,
                R.drawable.question,
                R.drawable.wifi,
                R.drawable.food,
                R.drawable.trashcan,
                R.drawable.security
        };

        int totalItems = titles.length;

        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (int i = 0; i < totalItems; i++) {
            View view = inflater.inflate(R.layout.view_resume_item, airportContent, false);

            ((ImageView) view.findViewById(R.id.icon)).setImageResource(icons[i]);
            ((TextView) view.findViewById(R.id.label)).setText(titles[i]);

            airportContent.addView(view);
        }
    }
}
