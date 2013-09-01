package com.sage42.android.view.activities;

import android.app.Activity;
import android.os.Bundle;

import com.sage42.android.view.R;

public class MyTextViewTestActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // init the layout
        this.setContentView(R.layout.test_mytextview_activity);
    }

}
