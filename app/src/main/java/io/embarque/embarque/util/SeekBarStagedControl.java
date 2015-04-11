package io.embarque.embarque.util;

import android.widget.SeekBar;

public class SeekBarStagedControl {
    private static  final int SEEK_TOTAL = 100;

    private final SeekBar seekBar;
    private int steps;

    private int slice;
    private int lastProgress;

    public SeekBarStagedControl(SeekBar seekBar, int steps) {
        this.seekBar = seekBar;
        this.steps = steps;

        setUp();

        seekTo(0);
    }

    public void seekTo(int pos) {
        setSeekBar(pos);
    }

    public int getSelectedPos() {
        return getPosFromProgress(seekBar.getProgress());
    }

    private void setUp() {
        slice = SEEK_TOTAL / (steps - 1);
        seekBar.setMax(SEEK_TOTAL);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && Math.abs(lastProgress - progress) > 2) {
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(getPosFromProgress(seekBar.getProgress()));
            }
        });
    }

    private void setSeekBar(int pos) {
        seekBar.setProgress(pos * slice);
    }

    private int getPosFromProgress(int progress) {
        return Math.round((float) progress / (float) slice);
    }
}
