package com.sage42.android.view.fonts;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

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
/**
 * Auto-Scrolling or Marquee TextView
 * 
 * Notes:
 * Due to device/system limitations this may not work on when inside a RelativeLayout, 
 * unfortunately the reason/excuse for this is beyond me.
 * 
 * Reference: http://androidbears.stellarpc.net/?p=185
 *
 */
public class MyScrollingTextView extends TextView
{

    public MyScrollingTextView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
        this.init();
        this.initCustomFonts(context, attrs);
    }

    public MyScrollingTextView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        this.init();
        this.initCustomFonts(context, attrs);
    }

    public MyScrollingTextView(final Context context)
    {
        super(context);
        this.init();
        this.initCustomFonts(context, null);
    }

    private void init()
    {
        // setup the other text view properties
        super.setSingleLine(true);
        super.setEllipsize(TruncateAt.MARQUEE);
    }

    @Override
    protected void onFocusChanged(final boolean focused, final int direction, final Rect previouslyFocusedRect)
    {
        if (focused)
        {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(final boolean focused)
    {
        if (focused)
        {
            super.onWindowFocusChanged(focused);
        }
    }

    /**
     * Force this UI element to always think it is focused and therefore the marquee should play.
     * 
     * @see android.view.View#isFocused()
     */
    @Override
    public boolean isFocused()
    {
        return true;
    }

    /**
     * Extract any custom font related settings from supplied args.
     * 
     * @param context
     * @param attrs
     */
    private void initCustomFonts(final Context context, final AttributeSet attrs)
    {
        if (this.isInEditMode())
        {
            // this is preview mode so we need to stop processing
            return;
        }

        // Fonts work as a combination of particular family and the style. 
        final Typeface typeface = FontManager.extractTypeface(context, attrs);
        if (typeface != null)
        {
            this.setTypeface(typeface);
        }
    }

}
