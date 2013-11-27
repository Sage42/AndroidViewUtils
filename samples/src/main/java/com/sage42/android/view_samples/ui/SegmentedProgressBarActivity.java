package com.sage42.android.view_samples.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;

import com.sage42.android.view.ui.SegmentedProgressBar;
import com.sage42.android.view_samples.R;

/**
 * Copyright (C) 2013- Sage 42 App Sdn Bhd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Corey Scott (corey.scott@sage42.com)
 *
 */
public class SegmentedProgressBarActivity extends Activity
{
    private static final int     TOTAL_TIME_IN_SEC = 20;

    private static final int     ONE_SECOND_IN_MS  = 1000;

    // view elements
    private SegmentedProgressBar mProgressBar1;

    // a countdown timer to provide a little action
    private CountDownTimer       mTimerCountUp;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // wire up the layout
        this.setContentView(R.layout.segmented_progress_bar_activity);

        // wire up the ui elements
        this.mProgressBar1 = (SegmentedProgressBar) this.findViewById(R.id.segmented_bar1);

        // enable the back btn on newer phones
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
        {
            this.enableUpButton();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void enableUpButton()
    {
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    @SuppressWarnings("synthetic-access")
    protected void onResume()
    {
        super.onResume();

        // start some timers so that things move
        this.mTimerCountUp = new CountDownTimer(TOTAL_TIME_IN_SEC * ONE_SECOND_IN_MS, ONE_SECOND_IN_MS)
        {
            @Override
            public void onTick(final long millisUntilFinished)
            {
                final double progress = ((TOTAL_TIME_IN_SEC - (millisUntilFinished / (double) ONE_SECOND_IN_MS)) / TOTAL_TIME_IN_SEC) * 100;
                SegmentedProgressBarActivity.this.mProgressBar1.setProgress((int) progress);
            }

            @Override
            public void onFinish()
            {
                SegmentedProgressBarActivity.this.mProgressBar1
                                .setProgress(SegmentedProgressBarActivity.this.mProgressBar1.getMax());
            }
        }.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // stop any running timers
        // there are needed to be clear and be sure that the timers don't cause exceptions when this activity is not in focus
        if (this.mTimerCountUp != null)
        {
            this.mTimerCountUp.cancel();
        }
    }

}
