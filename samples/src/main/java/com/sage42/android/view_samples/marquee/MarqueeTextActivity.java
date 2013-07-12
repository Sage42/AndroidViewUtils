package com.sage42.android.view_samples.marquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.sage42.android.view_samples.R;

public class MarqueeTextActivity extends Activity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // wire up the layout
        this.setContentView(R.layout.marquee_text_activity);

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

}
