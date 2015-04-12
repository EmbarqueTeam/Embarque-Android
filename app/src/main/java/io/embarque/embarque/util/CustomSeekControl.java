package io.embarque.embarque.util;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import io.embarque.embarque.R;

public class CustomSeekControl {
    public static void setView(final View view, final double rating) {
        ((TextView) view.findViewById(R.id.grade)).setText(String.valueOf(rating));

        view.findViewById(R.id.seek_bg).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View seekBg = view.findViewById(R.id.seek_bg);
                View text = view.findViewById(R.id.grade);

                int mainSegmentSize =  seekBg.getWidth() / 6; // yep, I know
                int segmentSize = text.getWidth() / 6; // yep, I know
                float pos = (float) (rating + 3); // to 0 index this again

                text.setTranslationX(pos * mainSegmentSize - pos * segmentSize);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    seekBg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    seekBg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }
}
