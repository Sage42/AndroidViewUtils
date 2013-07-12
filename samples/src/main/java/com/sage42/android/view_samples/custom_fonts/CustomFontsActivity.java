package com.sage42.android.view_samples.custom_fonts;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.sage42.android.view_samples.R;

public class CustomFontsActivity extends Activity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // needed to ensure the custom fonts can be found/loaded
        MyFontManager.loadMyFonts(this);

        // wire up the layout
        this.setContentView(R.layout.custom_fonts_activity);

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
