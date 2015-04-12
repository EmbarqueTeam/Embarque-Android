package io.embarque.embarque.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.parse.ParseObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.embarque.embarque.R;
import io.embarque.embarque.data.ParseData;
import io.embarque.embarque.util.SeekBarStagedControl;

public class CreateFeedbackActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.feedback_content) LinearLayout feedbackContent;

    SeekBarStagedControl[] seekBarStagedControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ParseData.selectedAirport.getString("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setUpView() {
        // I'm sorry
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

        seekBarStagedControls = new SeekBarStagedControl[totalItems];

        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < totalItems; i++) {
            View view = inflater.inflate(R.layout.view_feedback_item, feedbackContent, false);

            ((ImageView) view.findViewById(R.id.icon)).setImageResource(icons[i]);
            ((TextView) view.findViewById(R.id.label)).setText(titles[i]);

            seekBarStagedControls[i] = new SeekBarStagedControl((SeekBar) view.findViewById(R.id.seek), 7);

            feedbackContent.addView(view);
        }

        View view = inflater.inflate(R.layout.view_send_button, feedbackContent, false);

        view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        feedbackContent.addView(view);
    }

    private void sendFeedback() {
        ParseObject feedback = new ParseObject("Feedback");

        feedback.add("punctuality", seekBarStagedControls[0].getSelectedPos() - 3);
        feedback.add("information", seekBarStagedControls[1].getSelectedPos() - 3);
        feedback.add("wifi", seekBarStagedControls[2].getSelectedPos() - 3);
        feedback.add("food", seekBarStagedControls[3].getSelectedPos() - 3);
        feedback.add("conservation", seekBarStagedControls[4].getSelectedPos() - 3);
        feedback.add("security", seekBarStagedControls[5].getSelectedPos() - 3);

        feedback.saveEventually();
    }
}
