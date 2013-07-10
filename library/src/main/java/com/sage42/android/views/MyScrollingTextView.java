package com.sage42.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sage42.android.view.R;

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
 * Auto-Scrolling marquee text view
 * 
 * Source: http://androidbears.stellarpc.net/?p=185
 * 
 * @author http://androidbears.stellarpc.net/?page_id=195
 *
 */
public class MyScrollingTextView extends TextView
{
    public MyScrollingTextView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);

        // Fonts work as a combination of particular family and the style. 
        final TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.fonts);
        final String family = args.getString(R.styleable.fonts_fontFamily);
        final int style = args.getInt(R.styleable.fonts_android_textStyle, -1);
        args.recycle();

        if (!this.isInEditMode())
        {
            // Set the typeface based on the family and the style combination.
            this.setTypeface(FontManager.getInstance().get(context, family, style));
        }
    }

    public MyScrollingTextView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyScrollingTextView(final Context context)
    {
        super(context);
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

}
