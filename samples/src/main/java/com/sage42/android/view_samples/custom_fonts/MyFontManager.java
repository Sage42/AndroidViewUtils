package com.sage42.android.view_samples.custom_fonts;

import android.content.Context;

import com.sage42.android.view.FontManager;
import com.sage42.android.view_samples.R;

public class MyFontManager extends FontManager
{

    // These strings will be the values needed in the layout.xml files
    private static final String BAROQUE_SCRIPT = "baroqueScript"; //$NON-NLS-1$
    private static final String KATY_BERRY     = "katyBerry";    //$NON-NLS-1$

    public static void loadMyFonts(final Context context)
    {
        // add more fonts here
        FontManager.getInstance().addCustomFont(context, BAROQUE_SCRIPT, R.raw.baroque_script, null);
        FontManager.getInstance().addCustomFont(context, KATY_BERRY, R.raw.kberry, null);
    }

}
