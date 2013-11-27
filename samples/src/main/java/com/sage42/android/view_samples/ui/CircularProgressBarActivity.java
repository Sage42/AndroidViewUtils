package com.sage42.android.view_samples.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;

import com.sage42.android.view.ui.CircularProgressBar;
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
public class CircularProgressBarActivity extends Activity
{
    private static final int    ONE_SECOND_IN_MS = 1000;

    // view elements
    private CircularProgressBar mCountdownBar1;
    private CircularProgressBar mCountdownBar2;
    private CircularProgressBar mCountdownBar3;
    
    private CircularProgressBar mCountUpBar1;
    private CircularProgressBar mCountUpBar2;
    private CircularProgressBar mCountUpBar3;

    private CircularProgressBar mCounterNoText1;
    private CircularProgressBar mCounterNoText2;
    private CircularProgressBar mCounterNoText3;

    // some countdown timers to provide a little action
    private CountDownTimer      mTimerCountDown;
    private CountDownTimer      mTimerCountUp;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // wire up the layout
        this.setContentView(R.layout.circular_progress_bar_activity);

        // wire up the ui elements
        this.mCountdownBar1 = (CircularProgressBar) this.findViewById(R.id.countdown_bar1);
        this.mCountdownBar2 = (CircularProgressBar) this.findViewById(R.id.countdown_bar2);
        this.mCountdownBar3 = (CircularProgressBar) this.findViewById(R.id.countdown_bar3);

        this.mCountUpBar1 = (CircularProgressBar) this.findViewById(R.id.countup_bar1);
        this.mCountUpBar2 = (CircularProgressBar) this.findViewById(R.id.countup_bar2);
        this.mCountUpBar3 = (CircularProgressBar) this.findViewById(R.id.countup_bar3);

        this.mCounterNoText1 = (CircularProgressBar) this.findViewById(R.id.counter_no_text1);
        this.mCounterNoText2 = (CircularProgressBar) this.findViewById(R.id.counter_no_text2);
        this.mCounterNoText3 = (CircularProgressBar) this.findViewById(R.id.counter_no_text3);

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
    @SuppressWarnings("synthetic-access")
    protected void onResume()
    {
        super.onResume();

        // start some timers so that things move
        this.mTimerCountDown = new CountDownTimer(30 * ONE_SECOND_IN_MS, ONE_SECOND_IN_MS)
        {
            @Override
            public void onTick(final long millisUntilFinished)
            {
                final int secondsRemaining = (int) (millisUntilFinished / ONE_SECOND_IN_MS);
                CircularProgressBarActivity.this.mCountdownBar1.setProgress(secondsRemaining);
                CircularProgressBarActivity.this.mCountdownBar2.setProgress(secondsRemaining);
                CircularProgressBarActivity.this.mCountdownBar3.setProgress(secondsRemaining);

                CircularProgressBarActivity.this.mCounterNoText1.setProgress(secondsRemaining);
                CircularProgressBarActivity.this.mCounterNoText3.setProgress(secondsRemaining);
            }

            @Override
            public void onFinish()
            {
                CircularProgressBarActivity.this.mCountdownBar1.setProgress(0);
                CircularProgressBarActivity.this.mCountdownBar2.setProgress(0);
                CircularProgressBarActivity.this.mCountdownBar3.setProgress(0);

                CircularProgressBarActivity.this.mCounterNoText1.setProgress(0);
                CircularProgressBarActivity.this.mCounterNoText3.setProgress(0);

                // make it disappear (because we can)
                CircularProgressBarActivity.this.mCountdownBar3.setVisibility(View.INVISIBLE);
            }
        }.start();

        this.mTimerCountUp = new CountDownTimer(30 * ONE_SECOND_IN_MS, ONE_SECOND_IN_MS)
        {
            @Override
            public void onTick(final long millisUntilFinished)
            {
                final int secondsElapsed = 30 - ((int) (millisUntilFinished / ONE_SECOND_IN_MS));
                CircularProgressBarActivity.this.mCountUpBar1.setProgress(secondsElapsed);
                CircularProgressBarActivity.this.mCountUpBar2.setProgress(secondsElapsed);
                CircularProgressBarActivity.this.mCountUpBar3.setProgress(secondsElapsed);
                
                CircularProgressBarActivity.this.mCounterNoText2.setProgress(secondsElapsed);
            }

            @Override
            public void onFinish()
            {
                CircularProgressBarActivity.this.mCountUpBar1.setProgress(30);
                CircularProgressBarActivity.this.mCountUpBar2.setProgress(30);
                CircularProgressBarActivity.this.mCountUpBar3.setProgress(30);

                CircularProgressBarActivity.this.mCounterNoText2.setProgress(30);

                // make it disappear (because we can)
                CircularProgressBarActivity.this.mCountUpBar3.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // stop any running timers
        // there are needed to be clear and be sure that the timers dont cause exceptions when this activity is not in focus
        if (this.mTimerCountDown != null)
        {
            this.mTimerCountDown.cancel();
        }
        if (this.mTimerCountUp != null)
        {
            this.mTimerCountUp.cancel();
        }
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
}
