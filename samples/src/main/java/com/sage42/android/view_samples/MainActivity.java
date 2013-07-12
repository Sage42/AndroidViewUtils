package com.sage42.android.view_samples;

import com.sage42.android.view_samples.circular_pb.CircularProgressBarActivity;
import com.sage42.android.view_samples.custom_fonts.CustomFontsActivity;
import com.sage42.android.view_samples.marquee.MarqueeTextActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class MainActivity extends ListActivity
{
    // constants to map menu positions to something more readable
    private static final int MENU_CIRCULAR_PROGRESS_BAR = 0;
    private static final int MENU_CUSTOM_FONT_VIEWS     = 1;
    private static final int MENU_MARQUEE_TEXT          = 2;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // populate the "menu" list
        final String[] values = new String[]
        { this.getString(R.string.menu_circular_progress_bar), this.getString(R.string.menu_custom_font_views),
                        this.getString(R.string.menu_marquee_text) };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(final ListView listView, final View view, final int position, final long id)
    {
        switch (position)
        {
            case MENU_CIRCULAR_PROGRESS_BAR:
                this.startActivity(new Intent(this, CircularProgressBarActivity.class));
                return;

            case MENU_CUSTOM_FONT_VIEWS:
                this.startActivity(new Intent(this, CustomFontsActivity.class));
                return;

            case MENU_MARQUEE_TEXT:
                this.startActivity(new Intent(this, MarqueeTextActivity.class));
                return;

            default:
                // do nothing
                break;
        }

        super.onListItemClick(listView, view, position, id);
    }

}
