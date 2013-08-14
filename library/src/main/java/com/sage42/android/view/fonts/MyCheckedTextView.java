package com.sage42.android.view.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

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
public class MyCheckedTextView extends CheckedTextView
{

    public MyCheckedTextView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
        this.initCustomFonts(context, attrs, defStyle);
    }

    public MyCheckedTextView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        this.initCustomFonts(context, attrs, -1);
    }

    public MyCheckedTextView(final Context context)
    {
        super(context);
        this.initCustomFonts(context, null, -1);
    }

    /**
     * Extract any custom font related settings from supplied args
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void initCustomFonts(final Context context, final AttributeSet attrs, final int defStyle)
    {
        if (this.isInEditMode())
        {
            // this is preview mode so we need to stop processing
            return;
        }

        // Fonts work as a combination of particular family and the style. 
        final TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.fonts);
        final String family = args.getString(R.styleable.fonts_fontFamily);
        final int style = args.getInt(R.styleable.fonts_android_textStyle, -1);
        args.recycle();

        if (family != null)
        {
            // Set the typeface based on the family and the style combination.
            this.setTypeface(FontManager.getInstance().get(context, family, style));
        }
    }

}
