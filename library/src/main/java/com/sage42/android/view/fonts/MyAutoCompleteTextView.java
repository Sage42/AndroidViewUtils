package com.sage42.android.view.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

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
public class MyAutoCompleteTextView extends AutoCompleteTextView
{

    public MyAutoCompleteTextView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
        this.initCustomFonts(context, attrs);
    }

    public MyAutoCompleteTextView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        this.initCustomFonts(context, attrs);
    }

    public MyAutoCompleteTextView(final Context context)
    {
        super(context);
        this.initCustomFonts(context, null);
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
